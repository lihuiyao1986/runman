package com.clt.runman.model;

import java.util.List;

/**
 * 体验券查询响应
 * @author yanshengli
 * @since 2015-3-12
 */
public class ExperienceListQueryRespBean extends BaseRespBean
{

	/** 体验券列表 **/
	private List<Voucher> listCoupon;

	public List<Voucher> getListCoupon() {
		return listCoupon;
	}

	public void setListCoupon(List<Voucher> listCoupon) {
		this.listCoupon = listCoupon;
	}
	
}
