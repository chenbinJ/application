package com.springboot.util.chenbin;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.springboot.config.Msgagger;
import com.springboot.config.ZtgeoBizException;
import com.springboot.constant.penghao.BizOrBizExceptionConstant;
import com.springboot.entity.SJ_Fjfile;
import com.springboot.entity.SJ_Fjinst;
import com.springboot.entity.chenbin.personnel.pub_use.*;
import com.springboot.entity.chenbin.personnel.req.*;
import com.springboot.entity.chenbin.personnel.tax.TaxParamBody;
import com.springboot.entity.chenbin.personnel.tra.TraParamBody;
import com.springboot.popj.pub_data.*;
import com.springboot.popj.registration.*;
import com.springboot.util.DateUtils;
import com.springboot.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

import static com.springboot.constant.chenbin.KeywordConstant.*;

/*
    数据处理基本UTIL工具类
 */
@Slf4j
public class BusinessDealBaseUtil {
    //处理基本信息
    public static RegistrationBureau dealBaseInfo(SJ_Sjsq sjsq, String pid, boolean isSubmit, String bizType, String dealPerson, String areaNo) {
        RegistrationBureau registrationBureau = new RegistrationBureau();
        List<SJ_Sjsq_User_Ext> userExts = sjsq.getUserExtVoList();
        SJ_Sjsq_User_Ext bdcUser = null;
        if(userExts!=null && userExts.size()>0){
            for(SJ_Sjsq_User_Ext userExt:userExts){
                if(
                        userExt!=null
                        && StringUtils.isNotBlank(userExt.getAdaptSys())
                        && BizOrBizExceptionConstant.BDC_BIZ_ADOBE_CODE.equals(userExt.getAdaptSys())
                ){
                    boolean isBreak = true;
                    if(StringUtils.isNotBlank(userExt.getUserCode())){
                        dealPerson = userExt.getUserCode();
                    }else{
                        isBreak = isBreak && false;
                    }
                    if(StringUtils.isNotBlank(userExt.getBizCode())){
                        pid = userExt.getBizCode();
                    }else{
                        isBreak = isBreak && false;
                    }
                    if(isBreak)
                        break;
                }
            }
        }
        registrationBureau.setPid(pid);//测试数据
        registrationBureau.setSubmitFlow(isSubmit);
        registrationBureau.setBizType(bizType);//#（抵押注销(个人)）
        registrationBureau.setOperatorName(dealPerson);//测试后续需要设置
        registrationBureau.setContactsAdress(StringUtils.isBlank(sjsq.getNotifiedPersonAddress()) ? (StringUtils.isBlank(sjsq.getImmovableSite())?"无":sjsq.getImmovableSite()) : sjsq.getNotifiedPersonAddress());//测试数据
        registrationBureau.setContacts(sjsq.getNotifiedPersonName());
        registrationBureau.setContactsPhone(sjsq.getNotifiedPersonTelephone());
        registrationBureau.setBusinessAreas(StringUtils.isNotBlank(sjsq.getDistrictCode()) ? sjsq.getDistrictCode() : areaNo);
        return registrationBureau;
    }

    public static String getModeBySub(String djxl){
        String mode;
        switch (djxl){
            case "最高额抵押权":
                mode = "最高额抵押";
                break;
            default:
                mode = "一般抵押";
                break;
        }
        return mode;
    }

    //处理MortgageBizInfo，通过两个合同
    public static MortgageBizInfo getMortgageBizInfoByContract(Sj_Info_Jyhtxx jyht, Sj_Info_Dyhtxx dyht, String idType) {
        MortgageBizInfo mortgageBizInfo = new MortgageBizInfo();
        mortgageBizInfo.setMortgageApplyDate(StringUtils.isNotBlank(dyht.getApplyTime()) ? dyht.getApplyTime() : TimeUtil.getTimeString(new Date()));
        mortgageBizInfo.setRegisterSubType(StringUtils.isBlank(dyht.getRegistrationSubclass())?"一般抵押权":dyht.getRegistrationSubclass());
        mortgageBizInfo.setMortgageWay(StringUtils.isBlank(dyht.getMortgageMode())?getModeBySub(dyht.getRegistrationSubclass()):dyht.getMortgageMode());
        mortgageBizInfo.setCreditAmount(dyht.getCreditAmount() == null ? null : dyht.getCreditAmount().toString());
        mortgageBizInfo.setEvaluationValue(dyht.getValuationValue() == null ? null : dyht.getValuationValue().toString());
        mortgageBizInfo.setMortgageTerm(dyht.getMortgagePeriod());
        mortgageBizInfo.setMortgageStartDate(DateUtils.strToDate(dyht.getMortgageStartingDate()));
        mortgageBizInfo.setMortgageEndDate(DateUtils.strToDate(dyht.getMortgageEndingDate()));
        mortgageBizInfo.setMortgageReason(StringUtils.isNotBlank(dyht.getMortgageReason()) ? dyht.getMortgageReason() : "购买商品房");
        mortgageBizInfo.setAbsoluteFact(dyht.getMaximumClaimConfirm());
        mortgageBizInfo.setHighestClaimAmount(dyht.getMaximumClaimAmount() == null ? null : dyht.getMaximumClaimAmount().toString());
        mortgageBizInfo.setHtbh(jyht.getContractRecordNumber());

        List<SJ_Qlr_Gl> dyqrs = dyht.getGlMortgageHolderVoList();
        List<SJ_Qlr_Gl> dyrs = dyht.getGlMortgagorVoList();
        List<DyqrGlMortgator> mortgageeInfoVoList = getMortgageeInfoVoList(dyqrs, idType);
        List<DyrGlMortgator> mortgagorInfoVoList = getMortgagorInfoVoList(dyrs, idType);
        List<SJ_Qlr_Gl> dyqrdls = dyht.getGlMortgageeAgentInfoVoList();//抵押权代理人
        List<SJ_Qlr_Gl> dyrdls = dyht.getGlMortgagorAgentInfoVoList();//抵押代理人
        if(dyqrdls!=null && dyqrdls.size()>0){
            mortgageBizInfo.setMortgageeAgentInfoVoList(getAgentInfoVoList(dyqrdls, idType));
        }
        if(dyrdls!=null && dyrdls.size()>0){
            mortgageBizInfo.setMortgagorAgentInfoVoList(getAgentInfoVoList(dyrdls, idType));
        }
        List<RealEstateInfo> realEstateInfoVoList = getRealEstateInfoVoListByJyht(jyht, idType);
        mortgageBizInfo.setMortgageeInfoVoList(mortgageeInfoVoList);
        mortgageBizInfo.setMortgagorInfoVoList(mortgagorInfoVoList);
        mortgageBizInfo.setRealEstateInfoVoList(realEstateInfoVoList);
        return mortgageBizInfo;
    }

    public static MortgageBizInfo getMortgageBizInfoByImmovList(List<SJ_Info_Bdcqlxgxx> bdcqls,Sj_Info_Dyhtxx dyhtxx){
        MortgageBizInfo mortgageBizInfo = new MortgageBizInfo();

        return mortgageBizInfo;
    }

    public static MortgageBizInfo getMortgageBizInfoByDyhtAndBiztype(SJ_Sjsq sjsq,String idType,boolean isTransfer){
        if(sjsq==null || sjsq.getMortgageContractInfo()==null){
            throw new ZtgeoBizException("抵押登记缺失必要数据，检查是否传入参数或传入的参数是否包含抵押合同信息");
        }
        MortgageBizInfo mortgageBizInfo = null;
        if(isTransfer){
            if(sjsq.getTransactionContractInfo()==null){
                throw new ZtgeoBizException("转移及抵押登记缺失必要数据，检查传入参数是否包含交易合同信息");
            }
            mortgageBizInfo = getMortgageBizInfoByContract(sjsq.getTransactionContractInfo(),sjsq.getMortgageContractInfo(),idType);
        }else{
            if(sjsq.getImmovableRightInfoVoList()==null || sjsq.getImmovableRightInfoVoList().size()==0){
                throw new ZtgeoBizException("一般抵押登记缺失必要数据，检查传入参数是否包含不动产权属信息");
            }
            mortgageBizInfo = getMortgageBizInfoByImmovList(sjsq.getImmovableRightInfoVoList(),sjsq.getMortgageContractInfo());
        }
        return mortgageBizInfo;
    }

    public static TransferBizInfo getTransferBizInfoByJyhtAndBdcqls(SJ_Sjsq sjsq,String idType){
        if(sjsq==null || sjsq.getImmovableRightInfoVoList()==null || sjsq.getTransactionContractInfo()==null){
            throw new ZtgeoBizException("转移登记缺失必要数据，检查传入参数是否包含待转移的权属列表以及交易合同信息");
        }
        TransferBizInfo transferBizInfo = new TransferBizInfo();
        //加载转移信息
        Sj_Info_Jyhtxx sjInfoJyhtxx = sjsq.getTransactionContractInfo();
        List<SJ_Info_Bdcqlxgxx> immovableRightInfoVoList = sjsq.getImmovableRightInfoVoList();

        SJ_Info_Bdcqlxgxx immovableRightInfo_bdcqz = null;
        SJ_Info_Bdcqlxgxx immovableRightInfo_td = null;
        for(SJ_Info_Bdcqlxgxx immovableRightInfo:immovableRightInfoVoList){
            if(StringUtils.isNotBlank(immovableRightInfo.getDataType())
                    && "主设施".equals(immovableRightInfo.getDataType())
                    && !immovableRightInfo.getCertificateType().equals("土地证")
            ){
                immovableRightInfo_bdcqz = immovableRightInfo;
            }
            if(immovableRightInfo.getCertificateType().equals("土地证")){
                immovableRightInfo_td = immovableRightInfo;
            }
        }
        if(immovableRightInfo_td!=null) {
            transferBizInfo.setLandCertificate(immovableRightInfo_td.getImmovableCertificateNo());//土地证
        }
        transferBizInfo.setRealEstateId(immovableRightInfo_bdcqz.getImmovableCertificateNo());//不动产权证
        transferBizInfo.setHtbh(sjInfoJyhtxx.getContractRecordNumber());//合同备案号
        transferBizInfo.setRegisterSubType(StringUtils.isBlank(sjInfoJyhtxx.getRegistrationSubclass())?sjsq.getRegistrationSubclass():sjInfoJyhtxx.getRegistrationSubclass());//登记小类
        transferBizInfo.setTransferReason(StringUtils.isBlank(sjInfoJyhtxx.getRegistrationReason())?sjsq.getRegistrationReason():sjInfoJyhtxx.getRegistrationReason());//转移原因
        //权利人
        List<SJ_Qlr_Gl> buyers = sjInfoJyhtxx.getGlHouseBuyerVoList();
        if(buyers==null || buyers.size()==0){
            throw new ZtgeoBizException("不完备的权利人数据");
        }
        transferBizInfo.setCommonWay(buyers.get(0).getSharedMode());//共有方式
        List<QlrGlMortgator> obligeeInfoVoList = getObligeeInfoVoList(buyers, idType);
        transferBizInfo.setObligeeInfoVoList(obligeeInfoVoList);
        //义务人（追加）
        List<SJ_Qlr_Gl> salers = sjInfoJyhtxx.getGlHouseSellerVoList();
        transferBizInfo.setSalerInfoVoList(getObligorInfoVoList(salers, idType));
        //代理人
        List<SJ_Qlr_Gl> ql_agents = sjInfoJyhtxx.getGlAgentVoList();
        List<SJ_Qlr_Gl> yw_agents = sjInfoJyhtxx.getGlAgentSellerVoList();
        if(ql_agents!=null && ql_agents.size()>0){
            transferBizInfo.setObligeeAgentInfoVoList(getAgentInfoVoList(ql_agents, idType));
        }
        if(yw_agents!=null && yw_agents.size()>0){
            transferBizInfo.setSalerAgentInfoVoList(getAgentInfoVoList(yw_agents, idType));
        }
        return transferBizInfo;
    }

    //抵押权人
    public static List<DyqrGlMortgator> getMortgageeInfoVoList(List<SJ_Qlr_Gl> dyqrs, String idType) {
        List<DyqrGlMortgator> mortgageeInfoVoList = new ArrayList<DyqrGlMortgator>();
        for (SJ_Qlr_Gl dyqr : dyqrs) {
            SJ_Qlr_Info person = dyqr.getRelatedPerson();
            DyqrGlMortgator mortgageeInfo = new DyqrGlMortgator();
            mortgageeInfo.setMortgageeName(person.getObligeeName());
            mortgageeInfo.setMortgageeIdType(getIdTypeNumber(person.getObligeeDocumentType(), idType));
            mortgageeInfo.setMortgageeId(person.getObligeeDocumentNumber());
            mortgageeInfo.setPhone(person.getDh());
            mortgageeInfo.setOrder(dyqr.getObligeeOrder()!=null?Integer.toString(dyqr.getObligeeOrder()):null);
            mortgageeInfo.setAddress(person.getDz());
            mortgageeInfoVoList.add(mortgageeInfo);
        }
        return mortgageeInfoVoList;
    }

    //抵押人
    public static List<DyrGlMortgator> getMortgagorInfoVoList(List<SJ_Qlr_Gl> dyrs, String idType) {
        List<DyrGlMortgator> mortgagorInfoVoList = new ArrayList<DyrGlMortgator>();
        for (SJ_Qlr_Gl dyr : dyrs) {
            SJ_Qlr_Info person = dyr.getRelatedPerson();
            DyrGlMortgator mortgagorInfo = new DyrGlMortgator();
            mortgagorInfo.setMortgagorId(person.getObligeeDocumentNumber());
            mortgagorInfo.setMortgagorIdType(getIdTypeNumber(person.getObligeeDocumentType(), idType));
            mortgagorInfo.setMortgagorName(person.getObligeeName());
            mortgagorInfo.setOrder(dyr.getObligeeOrder()!=null?Integer.toString(dyr.getObligeeOrder()):null);
            mortgagorInfo.setAddress(person.getDz());
            mortgagorInfo.setPhone(person.getDh());
            mortgagorInfoVoList.add(mortgagorInfo);
        }
        return mortgagorInfoVoList;
    }

    //抵押的不动产信息
    public static List<RealEstateInfo> getRealEstateInfoVoListByJyht(Sj_Info_Jyhtxx jyht, String idType) {
        List<RealEstateInfo> realEstateInfoVoList = new ArrayList<RealEstateInfo>();
        RealEstateInfo realEstateInfo = new RealEstateInfo();
        List<SJ_Qlr_Gl> buyers = jyht.getGlHouseBuyerVoList();
        List<SJ_Qlr_Gl> sellers = jyht.getGlHouseSellerVoList();
        List<SJ_Bdc_Gl> bdc_gls = jyht.getGlImmovableVoList();
        realEstateInfo.setObligeeInfoVoList(getObligeeInfoVoList(buyers, idType));
        realEstateInfo.setSalerInfoVoList(getSalerInfoVoList(sellers, idType));
        realEstateInfo.setRealEstateUnitInfoVoList(getRealEstateUnitInfoVoList(bdc_gls));
        realEstateInfoVoList.add(realEstateInfo);
        return realEstateInfoVoList;
    }

    //权利人信息
    public static List<QlrGlMortgator> getObligeeInfoVoList(List<SJ_Qlr_Gl> buyers, String idType) {
        List<QlrGlMortgator> obligeeInfoVoList = new ArrayList<QlrGlMortgator>();
        for (SJ_Qlr_Gl buyer : buyers) {
            SJ_Qlr_Info person = buyer.getRelatedPerson();
            QlrGlMortgator obligeeInfo = new QlrGlMortgator();
            obligeeInfo.setObligeeName(person.getObligeeName());
            obligeeInfo.setObligeeIdType(getIdTypeNumber(person.getObligeeDocumentType(), idType));
            obligeeInfo.setObligeeId(person.getObligeeDocumentNumber());
            obligeeInfo.setCommonWay(buyer.getSharedMode());
            obligeeInfo.setSharedSharel(buyer.getSharedValue() == null ? null : buyer.getSharedValue().toString());
            obligeeInfo.setOrder(buyer.getObligeeOrder()!=null?Integer.toString(buyer.getObligeeOrder()):null);
            obligeeInfo.setPhone(buyer.getRelatedPerson().getDh());
            obligeeInfo.setAddress(buyer.getRelatedPerson().getDz());
            obligeeInfoVoList.add(obligeeInfo);
        }
        return obligeeInfoVoList;
    }

    //义务人信息
    public static List<BDCSalerInfo> getObligorInfoVoList(List<SJ_Qlr_Gl> salers,String idType){
        List<BDCSalerInfo> obligorInfoVoList = new ArrayList<BDCSalerInfo>();
        if(salers!=null && salers.size()>0){
            for (SJ_Qlr_Gl saler:salers){
                BDCSalerInfo obligorInfo = new BDCSalerInfo();
                SJ_Qlr_Info person = saler.getRelatedPerson();
                obligorInfo.setSalerId(person.getObligeeDocumentNumber());
                obligorInfo.setSalerIdType(getIdTypeNumber(person.getObligeeDocumentType(), idType));
                obligorInfo.setSalerName(person.getObligeeName());
                obligorInfo.setAddress(person.getDz());
                obligorInfo.setPhone(person.getDh());
                obligorInfo.setOrder(saler.getObligeeOrder()!=null?Integer.toString(saler.getObligeeOrder()):null);
                obligorInfoVoList.add(obligorInfo);
            }
        }
        return obligorInfoVoList;
    }

    //代理人信息
    public static List<WtdlrGlMortgator> getAgentInfoVoList(List<SJ_Qlr_Gl> agents,String idType){
        List<WtdlrGlMortgator> agentInfoVoList = new ArrayList<WtdlrGlMortgator>();
        for (SJ_Qlr_Gl agent : agents) {
            SJ_Qlr_Info person = agent.getRelatedPerson();
            WtdlrGlMortgator agentInfo = new WtdlrGlMortgator();
            agentInfo.setAgentName(person.getObligeeName());
            agentInfo.setAgentId(person.getObligeeDocumentNumber());
            agentInfo.setAgentIdType(getIdTypeNumber(person.getObligeeDocumentType(), idType));
            agentInfo.setOrder(agent.getObligeeOrder()!=null?Integer.toString(agent.getObligeeOrder()):null);
            agentInfo.setPhone(person.getDh());
            agentInfo.setAddress(person.getDz());
            agentInfoVoList.add(agentInfo);
        }
        return agentInfoVoList;
    }

    //义务人信息
    public static List<SalerInfo> getSalerInfoVoList(List<SJ_Qlr_Gl> sellers, String idType) {
        List<SalerInfo> salerInfoVoList = new ArrayList<SalerInfo>();
        for (SJ_Qlr_Gl seller : sellers) {
            SJ_Qlr_Info person = seller.getRelatedPerson();
            System.out.println(JSONObject.toJSONString(person));
            SalerInfo salerInfo = new SalerInfo();
            salerInfo.setSalerName(person.getObligeeName());
            salerInfo.setSalerIdType(getIdTypeNumber(person.getObligeeDocumentType(), idType));
            salerInfo.setSalerId(person.getObligeeDocumentNumber());
            salerInfoVoList.add(salerInfo);
        }
        return salerInfoVoList;
    }

    //不动产单元
    public static List<RealEstateUnitInfo> getRealEstateUnitInfoVoList(List<SJ_Bdc_Gl> bdc_gls) {
        List<RealEstateUnitInfo> realEstateUnitInfoVoList = new ArrayList<RealEstateUnitInfo>();
        for (SJ_Bdc_Gl bdc_gl : bdc_gls) {
            RealEstateUnitInfo realEstateUnitInfo = new RealEstateUnitInfo();
            switch (bdc_gl.getImmovableType()) {
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

    public static String getIdTypeNumber(String idName, String idTypess) {
        if (StringUtils.isBlank(idName)) {
            return "1";//或者抛异常
        }
        String idd = "99";
        String[] idTypes = idTypess.split("\\$");
        System.err.println(idTypes);
        for (int i = 0; i < idTypes.length; i++) {
            String idType = idTypes[i];
            String[] id_ = idType.split(",");
            String idTypeName = id_[0];
            if (idType.contains(idName)) {
                System.out.println("为：" + id_[1]);
                idd = id_[1];
            }
        }
        return idd;
    }

    public static int getFileXh(SJ_Fjfile file, List<SJ_Fjfile> fileVoList) {
        int i = 0;
        for (SJ_Fjfile fjfile : fileVoList) {
            if (fjfile.getLogicPath().equals(file.getLogicPath()))
                i++;
            if (fjfile.getFileId().equals(file.getFileId())) {
                return i;
            }
        }
        return i;
    }

    public static TaxParamBody dealParamForTax(SJ_Sjsq sjsq) throws ParseException {
        TaxParamBody taxParam = new TaxParamBody();
        List<SJ_Info_Bdcqlxgxx> bdcqls =  sjsq.getImmovableRightInfoVoList();

        Sj_Info_Jyhtxx jyht = sjsq.getTransactionContractInfo();
        String CFZT = "0";
        String YYZT = "0";
        String DYZT = "0";
        //权属信息
        QSXX qsxx = new QSXX();
        List<FWXX> FWXX = new ArrayList<FWXX>();
        for(SJ_Info_Bdcqlxgxx bdcql:bdcqls) {
            List<SJ_Its_Right> itsRightVoList = bdcql.getItsRightVoList();
            if (itsRightVoList != null) {
                for (SJ_Its_Right itsRight : itsRightVoList) {
                    switch (itsRight.getItsRightType()) {
                        case "抵押":
                            DYZT = "1";
                            break;
                        case "查封":
                            CFZT = "1";
                            break;
                        case "异议":
                            YYZT = "1";
                            break;
                    }
                }
            }

            if (
                    StringUtils.isNotBlank(bdcql.getDataType())
                    && "主设施".equals(bdcql.getDataType())
                    && !bdcql.getCertificateType().equals("土地证")
            ) {
                String bdczh = bdcql.getImmovableCertificateNo();
                if(bdczh.contains("-")) {
                    bdczh = bdczh.substring(0,bdczh.lastIndexOf("_"))+"号";
                }
                qsxx.setBDCZH(bdczh);
                qsxx.setJZMJ(bdcql.getArchitecturalArea());
                qsxx.setTNMJ(bdcql.getHouseArchitecturalArea());
                qsxx.setYT(bdcql.getHousePlanningPurpose());
                qsxx.setZL(bdcql.getImmovableSite());
                qsxx.setTDSYQR(bdcql.getLandUseRightOwner());
                qsxx.setTDHQFS(bdcql.getLandObtainWay());
                List<SJ_Bdc_Gl> bdcgls = bdcql.getGlImmovableVoList();
                if (bdcgls != null) {
                    for (SJ_Bdc_Gl bdcgl : bdcgls) {
                        if (bdcgl.getImmovableType().equals("房地")) {
                            FWXX.add(getFwxx(bdcql,bdcgl.getFwInfo()));
                        }
                    }
                }
            } else if(!bdcql.getCertificateType().equals("土地证")){//添加附属设施到FWXX
                List<SJ_Bdc_Gl> bdcgls = bdcql.getGlImmovableVoList();
                if (bdcgls != null) {
                    for (SJ_Bdc_Gl bdcgl : bdcgls) {
                        if (bdcgl.getImmovableType().equals("房地")) {
                            SJ_Bdc_Fw_Info fw = bdcgl.getFwInfo();
                            FWXX.add(getFwxx(bdcql,fw));
                        }
                    }
                }
            }
        }
        qsxx.setFWXX(FWXX);

        //买卖方
        List<SJ_Qlr_Gl> buyergls = jyht.getGlHouseBuyerVoList();
        List<SJ_Qlr_Gl> sellergls = jyht.getGlHouseSellerVoList();
        List<JYQLRXX> JYQLRXX = new ArrayList<JYQLRXX>();
        for(SJ_Qlr_Gl buyergl:buyergls){
            JYQLRXX jyqlrxx = getJyqlr(buyergl);
            jyqlrxx.setQLRBS("1");
            JYQLRXX.add(jyqlrxx);
        }
        for(SJ_Qlr_Gl sellergl:sellergls){
            JYQLRXX jyqlrxx = getJyqlr(sellergl);
            jyqlrxx.setQLRBS("0");
            JYQLRXX.add(jyqlrxx);
        }

        List<SJ_Qlr_Gl> buyerAgentgls = jyht.getGlAgentVoList();
        List<SJ_Qlr_Gl> sellerAgentgls = jyht.getGlAgentSellerVoList();
        List<JYQLRXX> JYDLRXX = new ArrayList<JYQLRXX>();
        if(buyerAgentgls!=null){//权利代理人整理
            for(SJ_Qlr_Gl buyerAgentgl:buyerAgentgls) {
                JYQLRXX ydlrxx = getJyqlr(buyerAgentgl);
                ydlrxx.setQLRBS("1");
                JYDLRXX.add(ydlrxx);
            }
        }
        if(sellerAgentgls!=null){//义务代理人整理
            for(SJ_Qlr_Gl sellerAgentgl:sellerAgentgls) {
                JYQLRXX jdlrxx = getJyqlr(sellerAgentgl);
                jdlrxx.setQLRBS("0");
                JYDLRXX.add(jdlrxx);
            }
        }

        DecimalFormat df = new DecimalFormat("#.00");
        SJ_Jyht_Detail htDetail = jyht.getHtDetail();
        HTXX HTXX = new HTXX();
        HTXX.setHTJE(jyht.getContractAmount()!=null? Double.parseDouble(df.format(jyht.getContractAmount())):null);//合同金额
        HTXX.setFSSS(StringUtils.isNotBlank(htDetail.getHouseProperties())?htDetail.getHouseProperties().replaceAll("FJ,",""):htDetail.getHouseProperties());//附属设施
        HTXX.setSFCZ(StringUtils.isNotBlank(htDetail.getIsHire())?Integer.parseInt(htDetail.getIsHire()):null);//是否出租
        HTXX.setCZSM(htDetail.getHireInstructions());//出租说明
        HTXX.setSFBHFS(StringUtils.isNotBlank(htDetail.getDoesIncludeHouseProperties())?Integer.parseInt(htDetail.getDoesIncludeHouseProperties()):null);//是否包含附属设施 1 包含，0 不包含
        HTXX.setSFTG(StringUtils.isNotBlank(jyht.getFundTrusteeship())?Integer.parseInt(jyht.getFundTrusteeship()):null);//是否资金托管 1 是，0 否
        HTXX.setTGKHH(htDetail.getFundOpenBank());//资金托管开户行
        HTXX.setTGZH(htDetail.getFundAccount());//托管账户
        HTXX.setTGMFZFNR(htDetail.getFundBuyerPaysContent());//买方支付内容
        HTXX.setZFFS(StringUtils.isNotBlank(jyht.getPaymentMethod())?Integer.parseInt(jyht.getPaymentMethod()):null);//支付方式 1 一次性付款，2 分期付款，3 贷款付款，4 其它付款方式
        HTXX.setFKRQ(htDetail.getFullPaymentDate());//付款日期（付款方式1）
        HTXX.setFQFKRQ1(htDetail.getStagePaymentDate1());//分期付款日期1（付款方式2）
        HTXX.setFQFKJE1(htDetail.getStagePaymentAmount1()!=null?Double.parseDouble(df.format(htDetail.getStagePaymentAmount1())):null);//分期付款金额1（付款方式2）//保留两位小数
        HTXX.setFQFKRQ2(htDetail.getStagePaymentDate2());//分期付款日期2（付款方式2）
        HTXX.setFQFKJE2(htDetail.getStagePaymentAmount2()!=null?Double.parseDouble(df.format(htDetail.getStagePaymentAmount2())):null);//分期付款金额2（付款方式2）
        HTXX.setFQFKRQ3(htDetail.getStagePaymentDate3());//分期付款日期3（付款方式2）
        HTXX.setFQFKJE3(htDetail.getStagePaymentAmount3()!=null?Double.parseDouble(df.format(htDetail.getStagePaymentAmount3())):null);//分期付款金额3（付款方式2）
        HTXX.setDKFS(StringUtils.isNotBlank(htDetail.getLoanMode())?Integer.parseInt(htDetail.getLoanMode()):null); //贷款方式，1 银行按揭，2 公积金贷款（付款方式3）
        HTXX.setSFKRQ(htDetail.getFirstPaymentDate());//首付款日期（付款方式3）
        HTXX.setSFKJE(htDetail.getFirstPaymentAmount()!=null?Double.parseDouble(df.format(htDetail.getFirstPaymentAmount())):null); //首付款金额（付款方式3）//保留两位小数
        HTXX.setDKSQRQ(htDetail.getLoanApplyDate()); //贷款申请日期（付款方式3）
        HTXX.setQTFKNR(htDetail.getPaymentContents()); //其它付款内容（付款方式4）
        HTXX.setQTFKMFZF(htDetail.getBuyerPays()); //其它付款-买方支付（付款方式4）
        if(StringUtils.isNotBlank(jyht.getTaxBurdenParty())){
            if(jyht.getTaxBurdenParty().equals("1")){
                HTXX.setJFCDSF(1); //买方承担税费情况，1 独自承担（此时YFCDSF为空），2 承担各自部分
            }else if(jyht.getTaxBurdenParty().equals("2")){
                HTXX.setYFCDSF(1); //卖方承担税费情况，1 独自承担（此时JFCDSF为空），2 承担各自部分
            }else if(jyht.getTaxBurdenParty().equals("3")){
                HTXX.setJFCDSF(2); //买方承担税费情况，1 独自承担（此时YFCDSF为空），2 承担各自部分
                HTXX.setYFCDSF(2); //卖方承担税费情况，1 独自承担（此时JFCDSF为空），2 承担各自部分
            }else{
                throw new ZtgeoBizException("不支持的税费承担方式");
            }
        }
        HTXX.setJFTS(StringUtils.isNotBlank(jyht.getDeliveryDays())?Integer.parseInt(jyht.getDeliveryDays()):null); //交付天数
        HTXX.setJFRQ(jyht.getDeliveryDate());  //交付日期

        YCSLXX YCSLXX = new YCSLXX();
        YCSLXX.setSLBH(sjsq.getReceiptNumber());
        YCSLXX.setSQSJ(sjsq.getReceiptTime());

        taxParam.setCFZT(CFZT);
        taxParam.setDYZT(DYZT);
        taxParam.setYYZT(YYZT);
        taxParam.setHTXX(HTXX);
        taxParam.setYCSLXX(YCSLXX);
        taxParam.setQSXX(qsxx);
        taxParam.setJYQLRXX(JYQLRXX);
        taxParam.setJYDLRXX(JYDLRXX);
        return taxParam;
    }
    public static TraParamBody dealParamForTra(SJ_Sjsq sjsq) throws ParseException {
        TaxParamBody taxParam = dealParamForTax(sjsq);
        TraParamBody traParam = new TraParamBody();
        traParam.setCFZT(taxParam.getCFZT());
        traParam.setDYZT(taxParam.getDYZT());
        traParam.setHTXX(taxParam.getHTXX());
        traParam.setJYQLRXX(taxParam.getJYQLRXX());
        traParam.setJYDLRXX(taxParam.getJYDLRXX());
        traParam.setQSXX(taxParam.getQSXX());
        traParam.setYCSLXX(taxParam.getYCSLXX());
        traParam.setYYZT(taxParam.getYYZT());
        return traParam;
    }

    public static JYQLRXX getJyqlr(SJ_Qlr_Gl qlrgl){
        JYQLRXX jyqlrxx = new JYQLRXX();
        SJ_Qlr_Info qlrInfo = qlrgl.getRelatedPerson();
        jyqlrxx.setQLRMC(qlrInfo.getObligeeName());
        jyqlrxx.setZJLX(qlrInfo.getObligeeDocumentType());
        jyqlrxx.setZJHM(qlrInfo.getObligeeDocumentNumber());
        jyqlrxx.setDH(qlrInfo.getDh());
        jyqlrxx.setGYFS(qlrgl.getSharedMode());
        jyqlrxx.setGYFE(qlrgl.getSharedValue()!=null?qlrgl.getSharedValue():null);
        return jyqlrxx;
    }

    public static FWXX getFwxx(SJ_Info_Bdcqlxgxx bdcql,SJ_Bdc_Fw_Info fw){
        FWXX fwxx = new FWXX();
        fwxx.setBDCDYH(fw.getImmovableUnitNumber());
        fwxx.setFTMJ(fw.getApportionmentArchitecturalArea());
        fwxx.setFWDY(fw.getUnitMark());
        fwxx.setFWFH(fw.getRoomMark());
        fwxx.setFWJG(fw.getHouseStructure());
        fwxx.setFWLX(StringUtils.isBlank(fw.getHouseType()) ? bdcql.getHouseType() : fw.getHouseType());
        fwxx.setFWXZ(StringUtils.isBlank(fw.getHouseNature()) ? bdcql.getHouseNature() : fw.getHouseNature());
        fwxx.setFWZL(fw.getHouseLocation());
        fwxx.setJZMJ(fw.getArchitecturalArea());
        fwxx.setTNMJ(fw.getHouseArchitecturalArea());
        fwxx.setXMMC(fw.getProjectName());
        fwxx.setYFCBH(fw.getOldHouseCode());
        fwxx.setYT(fw.getImmovablePlanningUse());
        fwxx.setZL(fw.getHouseLocation());
        fwxx.setZCS(fw.getTotalStorey());
        fwxx.setSZC(fw.getLocationStorey());
        return fwxx;
    }

    public static JSONObject dealJSONForSB(Object body){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen,
                                  SerializerProvider serializers) throws IOException,
                    JsonProcessingException {
                gen.writeString("");
            }
        });
        JSONObject jsonObj = null;
        try {
            String dealJsonStr = objectMapper.writeValueAsString(body);
            System.out.println("处理进行中。。。"+dealJsonStr);
            jsonObj = JSONObject.parseObject(dealJsonStr);
//            jsonObj = JSONObject.parseObject(new String(dealJsonStr.getBytes(),"UTF-8"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ZtgeoBizException("json数据处理异常");
        }
//        catch (UnsupportedEncodingException e){
//            e.printStackTrace();
//            throw new ZtgeoBizException("字符转码异常");
//        }
        return jsonObj;
    }

    public static List<FJXX> convertFiles(List<ImmovableFile> files){
        List<FJXX> fjxxs = new ArrayList<FJXX>();
        if(files!=null){
            for(ImmovableFile file:files){
                log.info("进入"+file.getFileName()+"的文件处理");
                FJXX fjxx = new FJXX();
                fjxx.setFJMC(file.getFileName());
                fjxx.setFJDX(file.getFileSize());
                fjxx.setFJFL(file.getpName());
                fjxx.setFJKZM(file.getFileType());
                fjxx.setFTPDZ(convertStr(file.getFileAddress()));
                fjxx.setXH(file.getFileSequence());
                fjxxs.add(fjxx);
            }
        }
        return fjxxs;
    }

    public static String convertStr(String fileAddress){
        log.info("传入的字符串："+fileAddress);
        if(fileAddress.contains("\\")) {
            fileAddress = fileAddress.replaceAll("\\\\","/");
        }
        log.info("处理后的字符串："+fileAddress);
        return fileAddress;
    }

    /**
     * 描述：处理出电力需要的实体类
     * 作者：chenb
     * 日期：2020/6/12/012
     * 参数：
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
    */
    public static DLReqEntity dealParamForEle(SJ_Sjsq sjsq){
        DLReqEntity dlcs = new DLReqEntity();
        dlcs.assignOrg(sjsq.getSdqgxx());
        dealParamForSDQ(sjsq,dlcs);
        return dlcs;
    }

    /**
     * 描述：处理出水务需要的实体类
     * 作者：chenb
     * 日期：2020/6/12/012
     * 参数：
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
    */
    public static ZLSReqEntity dealParamForWat(SJ_Sjsq sjsq){
        ZLSReqEntity dlcs = new ZLSReqEntity();
        dlcs.assignOrg(sjsq.getSdqgxx());
        dealParamForSDQ(sjsq,dlcs);
        return dlcs;
    }

    /**
     * 描述：处理出天然气需要的实体类
     * 作者：chenb
     * 日期：2020/6/12/012
     * 参数：
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
    */
    public static TRQReqEntity dealParamForGas(SJ_Sjsq sjsq){
        TRQReqEntity dlcs = new TRQReqEntity();
        dlcs.assignOrg(sjsq.getSdqgxx());
        dealParamForSDQ(sjsq,dlcs);
        return dlcs;
    }

    /**
     * 描述：处理出水电气基础实体类数据
     * 作者：chenb
     * 日期：2020/6/12/012
     * 参数：
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
    */
    public static SDQReqEntity dealParamForSDQ(SJ_Sjsq sjsq,SDQReqEntity dlcs){
        List<SJ_Info_Bdcqlxgxx> bdcqls = sjsq.getImmovableRightInfoVoList();
        if(bdcqls!=null && bdcqls.size()>0) {
            SJ_Info_Bdcqlxgxx bdcql_book = null;
            for(SJ_Info_Bdcqlxgxx bdcql:bdcqls){
                if(bdcql.getServiceCode().equals(Msgagger.BDCQZSDZF_SERVICE_CODE)){
                    bdcql_book = bdcql;
                    break;
                }
            }
            if(bdcql_book==null){
                throw new ZtgeoBizException("一窗业务登簿时缺失必要服务（不动产登簿）数据");
            }
            dlcs.getBaseFromBdcql(bdcql_book);
        }else{
            throw new ZtgeoBizException("水电气数据分发前请先登簿");
        }
        Sj_Info_Jyhtxx jyhtxx = sjsq.getTransactionContractInfo();
        dlcs.replenishFromJyxx(sjsq.getReceiptNumber(),sjsq.getNotifiedPersonName(),sjsq.getNotifiedPersonTelephone(),jyhtxx);
        return dlcs;
    }

    public static SJ_Qlr_Info getNeedPerson(String exampleName,List<SJ_Qlr_Gl> people){
        for(SJ_Qlr_Gl person:people){
            if(person.getObligeeName().equals(exampleName)){
                return person.getRelatedPerson();
            }
        }
        return null;
    }

    public static List<SDQFile> dealFjForSDQ(Sj_Info_Jyhtxx jyhtxx, Map<String , SJ_Fjinst> fjinstMap){
        List<SDQFile> datas = new ArrayList<>();
        List<SJ_Qlr_Gl> sellers = jyhtxx.getGlHouseSellerVoList();
        List<SJ_Qlr_Gl> buyers = jyhtxx.getGlHouseBuyerVoList();
        Set<String> xgrNames = arrangeGuysName(sellers,buyers);
        SJ_Fjinst fjinst = fjinstMap.get(KEYWORD_OF_ANNEX_IDCARD);
        if(fjinst!=null){
            List<SJ_Fjinst> sons = fjinst.getChildren();
            int index_rzjg = 1;//人证结果序号
            for(SJ_Fjinst son : sons){
                String sonName = son.getCname();
                if(son.getCkind().equals(CKIND_OF_ANNEX_FOLDER) && xgrNames.contains(sonName)){//文件夹并且在相关人范围内
                    List<SJ_Fjinst> sonsChildren = son.getChildren();
                    int index_ysjg = 1;//衍生结果序号
                    for(SJ_Fjinst sonsChild:sonsChildren){
                        if(sonsChild.getCkind().equals(CKIND_OF_ANNEX_FILE) && sonsChild.getFile()!=null){
                            if(!sonsChild.getCname().equals(sonName+CARD_NAME_OF_XCTP) && !sonsChild.getCname().equals(sonName+CARD_NAME_OF_ZJZTP)){
                                SDQFile file_this = new SDQFile();
                                if(sonsChild.getCname().equals(sonName+CARD_NAME_OF_RZJG)) {
                                    file_this.setFileName(CARD_NAME_OF_RZJG_RENAME + index_rzjg + "." + sonsChild.getFile().getFileExt());
                                    index_rzjg++;
                                }else {
                                    file_this.setFileName(CARD_NAME_OF_YSJG + index_rzjg+"-"+ index_ysjg + "." + sonsChild.getFile().getFileExt());
                                    index_ysjg++;
                                }
                                file_this.setFileData(sonsChild.getFile().getFileId());
                                datas.add(file_this);
                            }
                        }
                    }
                }
            }
        }
        return datas;
    }

    public static Set<String> arrangeGuysName(List<SJ_Qlr_Gl>... guysList){
        Set<String> guysName = new HashSet<>();
        if(guysList!=null){
            for (List<SJ_Qlr_Gl> guys:guysList){
                for(SJ_Qlr_Gl guy:guys){
                    guysName.add(guy.getObligeeName());
                }
            }
        }
        return guysName;
    }
}
