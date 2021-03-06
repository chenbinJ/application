package com.springboot.entity.chenbin.personnel.tax;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.entity.chenbin.personnel.pub_use.*;

import java.io.Serializable;
import java.util.List;

public class TaxParamBody implements Serializable {
    @JsonProperty("DYZT")
    private String DYZT;
    @JsonProperty("YYZT")
    private String YYZT;
    @JsonProperty("CFZT")
    private String CFZT;

    @JsonProperty("HTXX")
    private HTXX HTXX;
    @JsonProperty("YCSLXX")
    private YCSLXX YCSLXX;
    @JsonProperty("QSXX")
    private QSXX QSXX;
    @JsonProperty("JYQLRXX")
    private List<JYQLRXX> JYQLRXX;
    @JsonProperty("JYDLRXX")
    private List<JYQLRXX> JYDLRXX;
    @JsonProperty("FJXX")
    private List<FJXX> FJXX;

    public String getDYZT() {
        return DYZT;
    }

    @JsonIgnore
    public void setDYZT(String DYZT) {
        if(DYZT==null){
            DYZT = "";
        }
        this.DYZT = DYZT;
    }

    public String getYYZT() {
        return YYZT;
    }

    @JsonIgnore
    public void setYYZT(String YYZT) {
        if(YYZT==null){
            YYZT = "";
        }
        this.YYZT = YYZT;
    }

    public String getCFZT() {
        return CFZT;
    }

    @JsonIgnore
    public void setCFZT(String CFZT) {
        if(CFZT==null){
            CFZT = "";
        }
        this.CFZT = CFZT;
    }

    public com.springboot.entity.chenbin.personnel.pub_use.HTXX getHTXX() {
        return HTXX;
    }

    @JsonIgnore
    public void setHTXX(com.springboot.entity.chenbin.personnel.pub_use.HTXX HTXX) {
        this.HTXX = HTXX;
    }

    public com.springboot.entity.chenbin.personnel.pub_use.YCSLXX getYCSLXX() {
        return YCSLXX;
    }
    @JsonIgnore
    public void setYCSLXX(com.springboot.entity.chenbin.personnel.pub_use.YCSLXX YCSLXX) {
        this.YCSLXX = YCSLXX;
    }

    public com.springboot.entity.chenbin.personnel.pub_use.QSXX getQSXX() {
        return QSXX;
    }
    @JsonIgnore
    public void setQSXX(com.springboot.entity.chenbin.personnel.pub_use.QSXX QSXX) {
        this.QSXX = QSXX;
    }

    public List<com.springboot.entity.chenbin.personnel.pub_use.JYQLRXX> getJYQLRXX() {
        return JYQLRXX;
    }
    @JsonIgnore
    public void setJYQLRXX(List<com.springboot.entity.chenbin.personnel.pub_use.JYQLRXX> JYQLRXX) {
        this.JYQLRXX = JYQLRXX;
    }

    public List<com.springboot.entity.chenbin.personnel.pub_use.JYQLRXX> getJYDLRXX() {
        return JYDLRXX;
    }
    @JsonIgnore
    public void setJYDLRXX(List<com.springboot.entity.chenbin.personnel.pub_use.JYQLRXX> JYDLRXX) {
        this.JYDLRXX = JYDLRXX;
    }

    public List<com.springboot.entity.chenbin.personnel.pub_use.FJXX> getFJXX() {
        return FJXX;
    }
    @JsonIgnore
    public void setFJXX(List<com.springboot.entity.chenbin.personnel.pub_use.FJXX> FJXX) {
        this.FJXX = FJXX;
    }
}
