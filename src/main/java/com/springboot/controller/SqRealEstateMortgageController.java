package com.springboot.controller;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.RealEstateMortgageComponent;
import com.springboot.component.SqRealEstateMortgageComponent;
import com.springboot.component.chenbin.file.ToFTPUploadComponent;
import com.springboot.entity.ParamEntity;
import com.springboot.entity.chenbin.personnel.other.bank.business.mortgage.MortgageRegistrationReqVo;
import com.springboot.entity.chenbin.personnel.other.bank.business.revok.RevokeRegistrationReqVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@Api(tags = "宿迁不动产api")
@RestController
@Scope("prototype")
public class SqRealEstateMortgageController {

    @Autowired
    private SqRealEstateMortgageComponent sqRealEstateMortgageComponent;
    @Autowired
    private RealEstateMortgageComponent realEstateMortgageComponent;
    @Autowired
    private ToFTPUploadComponent toFTPUploadComponent;


    @RequestMapping(value = "/sqTransactionContract", method = RequestMethod.POST)
    @ApiOperation("二手房交易合同信息处理")
    public ObjectRestResponse sqTransactionContract(@RequestBody ParamEntity paramEntity)throws IOException {
        return sqRealEstateMortgageComponent.sqTransactionContract(paramEntity);
    }

    @RequestMapping(value = "/spfTransactionContract", method = RequestMethod.POST)
    @ApiOperation("一手房交易合同信息处理")
    public ObjectRestResponse spfTransactionContract(@RequestBody ParamEntity paramEntity)throws IOException {
        return sqRealEstateMortgageComponent.spfTransactionContract(paramEntity);
    }

    @RequestMapping(value = "/realEstateRenewal", method = RequestMethod.POST)
    @ApiOperation("换证登记发送登记局收件")
    public ObjectRestResponse realEstateRenewal(@RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer) throws Exception {
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return  rv.data(realEstateMortgageComponent.realEstateRenewal(commonInterfaceAttributer));
    }

    @RequestMapping(value = "/supplementaryEvidence", method = RequestMethod.POST)
    @ApiOperation("补证登记发送登记局收件")
    public ObjectRestResponse supplementaryEvidence(@RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer) throws Exception {
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return  rv.data(realEstateMortgageComponent.supplementaryEvidence(commonInterfaceAttributer));
    }



    @RequestMapping(value = "/sendTransferRegister", method = RequestMethod.POST)
    @ApiOperation("二手房转移登记发送登记局收件")
    public ObjectRestResponse sendTransferRegister(@RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer) throws Exception {
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return  rv.data(realEstateMortgageComponent.sendTransferRegister(commonInterfaceAttributer));
    }

    @RequestMapping(value = "/sendTransferMortgage", method = RequestMethod.POST)
    @ApiOperation("二手房转移登记及抵押发送登记局收件")
    public ObjectRestResponse sendTransferMortgage(@RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer) throws Exception {
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return  rv.data(realEstateMortgageComponent.sendTransferMortgage(commonInterfaceAttributer));
    }

    @RequestMapping(value = "/sqTaxation", method = RequestMethod.POST)
    @ApiOperation("地税信息处理")
    public ObjectRestResponse sqTaxation(@RequestParam("htbh") String htbh) throws Exception{
        return sqRealEstateMortgageComponent.sqTaxation(htbh);
    }

    @RequestMapping(value = "/file", method = RequestMethod.POST)
    @ApiOperation("测试附件")
    public Object sqTaxation(@Param("file") MultipartFile file) throws Exception{
        return toFTPUploadComponent.uploadFile(file);
    }


    @RequestMapping(value = "/sq/jt/sqBdcdydj", method = RequestMethod.POST)
    @ApiOperation("不动产抵押登记,预告及预告抵押登记银行转办接入接口")
    public void sqBdcdydj(@RequestBody MortgageRegistrationReqVo mortgageRegistrationReqVo, OutputStream outputStream){
         sqRealEstateMortgageComponent.sqJgdyjk(mortgageRegistrationReqVo,outputStream);
    }


    @RequestMapping(value = "/sq/jt/sqBdczxdj", method = RequestMethod.POST)
    @ApiOperation("抵押注销登记银行转办接入接口")
    public void sqBdczxdj(@RequestBody RevokeRegistrationReqVo revokeRegistrationRespVo,OutputStream outputStream){
        sqRealEstateMortgageComponent.sqJgdyzx(revokeRegistrationRespVo,outputStream);
    }

    @RequestMapping(value = "/cancellationOfWarrants", method = RequestMethod.POST)
    @ApiOperation("权证注销发送至登记平台办件")
    public ObjectRestResponse cancellationOfWarrants(@RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer) throws Exception {
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return  rv.data(realEstateMortgageComponent.cancellationOfWarrants(commonInterfaceAttributer));
    }

    @RequestMapping(value = "/zyAnalysisProduction", method = RequestMethod.POST)
    @ApiOperation("不动产转移析产发送至登记平台办件")
    public ObjectRestResponse zyAnalysisProduction(@RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer) throws Exception {
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return  rv.data(realEstateMortgageComponent.zyAnalysisProduction(commonInterfaceAttributer));
    }

    @RequestMapping(value = "/transferInheritanceOrBequest", method = RequestMethod.POST)
    @ApiOperation("继承或受遗赠发送至登记平台办件")
    public ObjectRestResponse transferInheritanceOrBequest(@RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer) throws Exception {
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return  rv.data(realEstateMortgageComponent.transferInheritanceOrBequest(commonInterfaceAttributer));
    }

    @RequestMapping(value = "/courtRulingIndividual", method = RequestMethod.POST)
    @ApiOperation("不动产法院裁定发送至登记平台办件")
    public ObjectRestResponse<String> courtRulingIndividual(@RequestParam("commonInterfaceAttributer") String commonInterfaceAttributer) throws Exception {
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return  rv.data(realEstateMortgageComponent.courtRulingIndividual(commonInterfaceAttributer));
    }

}
