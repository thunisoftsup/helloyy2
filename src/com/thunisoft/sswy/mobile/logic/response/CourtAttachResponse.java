package com.thunisoft.sswy.mobile.logic.response;

import java.util.List;

import com.thunisoft.sswy.mobile.pojo.TCourtAttach;

public class CourtAttachResponse extends BaseResponse{
    
    private TCourtAttach gdtp;//过渡图片
    private List<TCourtAttach> ydtp;//引导图片
    private Long time;//更新时间
    
    public TCourtAttach getGdtp() {
        return gdtp;
    }
    public void setGdtp(TCourtAttach gdtp) {
        this.gdtp = gdtp;
    }
    public List<TCourtAttach> getYdtp() {
        return ydtp;
    }
    public void setYdtp(List<TCourtAttach> ydtp) {
        this.ydtp = ydtp;
    }
    public Long getTime() {
        return time;
    }
    public void setTime(Long time) {
        this.time = time;
    }
    
    
    
}
