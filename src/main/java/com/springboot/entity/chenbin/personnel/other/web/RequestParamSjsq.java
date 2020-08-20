package com.springboot.entity.chenbin.personnel.other.web;

import com.springboot.config.ZtgeoBizException;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

public class RequestParamSjsq implements Serializable {
    private String businessType;                                //业务类型
    private String registrationCategory;                        //登记大类（填入字典值）
    private String registrationSubclass;                        //登记小类
    private String registrationReason;                          //登记原因
    private String immovableType;                               //不动产类型，（房地/宗地）
    private String immovableSite;                               //不动产坐落,可以为空
    private String notifiedPersonName;                          //通知人名称
    private String notifiedPersonTelephone;                     //通知人电话
    private String notifiedPersonAddress;                       //通知人地址
    private String platform;                                    //发起平台(填web+)
    private String receiverId;                                  //收件人ID
    private String receiverName;                                //收件人NAME
    private String districtCode;                                //区县代码
    private List<String> bdcDatas;                              //证号集合
    private RequestParamJyht htdata;                            //合同数据

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getRegistrationCategory() {
        return registrationCategory;
    }

    public void setRegistrationCategory(String registrationCategory) {
        this.registrationCategory = registrationCategory;
    }

    public String getRegistrationSubclass() {
        return registrationSubclass;
    }

    public void setRegistrationSubclass(String registrationSubclass) {
        this.registrationSubclass = registrationSubclass;
    }

    public String getRegistrationReason() {
        return registrationReason;
    }

    public void setRegistrationReason(String registrationReason) {
        this.registrationReason = registrationReason;
    }

    public String getImmovableType() {
        return immovableType;
    }

    public void setImmovableType(String immovableType) {
        this.immovableType = immovableType;
    }

    public String getImmovableSite() {
        return immovableSite;
    }

    public void setImmovableSite(String immovableSite) {
        this.immovableSite = immovableSite;
    }

    public String getNotifiedPersonName() {
        return notifiedPersonName;
    }

    public void setNotifiedPersonName(String notifiedPersonName) {
        this.notifiedPersonName = notifiedPersonName;
    }

    public String getNotifiedPersonTelephone() {
        return notifiedPersonTelephone;
    }

    public void setNotifiedPersonTelephone(String notifiedPersonTelephone) {
        this.notifiedPersonTelephone = notifiedPersonTelephone;
    }

    public String getNotifiedPersonAddress() {
        return notifiedPersonAddress;
    }

    public void setNotifiedPersonAddress(String notifiedPersonAddress) {
        this.notifiedPersonAddress = notifiedPersonAddress;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public List<String> getBdcDatas() {
        return bdcDatas;
    }

    public void setBdcDatas(List<String> bdcDatas) {
        this.bdcDatas = bdcDatas;
    }

    public RequestParamJyht getHtdata() {
        return htdata;
    }

    public void setHtdata(RequestParamJyht htdata) {
        this.htdata = htdata;
    }

    public void checkSelfStandard() {
        if(StringUtils.isBlank(this.receiverId)||StringUtils.isBlank(this.receiverName)) {
            throw new ZtgeoBizException("收件人数据【receiver】未传入");
        }
        if(this.bdcDatas==null||this.bdcDatas.size()<1) {
            throw new ZtgeoBizException("不动产权证数据【bdcDatas】未传入！");
        }
        if(this.htdata == null) {
            throw new ZtgeoBizException("交易合同数据【htdata】未传入！");
        }
    }
}
