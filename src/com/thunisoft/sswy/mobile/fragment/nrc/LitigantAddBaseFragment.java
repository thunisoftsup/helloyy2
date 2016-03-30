package com.thunisoft.sswy.mobile.fragment.nrc;

import com.thunisoft.sswy.mobile.pojo.TLayyDsr;

import android.support.v4.app.Fragment;

/**
 * 添加当事人 baseFragment
 * @author gewx
 *
 */
public class LitigantAddBaseFragment extends Fragment {

	/**
	 * 当事人
	 */
	private TLayyDsr dsr;
	
	/**
	 * 构造立案预约_当事人
	 */
	public void buildLayyDsr() {
		
	}

	/**  
	 * 获取  当事人
	 * @return dsr  
	 */
	
	public TLayyDsr getDsr() {
		return dsr;
	}

	/**  
	 * 设置  当事人
	 * @param dsr
	 */
	public void setDsr(TLayyDsr dsr) {
		this.dsr = dsr;
	}
}
