package com.clt.runman.machineAccess;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.clt.runman.utils.MyLog;

@SuppressLint({ "HandlerLeak" })
public class Machine {

    private final static String TAG       = Machine.class.getName ();
    private Context             mContext;
    private String              mMacAddress;
    private String              mUUID;
    private Handler             mStateHandler;
    private int                 state     = 0;
    private int                 errorCode = 0;

    private BluetoothAdapter    bluetooth;
    private ConnectThread       mConnectThread;
    private ConnectedThread     mConnectedThread;
    private static Machine      machine;

    // private static final UUID SPP_UUID = UUID.fromString ("00001101-0000-1000-8000-00805F9B34FB");

    public static synchronized Machine getMachine(Context context,String macAddress,String uuid,Handler stateHandler){
        if (null == machine) {
            machine = new Machine ();
        }
        machine.mContext = context;
        machine.mMacAddress = macAddress;
        machine.mUUID = uuid;
        machine.mStateHandler = stateHandler;
        machine.bluetooth = BluetoothAdapter.getDefaultAdapter ();
        IntentFilter intentFilter = new IntentFilter (BluetoothAdapter.ACTION_STATE_CHANGED);
        machine.mContext.registerReceiver (machine.mReceiver, intentFilter);

        IntentFilter filter = new IntentFilter (BluetoothDevice.ACTION_FOUND);
        machine.mContext.registerReceiver (machine.mReceiver, filter);

        filter = new IntentFilter (BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        machine.mContext.registerReceiver (machine.mReceiver, filter);

        return machine;
    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver () {

                                           public void onReceive(Context context,Intent intent){
                                               mContext.unregisterReceiver (mReceiver);
                                               String action = intent.getAction ();
                                               if (BluetoothAdapter.ACTION_STATE_CHANGED.equals (action)) {
                                                   if (Machine.this.bluetooth.getState () == BluetoothAdapter.STATE_ON) {
                                                       Machine.this.scan ();
                                                   } else {
                                                       Machine.this.changeState (MachineStatus.OFF);
                                                   }
                                               } else if (BluetoothDevice.ACTION_FOUND.equals (action)) {
                                                   BluetoothDevice device = (BluetoothDevice) intent
                                                           .getParcelableExtra ("android.bluetooth.device.extra.DEVICE");
                                                   if (Machine.this.mMacAddress.equals (device.getAddress ())) {
                                                       Machine.this.bluetooth.cancelDiscovery ();
                                                       Machine.this.btConnect (device);
                                                   }
                                               } else if ((BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals (action))
                                                       && (Machine.this.getStatus () == MachineStatus.SCANNING)) {
                                                   Machine.this.changeState (MachineStatus.OFF);
                                                   Machine.this.errorHandler (MachineErrors.MACINE_NOT_FOUND);
                                                   if (Machine.this.bluetooth.isDiscovering ()) {
                                                       Machine.this.bluetooth.cancelDiscovery ();
                                                   }
                                               }
                                           }
                                       };

    private final Handler    mHandler  = new Handler () {

                                           public void handleMessage(Message msg){
                                               switch (msg.what) {
                                                   case MachineMessage.ERROR:
                                                       break;
                                                   case MachineMessage.STATE:
                                                       byte[] readBuf = (byte[]) msg.obj;
                                                       String readMessage = new String (readBuf,0,msg.arg1);
                                                       String firstChar = readMessage.substring (0, 1);
                                                       if (MachineHResult.READY.equals (readMessage)) {
                                                           Machine.this.sendMessage (MachineCommand.MATCH + Machine.this.mUUID);
                                                           Machine.this.changeState (MachineStatus.MATCHING);
                                                       } else if (MachineHResult.SUCCESS.equals (firstChar)) {
                                                           Machine.this.errorCode = 0;
                                                           switch (Machine.this.getStatus ()) {
                                                               case MachineStatus.MATCHING:// 配对中
                                                                   Machine.this.changeState (MachineStatus.MATCHED);
                                                                   break;
                                                               case MachineStatus.READY:// 配对成功
                                                                   Machine.this.changeState (MachineStatus.TURN_WORK);// 工作中
                                                                   break;
                                                               case MachineStatus.STOP:
                                                                   Machine.this.changeState (MachineStatus.TURN_OFF);
                                                                   break;
                                                               case MachineStatus.TURN_OFF:
                                                                   Machine.this.changeState (MachineStatus.OFF);// 关机
                                                                   break;
                                                               default:
                                                                   break;
                                                           }
                                                       } else if (MachineHResult.STOP.equals (firstChar)) {
                                                           if (Machine.this.getStatus () == MachineStatus.TURN_WORK) {
                                                               Machine.this.changeState (MachineStatus.STOP);
                                                           }
                                                       } else if (MachineHResult.STANDBY.equals (readMessage)) {
                                                           Machine.this.changeState (MachineStatus.READY);
                                                       } else if (MachineHResult.WORKING.equals (readMessage)) {
                                                           Machine.this.changeState (MachineStatus.TURN_WORK);
                                                           Machine.this.errorHandler (MachineErrors.MACINE_NOT_READY);
                                                       } else if (MachineHResult.FAIL.equals (readMessage)) {
                                                           switch (Machine.this.getStatus ()) {
                                                               case MachineStatus.MATCHING:// 开始配对
                                                                   Machine.this.changeState (MachineStatus.OFF);
                                                                   Machine.this.errorHandler (MachineErrors.MACINE_CONNECT_FAILED);
                                                                   break;
                                                               case MachineStatus.READY:// 设备准备就绪
                                                                   Machine.this.errorHandler (MachineErrors.MACINE_NOT_READY);
                                                                   break;
                                                               case MachineStatus.TURN_WORK:// 设备工作中
                                                                   Machine.this.errorHandler (MachineErrors.MACINE_STOP_FAILED);
                                                                   break;
                                                               case MachineStatus.STOP:// 准备关机
                                                                   Machine.this.errorHandler (MachineErrors.MACINE_NOT_READY);
                                                                   break;
                                                               case MachineStatus.TURN_OFF:// 准备关机
                                                                   Machine.this.errorHandler (MachineErrors.MACINE_NOT_READY);
                                                                   break;

                                                               default:
                                                                   break;
                                                           }
                                                       } else if (MachineHResult.BATTERYLOW.equals (readMessage)) {
                                                           Machine.this.errorHandler (MachineErrors.MACINE_LOW_POWER);
                                                       } else {
                                                           Machine.this.errorHandler (MachineErrors.BLUETOOTH_CONNECT_FAILED);
                                                       }
                                               }
                                           }
                                       };

    /**
     *@Description: 设备配对
     *@Author: 张聪
     *@Since: 2015年3月14日下午5:23:26
     */
    public void connect(){
        if (this.getStatus () < MachineStatus.READY) {
            if (this.bluetooth != null) {
                if (this.bluetooth.getState () == BluetoothAdapter.STATE_OFF) {
                    this.changeState (MachineStatus.OPENNING);
                    this.bluetooth.enable ();
                } else {
                    scan ();
                }
            } else {
                this.errorHandler (MachineErrors.BLUETOOTH_NONE);
            }
        }
    }

    /**
     *@Description: 开始洗车
     *@Author: 张聪
     *@Since: 2015年4月1日下午5:18:12
     *@param orderId
     */
    public void startWork(String orderId){
        this.sendMessage (MachineCommand.WORK + orderId);
    }

    /**
     *@Description: 关闭水泵
     *@Author: 张聪
     *@Since: 2015年4月1日下午5:17:58
     */
    public void stopWork(){
        this.sendMessage (MachineCommand.STOP);
        // mContext.unregisterReceiver (machine.mReceiver);
    }

    /**
     *@Description: 交互设备关闭
     *@Author: 张聪
     *@Since: 2015年4月1日下午5:17:46
     */
    public void powerOff(){
        this.sendMessage (MachineCommand.OFF);
        mContext.unregisterReceiver (machine.mReceiver);
    }

    /**
     *@Description:状态切换 
     *@Author: 张聪
     *@Since: 2015年4月8日下午7:01:15
     *@param _state
     */
    private synchronized void changeState(int _state){
        Message stateMsg = new Message ();
        stateMsg.what = MachineMessage.STATE;
        if (_state == MachineStatus.STOP) {
            stateMsg.arg1 = MachineStatus.READY;
            this.state = MachineStatus.READY;
        } else {
            stateMsg.arg1 = _state;
            this.state = _state;
        }
        this.mStateHandler.sendMessage (stateMsg);
    }

    /**  
     *@Description: 获取状态 
     *@Author: 张聪
     *@Since: 2015年3月14日下午5:19:22
     *@return
     */
    public synchronized int getStatus(){
        return this.state;
    }

    /**
     *@Description: 失败消息提示 
     *@Author: 张聪
     *@Since: 2015年4月8日下午6:43:42
     *@param _error
     */
    private synchronized void errorHandler(int _error){
        this.errorCode = _error;
        Message errorMsg = new Message ();
        errorMsg.what = MachineMessage.ERROR;
        errorMsg.arg1 = _error;
        this.mStateHandler.sendMessage (errorMsg);
    }

    /**
     *@Description: 获取设备错误 
     *@Author: 张聪
     *@Since: 2015年3月14日下午5:19:36
     *@return
     */
    public synchronized String getError(){
        String errorInfo = null;
        if (errorCode != 0) {
            errorInfo = MachineErrors.machineErrorMsg (this.errorCode);
        }
        return errorInfo;
    }

    /**
     *@Description: 异常处理 
     *@Author: 张聪
     *@Since: 2015年4月8日下午6:47:03
     *@param _error
     */
    private void exceptionHandler(int _error){
        this.state = MachineStatus.OFF;
        this.errorCode = _error;

        Message errorMsg = new Message ();
        errorMsg.what = MachineMessage.EXCEPTION;
        errorMsg.arg1 = _error;
        this.mStateHandler.sendMessage (errorMsg);
        // Machine.this.mHandler.sendMessage (errorMsg);
    }

    /**
     *@Description: 蓝牙扫描 
     *@Author: 张聪
     *@Since: 2015年4月8日下午7:01:29
     */
    private void scan(){
        changeState (MachineStatus.SCANNING);
        Boolean _isFound = Boolean.valueOf (false);
        Set<BluetoothDevice> devices = this.bluetooth.getBondedDevices ();
        if (devices.size () > 0) {
            for ( BluetoothDevice bluetooth : devices ) {
                if (!this.mMacAddress.equals (bluetooth.getAddress ())) {
                    continue;
                } else {
                    _isFound = Boolean.valueOf (true);
                    btConnect (bluetooth);
                    break;
                }
            }
        }

        if (!_isFound.booleanValue ()) {
            if (this.bluetooth.isDiscovering ()) this.bluetooth.cancelDiscovery ();
            this.bluetooth.startDiscovery ();
        }
    }

    /**
     *@Description: 发送指令 
     *@Author: 张聪
     *@Since: 2015年4月8日下午7:01:51
     *@param message
     */
    private void sendMessage(String message){
        if (this.getStatus () < MachineStatus.CONNECTED) { return; }

        if (message.length () > 0) {
            byte[] send = message.getBytes ();
            write (send);
        }
    }

    /**
     *@Description: 写入指令
     *@Author: 张聪
     *@Since: 2015年4月8日下午7:02:03
     *@param out
     */
    public void write(byte[] out){
        ConnectedThread r;
        synchronized (this) {
            if (this.getStatus () < MachineStatus.CONNECTED) return;
            r = this.mConnectedThread;
        }
        r.write (out);
    }

    private synchronized void btConnect(BluetoothDevice device){
        changeState (MachineStatus.CONNECTTING);
        if (this.mConnectThread != null) {
            this.mConnectThread.cancel ();
            this.mConnectThread = null;
        }

        if (this.mConnectedThread != null) {
            this.mConnectedThread.cancel ();
            this.mConnectedThread = null;
        }
        this.mConnectThread = new ConnectThread (device);
        this.mConnectThread.start ();
    }

    private synchronized void btConnected(BluetoothSocket socket,BluetoothDevice device){
        if (this.mConnectThread != null) {
            this.mConnectThread.cancel ();
            this.mConnectThread = null;
        }

        if (this.mConnectedThread != null) {
            this.mConnectedThread.cancel ();
            this.mConnectedThread = null;
        }

        this.mConnectedThread = new ConnectedThread (socket);
        this.mConnectedThread.start ();
        changeState (MachineStatus.CONNECTED);
    }

    private class ConnectThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            this.mmDevice = device;
            BluetoothSocket tmp = null;
            try {
                // 这种写法，会导致系统崩溃: tmp = device.createRfcommSocketToServiceRecord (Machine.SPP_UUID);
                Method m = device.getClass ().getMethod ("createRfcommSocket", new Class[] { int.class });
                tmp = (BluetoothSocket) m.invoke (device, 1);
            } catch (NoSuchMethodException e) {
                MyLog.e (TAG, "蓝牙连接异常-1:", e);
            } catch (IllegalArgumentException e) {
                MyLog.e (TAG, "蓝牙连接异常-1:", e);
            } catch (IllegalAccessException e) {
                MyLog.e (TAG, "蓝牙连接异常-1:", e);
            } catch (InvocationTargetException e) {
                MyLog.e (TAG, "蓝牙连接异常-1:", e);
            }
            this.mmSocket = tmp;
        }

        public void run(){
            setName ("ConnectThread");

            if (Machine.this.bluetooth.isDiscovering ()) {
                Machine.this.bluetooth.cancelDiscovery ();
            }

            try {
                this.mmSocket.connect ();

            } catch (IOException e) {
                exceptionHandler (MachineErrors.BLUETOOTH_CONNECT_FAILED);
                MyLog.e (TAG, "蓝牙连接异常-2:", e);
                cancel ();
                return;
            }

            synchronized (Machine.this) {
                Machine.this.mConnectThread = null;
            }

            Machine.this.btConnected (this.mmSocket, this.mmDevice);
        }

        public void cancel(){
            try {
                this.mmSocket.close ();
            } catch (IOException localIOException) {
                MyLog.e (TAG, "mmSocket关闭异常-1:", localIOException);
            }
        }
    }

    private class ConnectedThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final InputStream     mmInStream;
        private final OutputStream    mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            this.mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream ();
                tmpOut = socket.getOutputStream ();
            } catch (IOException localIOException) {
                MyLog.e (TAG, "读取输入、出流异常", localIOException);
            }
            this.mmInStream = tmpIn;
            this.mmOutStream = tmpOut;
        }

        public void run(){
            while (true) {
                int count = 0;
                int readCount = 0;
                try {
                    while (count == 0) {
                        count = this.mmInStream.available ();
                    }
                    byte[] buffer = new byte[count];
                    while (readCount < count) {
                        readCount += this.mmInStream.read (buffer, readCount, count - readCount);
                    }
                    Machine.this.mHandler.obtainMessage (0, readCount, -1, buffer).sendToTarget ();
                } catch (IOException e) {
                    exceptionHandler (MachineErrors.BLUETOOTH_CONNECT_LOST);
                    MyLog.e (TAG, "读取指令异常", e);
                    break;
                }
            }
        }

        public void write(byte[] buffer){
            try {
                this.mmOutStream.write (buffer);
                Machine.this.mHandler.obtainMessage (1, -1, -1, buffer).sendToTarget ();
            } catch (IOException localIOException) {
                exceptionHandler (MachineErrors.BLUETOOTH_CONNECT_LOST);
                MyLog.e (TAG, "发送指令异常", localIOException);
            }
        }

        public void cancel(){
            try {
                this.mmSocket.close ();
            } catch (IOException localIOException) {
                MyLog.e (TAG, "蓝压关闭异常-2", localIOException);
            }
        }
    }
}