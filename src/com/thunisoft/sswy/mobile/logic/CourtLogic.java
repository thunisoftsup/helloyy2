package com.thunisoft.sswy.mobile.logic;

import java.util.Comparator;
import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.widget.RadioButton;

import com.thunisoft.sswy.mobile.datasource.CourtDao;
import com.thunisoft.sswy.mobile.logic.net.CourtResponseUtil;
import com.thunisoft.sswy.mobile.logic.net.NetUtils;
import com.thunisoft.sswy.mobile.logic.response.CourtResponse;
import com.thunisoft.sswy.mobile.pojo.TCourt;
import com.thunisoft.sswy.mobile.pojo.TProvince;

/**
 * 法院信息逻辑类
 * 
 * @author lulg
 * 
 */
@EBean(scope = Scope.Singleton)
public class CourtLogic {
    private static final String TAG = "CourtLogic";

    @Bean
    CourtDao courtDao;
    RadioButton btn;

    @Bean
    CourtResponseUtil responseUtil;

    @Bean
    NetUtils netUtils;

    /**
     * 获得省份列表
     * 
     * @return
     */
    public List<TProvince> loadProvinceList() {
        return courtDao.getProvinceList();
    }

    /**
     * 获得法院列表
     * 
     * @param fjm
     * @param time
     * @return
     */
    public CourtResponse loadCourtList(String fjm, long time) {
        CourtResponse cr = loadAndSaveCourtList(fjm, time);
        if (time != 0L) {
            cr.setData(courtDao.getCourtList(fjm));
        }
        List<TCourt> courtList = cr.getData();
//        if(courtList!=null && !courtList.isEmpty()) {
//            Collections.sort(courtList, new CourtComparator());
//        }
        return cr;
    }
    
    static class CourtComparator implements Comparator<TCourt> {

        @Override
        public int compare(TCourt o1, TCourt o2) {
            if(o1 == null || o2 == null) {
                return 0;
            }
            int result = o1.getCJb().compareTo(o2.getCJb());
            if(result == 0) {
                return Integer.parseInt(o1.getCId())-Integer.parseInt(o2.getCId());
            } else {
                return result;
            }
        }
        
    }

    /**
     * 加载并保存法院信息
     * 
     * @param fjm
     * @param time
     *            上次加载时间，如果是首次加载，time为null
     * @return
     */
    public CourtResponse loadAndSaveCourtList(String fjm, long time) {
        String url = netUtils.getMainAddress() + "/mobile/getFyList.htm";
        responseUtil.clearParams().addParam("fjm", fjm);
        if (time != 0L) {
            responseUtil.addParam("t", String.valueOf(time));
        }
        return responseUtil.getResponse(url, responseUtil.getParams());
    }

    public TCourt getCourt(String courtId) {
        return courtDao.getCourt(courtId);
    }

    public CourtResponse loadCourtInfo(String courtId) {
        String url = netUtils.getMainAddress() + "/mobile/getFyInfo.htm";
        responseUtil.clearParams().addParam("id", courtId);
        return responseUtil.getResponseNoUnzip(url, responseUtil.getParams());
    }

}
