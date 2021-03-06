package com.springboot.popj.pub_data;


import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class Sj_Info_Qsxx extends SJ_Information {

    @ApiModelProperty(value = "系统税票号码")
    private String xtsphm;//系统税票号码
    @ApiModelProperty(value = "合同编号")
    private String htbh;//合同编号
    @ApiModelProperty(value = "纳税人识别号")
    private String nsrsbh;  //纳税人识别号
    @ApiModelProperty(value = "zrfcsfbz")
    private String zrfcsfbz; //
    @ApiModelProperty(value = "纳税人名称")
    private String nsrmc;   //纳税人名称
    @ApiModelProperty(value = "电子税票号码")
    private String dzsphm;//电子税票号码
    @ApiModelProperty(value = "凭证种类代码")
    private String pzzlDm;//凭证种类代码
    @ApiModelProperty(value = "票证子规代码")
    private String pzzgDm;//票证子规代码
    @ApiModelProperty(value = "票证号码")
    private String pzhm;//票证号码
    @ApiModelProperty(value = "税款所属期起")
    private String skssqq;//税款所属期起
    @ApiModelProperty(value = "税款所属期止")
    private String skssqz;//税款所属期止
    @ApiModelProperty(value = "征收项目代码")
    private String zsxmDm;//征收项目代码
    @ApiModelProperty(value = "征收品目代码")
    private String zspmDm;//征收品目代码
    @ApiModelProperty(value = "征收子目代码")
    private String zszmDm;//征收子目代码
    @ApiModelProperty(value = "征收项目名称")
    private String zsxmMc;//征收项目名称
    @ApiModelProperty(value = "征收品目名称")
    private String zspmMc;//征收品目名称
    @ApiModelProperty(value = "征收子目名称")
    private String zszmMc;//征收子目名称
    @ApiModelProperty(value = "计税依据")
    private String jsyj;//计税依据
    @ApiModelProperty(value = "税率")
    private String sl;//税率
    @ApiModelProperty(value = "实际缴纳金额")
    private Long sjje;//实际缴纳金额
    @ApiModelProperty(value = "主管税务机关科分局")
    private String zgswskfjDm;//主管税务机关科分局
    @ApiModelProperty(value = "征收税务机关")
    private String zsswjgDm;//征收税务机关
    @ApiModelProperty(value = "税款所属机关")
    private String skssswjgDm;//税款所属机关
    @ApiModelProperty(value = "主管税务机关科分局名称")
    private String zgswskfjMc;
    @ApiModelProperty(value = "征收税务机关名称")
    private String zsswjgMc;
    @ApiModelProperty(value = "税款所属机关名称")
    private String skssswjgMc;
    @ApiModelProperty(value = "扣缴日期")
    private String kjrq;//扣缴日期
    @ApiModelProperty(value = "备注")
    private String bz;//备注
    @ApiModelProperty(value = "不动产单元号")
    private String bdcdyh;//不动产单元号
    @ApiModelProperty(value = "电子税票BASE64")
    private String dzspBase64;//电子税票base64



    private String ext1;

    private String ext2;

    private String ext3;


    public String getXtsphm() {
        return xtsphm;
    }

    public void setXtsphm(String xtsphm) {
        this.xtsphm = xtsphm;
    }

    public String getHtbh() {
        return htbh;
    }

    public void setHtbh(String htbh) {
        this.htbh = htbh;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public String getNsrsbh() {
        return nsrsbh;
    }

    public void setNsrsbh(String nsrsbh) {
        this.nsrsbh = nsrsbh;
    }

    public String getZrfcsfbz() {
        return zrfcsfbz;
    }

    public void setZrfcsfbz(String zrfcsfbz) {
        this.zrfcsfbz = zrfcsfbz;
    }

    public String getNsrmc() {
        return nsrmc;
    }

    public void setNsrmc(String nsrmc) {
        this.nsrmc = nsrmc;
    }

    public String getDzsphm() {
        return dzsphm;
    }

    public void setDzsphm(String dzsphm) {
        this.dzsphm = dzsphm;
    }

    public String getPzzlDm() {
        return pzzlDm;
    }

    public void setPzzlDm(String pzzlDm) {
        this.pzzlDm = pzzlDm;
    }

    public String getPzzgDm() {
        return pzzgDm;
    }

    public void setPzzgDm(String pzzgDm) {
        this.pzzgDm = pzzgDm;
    }

    public String getPzhm() {
        return pzhm;
    }

    public void setPzhm(String pzhm) {
        this.pzhm = pzhm;
    }

    public String getSkssqq() {
        return skssqq;
    }

    public void setSkssqq(String skssqq) {
        this.skssqq = skssqq;
    }

    public String getSkssqz() {
        return skssqz;
    }

    public void setSkssqz(String skssqz) {
        this.skssqz = skssqz;
    }

    public String getZsxmDm() {
        return zsxmDm;
    }

    public void setZsxmDm(String zsxmDm) {
        this.zsxmDm = zsxmDm;
    }

    public String getZspmDm() {
        return zspmDm;
    }

    public void setZspmDm(String zspmDm) {
        this.zspmDm = zspmDm;
    }

    public String getZszmDm() {
        return zszmDm;
    }

    public void setZszmDm(String zszmDm) {
        this.zszmDm = zszmDm;
    }

    public String getZsxmMc() {
        return zsxmMc;
    }

    public void setZsxmMc(String zsxmMc) {
        this.zsxmMc = zsxmMc;
    }

    public String getZspmMc() {
        return zspmMc;
    }

    public void setZspmMc(String zspmMc) {
        this.zspmMc = zspmMc;
    }

    public String getZszmMc() {
        return zszmMc;
    }

    public void setZszmMc(String zszmMc) {
        this.zszmMc = zszmMc;
    }

    public String getJsyj() {
        return jsyj;
    }

    public void setJsyj(String jsyj) {
        this.jsyj = jsyj;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public Long getSjje() {
        return sjje;
    }

    public void setSjje(Long sjje) {
        this.sjje = sjje;
    }

    public String getZgswskfjDm() {
        return zgswskfjDm;
    }

    public void setZgswskfjDm(String zgswskfjDm) {
        this.zgswskfjDm = zgswskfjDm;
    }

    public String getZsswjgDm() {
        return zsswjgDm;
    }

    public void setZsswjgDm(String zsswjgDm) {
        this.zsswjgDm = zsswjgDm;
    }

    public String getSkssswjgDm() {
        return skssswjgDm;
    }

    public void setSkssswjgDm(String skssswjgDm) {
        this.skssswjgDm = skssswjgDm;
    }

    public String getZgswskfjMc() {
        return zgswskfjMc;
    }

    public void setZgswskfjMc(String zgswskfjMc) {
        this.zgswskfjMc = zgswskfjMc;
    }

    public String getZsswjgMc() {
        return zsswjgMc;
    }

    public void setZsswjgMc(String zsswjgMc) {
        this.zsswjgMc = zsswjgMc;
    }

    public String getSkssswjgMc() {
        return skssswjgMc;
    }

    public void setSkssswjgMc(String skssswjgMc) {
        this.skssswjgMc = skssswjgMc;
    }

    public String getKjrq() {
        return kjrq;
    }

    public void setKjrq(String kjrq) {
        this.kjrq = kjrq;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
        this.bdcdyh = bdcdyh;
    }

    public String getDzspBase64() {
        return dzspBase64;
    }

    public void setDzspBase64(String dzspBase64) {
        this.dzspBase64 = dzspBase64;
    }
}
