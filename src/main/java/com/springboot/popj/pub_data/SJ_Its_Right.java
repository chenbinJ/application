package com.springboot.popj.pub_data;

import com.springboot.entity.newPlat.query.bizData.fromSY.cqzs.Dyxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Cfxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.cqzs.Yyxx;
import com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Djxx;

import java.io.Serializable;

import static com.springboot.constant.chenbin.KeywordConstant.*;

public class SJ_Its_Right implements Serializable {
    private String id;
    private String infoId;
    private String registerNumber;
    private String itsRightStart;
    private String itsRightEnd;
    private String itsRightLimit;
    private String itsRightType;
    private String immovableUnitNumber;
    private String status;
    private String ext1;
    private String ext2;
    private String ext3;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getItsRightStart() {
        return itsRightStart;
    }

    public void setItsRightStart(String itsRightStart) {
        this.itsRightStart = itsRightStart;
    }

    public String getItsRightEnd() {
        return itsRightEnd;
    }

    public void setItsRightEnd(String itsRightEnd) {
        this.itsRightEnd = itsRightEnd;
    }

    public String getItsRightLimit() {
        return itsRightLimit;
    }

    public void setItsRightLimit(String itsRightLimit) {
        this.itsRightLimit = itsRightLimit;
    }

    public String getItsRightType() {
        return itsRightType;
    }

    public void setItsRightType(String itsRightType) {
        this.itsRightType = itsRightType;
    }

    public String getImmovableUnitNumber() {
        return immovableUnitNumber;
    }

    public void setImmovableUnitNumber(String immovableUnitNumber) {
        this.immovableUnitNumber = immovableUnitNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public SJ_Its_Right initByDyxx(Dyxx dyxx){
        this.setRegisterNumber(dyxx.getYwh());
        this.setImmovableUnitNumber(dyxx.getBdcdyh());
        this.setItsRightType(BDC_ITS_RIGHT_DY);
        this.setItsRightStart(dyxx.getDykssj());
        this.setItsRightEnd(dyxx.getDyjssj());
        return this;
    }
    public SJ_Its_Right initByCfxx(Cfxx cfxx){
        this.setRegisterNumber(cfxx.getYwh());
        this.setImmovableUnitNumber(cfxx.getBdcdyh());
        this.setItsRightType(BDC_ITS_RIGHT_CF);
        this.setItsRightLimit(cfxx.getCfqx());
        this.setItsRightStart(cfxx.getCfkssj());
        this.setItsRightEnd(cfxx.getCfjssj());
        return this;
    }
    public SJ_Its_Right initByYyxx(Yyxx yyxx){
        this.setRegisterNumber(yyxx.getYwh());
        this.setImmovableUnitNumber(yyxx.getBdcdyh());
        this.setItsRightType(BDC_ITS_RIGHT_YY);
        return this;
    }
    public SJ_Its_Right initByDjxx(Djxx djxx){
        this.setRegisterNumber(djxx.getYwh());
        this.setImmovableUnitNumber(djxx.getBdcdyh());
        this.setItsRightType(BDC_ITS_RIGHT_DJ);
        this.setItsRightLimit(djxx.getDjqx());
        this.setItsRightStart(djxx.getDjkssj());
        this.setItsRightEnd(djxx.getDjjssj());
        return this;
    }
}
