package com.springboot.util.chenbin;

import com.springboot.config.ZtgeoBizException;
import com.springboot.popj.pub_data.*;
import com.springboot.popj.registration.*;
import com.springboot.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
    数据处理基本UTIL工具类
 */
public class BusinessDealBaseUtil {
    //处理基本信息
    public static RegistrationBureau dealBaseInfo(SJ_Sjsq sjsq,String pid,boolean isSubmit,String bizType,String dealPerson,String areaNo){
        RegistrationBureau registrationBureau = new RegistrationBureau();
        registrationBureau.setPid(pid);//测试数据
        registrationBureau.setSubmitFlow(isSubmit);
        registrationBureau.setBizType(bizType);//#（抵押注销(个人)）
        registrationBureau.setOperatorName(dealPerson);//测试后续需要设置
        registrationBureau.setContactsAdress(sjsq.getNotifiedPersonAddress());//测试数据
        registrationBureau.setContacts(sjsq.getNotifiedPersonName());
        registrationBureau.setContactsPhone(sjsq.getNotifiedPersonTelephone());
        registrationBureau.setBusinessAreas(StringUtils.isNotBlank(sjsq.getDistrictCode()) ? sjsq.getDistrictCode() : areaNo);
        return registrationBureau;
    }
    //处理MortgageBizInfo，通过两个合同
    public static MortgageBizInfo getMortgageBizInfoByContract(Sj_Info_Jyhtxx jyht, Sj_Info_Dyhtxx dyht){
        MortgageBizInfo mortgageBizInfo = new MortgageBizInfo();
        mortgageBizInfo.setMortgageApplyDate(StringUtils.isNotBlank(dyht.getApplyTime())?dyht.getApplyTime(): TimeUtil.getTimeString(new Date()));
        mortgageBizInfo.setMortgageWay(dyht.getMortgageMode());
        mortgageBizInfo.setCreditAmount(dyht.getCreditAmount().toString());
        mortgageBizInfo.setEvaluationValue(dyht.getValuationValue().toString());
        mortgageBizInfo.setMortgageTerm(dyht.getMortgagePeriod());
        mortgageBizInfo.setMortgageStartDate(dyht.getMortgageStartingDate());
        mortgageBizInfo.setMortgageEndDate(dyht.getMortgageEndingDate());
        mortgageBizInfo.setMortgageReason(StringUtils.isNotBlank(dyht.getMortgageReason())?dyht.getMortgageReason():"购买商品房");
        mortgageBizInfo.setAbsoluteFact(dyht.getMaximumClaimConfirm());
        mortgageBizInfo.setHighestClaimAmount(dyht.getMaximumClaimAmount().toString());
        mortgageBizInfo.setHtbh(jyht.getContractRecordNumber());

        List<SJ_Qlr_Gl> dyqrs = dyht.getGlMortgageHolderVoList();
        List<SJ_Qlr_Gl> dyrs = dyht.getGlMortgagorVoList();
        List<DyqrGlMortgator> mortgageeInfoVoList = getMortgageeInfoVoList(dyqrs);
        List<DyrGlMortgator> mortgagorInfoVoList = getMortgagorInfoVoList(dyrs);
        List<RealEstateInfo> realEstateInfoVoList = getRealEstateInfoVoListByJyht(jyht);
        mortgageBizInfo.setMortgageeInfoVoList(mortgageeInfoVoList);
        mortgageBizInfo.setMortgagorInfoVoList(mortgagorInfoVoList);
        mortgageBizInfo.setRealEstateInfoVoList(realEstateInfoVoList);
        return mortgageBizInfo;
    }

    //抵押权人
    public static List<DyqrGlMortgator> getMortgageeInfoVoList(List<SJ_Qlr_Gl> dyqrs){
        List<DyqrGlMortgator> mortgageeInfoVoList = new ArrayList<DyqrGlMortgator>();
        for(SJ_Qlr_Gl dyqr:dyqrs){
            SJ_Qlr_Info person = dyqr.getRelatedPerson();
            DyqrGlMortgator mortgageeInfo = new DyqrGlMortgator();
            mortgageeInfo.setMortgageeName(person.getObligeeName());
            mortgageeInfo.setMortgageeIdType(person.getObligeeDocumentType());
            mortgageeInfo.setMortgageeId(person.getObligeeDocumentNumber());
            mortgageeInfoVoList.add(mortgageeInfo);
        }
        return mortgageeInfoVoList;
    }

    //抵押人
    public static List<DyrGlMortgator> getMortgagorInfoVoList(List<SJ_Qlr_Gl> dyrs){
        List<DyrGlMortgator> mortgagorInfoVoList = new ArrayList<DyrGlMortgator>();
        for(SJ_Qlr_Gl dyr:dyrs){
            SJ_Qlr_Info person = dyr.getRelatedPerson();
            DyrGlMortgator mortgagorInfo = new DyrGlMortgator();
            mortgagorInfo.setMortgagorId(person.getObligeeDocumentNumber());
            mortgagorInfo.setMortgagorIdType(person.getObligeeDocumentType());
            mortgagorInfo.setMortgagorName(person.getObligeeName());
            mortgagorInfoVoList.add(mortgagorInfo);
        }
        return mortgagorInfoVoList;
    }

    //抵押的不动产信息
    public static List<RealEstateInfo> getRealEstateInfoVoListByJyht(Sj_Info_Jyhtxx jyht){
        List<RealEstateInfo> realEstateInfoVoList = new ArrayList<RealEstateInfo>();
        RealEstateInfo realEstateInfo = new RealEstateInfo();
        List<SJ_Qlr_Gl> buyers = jyht.getGlHouseBuyerVoList();
        List<SJ_Qlr_Gl> sellers = jyht.getGlHouseSellerVoList();
        List<SJ_Bdc_Gl> bdc_gls = jyht.getGlImmovableVoList();
        realEstateInfo.setObligeeInfoVoList(getObligeeInfoVoList(buyers));
        realEstateInfo.setSalerInfoVoList(getSalerInfoVoList(sellers));
        realEstateInfo.setRealEstateUnitInfoVoList(getRealEstateUnitInfoVoList(bdc_gls));
        realEstateInfoVoList.add(realEstateInfo);
        return realEstateInfoVoList;
    }

    //权利人信息
    public static List<QlrGlMortgator> getObligeeInfoVoList(List<SJ_Qlr_Gl> buyers){
        List<QlrGlMortgator> obligeeInfoVoList = new ArrayList<QlrGlMortgator>();
        for(SJ_Qlr_Gl buyer:buyers){
            SJ_Qlr_Info person = buyer.getRelatedPerson();
            QlrGlMortgator obligeeInfo = new QlrGlMortgator();
            obligeeInfo.setObligeeName(person.getObligeeName());
            obligeeInfo.setObligeeIdType(person.getObligeeDocumentType());
            obligeeInfo.setObligeeId(person.getObligeeDocumentNumber());
            obligeeInfo.setCommonWay(buyer.getSharedMode());
            obligeeInfo.setSharedSharel(buyer.getSharedValue().toString());
            obligeeInfoVoList.add(obligeeInfo);
        }
        return obligeeInfoVoList;
    }

    //义务人信息
    public static List<SalerInfo> getSalerInfoVoList(List<SJ_Qlr_Gl> sellers){
        List<SalerInfo> salerInfoVoList = new ArrayList<SalerInfo>();
        for(SJ_Qlr_Gl seller:sellers){
            SJ_Qlr_Info person = seller.getRelatedPerson();
            SalerInfo salerInfo = new SalerInfo();
            salerInfo.setSalerName(person.getObligeeName());
            salerInfo.setSalerIdType(person.getObligeeDocumentType());
            salerInfo.setSalerId(person.getObligeeDocumentNumber());
            salerInfoVoList.add(salerInfo);
        }
        return salerInfoVoList;
    }

    //不动产单元
    public static List<RealEstateUnitInfo> getRealEstateUnitInfoVoList(List<SJ_Bdc_Gl> bdc_gls){
        List<RealEstateUnitInfo> realEstateUnitInfoVoList = new ArrayList<RealEstateUnitInfo>();
        for(SJ_Bdc_Gl bdc_gl:bdc_gls){
            RealEstateUnitInfo realEstateUnitInfo = new RealEstateUnitInfo();
            switch (bdc_gl.getImmovableType()){
                case "房地":
                    SJ_Bdc_Fw_Info fw = bdc_gl.getFwInfo();
                    realEstateUnitInfo.setHouseholdId(fw.getHouseholdId());
                    realEstateUnitInfo.setRealEstateUnitId(fw.getImmovableUnitNumber());
                    realEstateUnitInfo.setSit(fw.getHouseLocation());
                    break;
                case "宗地":
                    SJ_Bdc_Zd_Info zd = bdc_gl.getZdInfo();
                    realEstateUnitInfo.setHouseholdId(zd.getParcelUnicode());
                    realEstateUnitInfo.setSit(zd.getParcelLocation());
                    realEstateUnitInfo.setRealEstateUnitId(zd.getImmovableUnitNumber());
                    break;
                default:
                    throw new ZtgeoBizException("不动产类型未选择或类型不合法");
            }
            realEstateUnitInfoVoList.add(realEstateUnitInfo);
        }
        return realEstateUnitInfoVoList;
    }
}