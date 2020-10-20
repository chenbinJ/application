package com.springboot.entity.chenbin.personnel.pub_use;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class QSXX implements Serializable {
    @JsonProperty("BDCZH")
    private String BDCZH;
    @JsonProperty("JZMJ")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal JZMJ = new BigDecimal(-999);
    @JsonProperty("TNMJ")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal TNMJ = new BigDecimal(-999);
    @JsonProperty("ZL")
    private String ZL;
    @JsonProperty("YT")
    private String YT;
    @JsonProperty("TDSYQR")
    private String TDSYQR;
    @JsonProperty("TDHQFS")
    private String TDHQFS = "";

    @JsonProperty("FWXX")
    private List<FWXX> FWXX;

    /*
        10/16 泗洪住建新增字段
     */
    /** 土地证号 */
    @JsonProperty("TDZH")
    private String TDZH;
    /** 土地权利信息 */
    @JsonProperty("TDQLXX")
    private String TDQLXX;
    /** 数据提供单位 */
    @JsonProperty("provideUnit")
    private String provideUnit;

    public String getBDCZH() {
        return BDCZH;
    }
    @JsonIgnore
    public void setBDCZH(String BDCZH) {
        this.BDCZH = BDCZH;
    }

    public BigDecimal getJZMJ() {
        return JZMJ;
    }
    @JsonIgnore
    public void setJZMJ(BigDecimal JZMJ) {
        if(JZMJ==null){
            JZMJ = new BigDecimal(-999);
        }
        this.JZMJ = JZMJ;
    }

    public BigDecimal getTNMJ() {
        return TNMJ;
    }
    @JsonIgnore
    public void setTNMJ(BigDecimal TNMJ) {
        if(TNMJ==null){
            TNMJ = new BigDecimal(-999);
        }
        this.TNMJ = TNMJ;
    }

    public String getZL() {
        return ZL;
    }
    @JsonIgnore
    public void setZL(String ZL) {
        this.ZL = ZL;
    }

    public String getYT() {
        return YT;
    }
    @JsonIgnore
    public void setYT(String YT) {
        this.YT = YT;
    }

    public List<com.springboot.entity.chenbin.personnel.pub_use.FWXX> getFWXX() {
        return FWXX;
    }
    @JsonIgnore
    public void setFWXX(List<com.springboot.entity.chenbin.personnel.pub_use.FWXX> FWXX) {
        this.FWXX = FWXX;
    }

    public String getTDSYQR() {
        return TDSYQR;
    }
    @JsonIgnore
    public void setTDSYQR(String TDSYQR) {
        this.TDSYQR = TDSYQR;
    }

    public String getTDHQFS() {
        return TDHQFS;
    }
    @JsonIgnore
    public void setTDHQFS(String TDHQFS) {
        if(TDHQFS!=null) {
            this.TDHQFS = TDHQFS;
        }
    }

    public String getTDZH() {
        return TDZH;
    }
    @JsonIgnore
    public void setTDZH(String TDZH) {
        this.TDZH = TDZH;
    }

    public String getTDQLXX() {
        return TDQLXX;
    }
    @JsonIgnore
    public void setTDQLXX(String TDQLXX) {
        this.TDQLXX = TDQLXX;
    }

    public String getProvideUnit() {
        return provideUnit;
    }

    @JsonIgnore
    public void setProvideUnit(String provideUnit) {
        this.provideUnit = provideUnit;
    }
}
