package com.springboot.component;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.google.common.collect.Maps;
import com.springboot.component.chenbin.HttpCallComponent;
import com.springboot.component.chenbin.file.ToFTPUploadComponent;
import com.springboot.config.DJJUser;
import com.springboot.config.Msgagger;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.EsfSdq;
import com.springboot.mapper.ExceptionRecordMapper;
import com.springboot.popj.ExceptionRecord;
import com.springboot.popj.GetReceiving;
import com.springboot.popj.MortgageService;
import com.springboot.popj.ReturnVo;
import com.springboot.popj.pub_data.RespServiceData;
import com.springboot.popj.pub_data.SJ_File;
import com.springboot.popj.pub_data.SJ_Info_Handle_Result;
import com.springboot.popj.pub_data.SJ_Sjsq;
import com.springboot.popj.register.HttpRequestMethedEnum;
import com.springboot.popj.warrant.RealPropertyCertificate;
import com.springboot.util.HttpClientUtils;
import com.springboot.util.ParamHttpClientUtil;
import com.springboot.util.StrUtil;
import com.springboot.util.chenbin.IDUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.*;

@Component
@Slf4j
public class AnonymousInnerComponent {


    @Autowired
    private HttpCallComponent httpCallComponent;
    @Autowired
    private RealEstateMortgageComponent realEstateMortgageComponent;
    @Value("${httpclient.windowAcceptanceIp}")
    private String windowAcceptanceIp; //一窗受理ip
    @Value("${httpclient.windowAcceptanceSeam}")
    private String windowAcceptanceSeam; //一窗受理接口
    @Value("${httpclient.ip}")
    private String ip;
    @Value("${httpclient.seam}")
    private String seam;
    @Value("${service.bdcbljgtjfw}")
    private String immovableHandleResultService;
    @Value("${machine.ip}")
    private String machineIp;
    @Value("${machine.post}")
    private String machinePost;
    @Autowired
    private ExceptionRecordMapper exceptionRecordMapper;
    @Autowired
    private ToFTPUploadComponent toFTPUploadComponent;
    @Autowired
    private BdcFTPDownloadComponent bdcFTPDownloadComponent;

    @Autowired
    private HttpClientUtils httpClientUtils;


    public void getSendRoom(GetReceiving getReceiving, OutputStream outputStream) throws  IOException {
        ExecutorService executor = Executors.newCachedThreadPool();
        ReturnVo returnVo = new ReturnVo();
        Map<String, String> mapParmeter = new HashMap<>();
        Map<String,Object> modelMap=Maps.newHashMap();
        modelMap.put("modelId",getReceiving.getModelId());
        Map<String, String> mapHeader = new HashMap<>();
        FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
            public String call() throws Exception { //建议抛出异常
                try {
                    System.out.println("执行主线程");
                    com.alibaba.fastjson.JSONObject tokenObject = httpCallComponent.getTokenYcsl(DJJUser.USERNAME, DJJUser.PASSWORD);//获得token
                    String token=getToken(tokenObject,"",getReceiving.getSlbh(),getReceiving.getMessageType(),null);
                    if (token==null){
                        return Msgagger.USER_LOGIN_BAD;
                    }
                    mapHeader.put("Authorization",token);
                    if (getReceiving.getMessageType().equals(Msgagger.ACCPETNOTICE)) {//审核
                        mapParmeter.put("modelId",getReceiving.getModelId());
                        //受理启动一窗受理流程
                        String entry=httpClientUtils.doGet("http://"+windowAcceptanceIp+":"+windowAcceptanceSeam+"/api/biz/RecService/DealRecieveFromOuter6",modelMap,mapHeader);
                        JSONObject entryObject=JSONObject.fromObject(entry);
                        JSONArray entryArray=JSONArray.fromObject(entryObject.get("data"));//获取data数据
                        EsfSdq esfSdq=new EsfSdq();
                        esfSdq.setAttDirList(entryArray);
                        esfSdq.setSlbh(getReceiving.getSlbh());
                        esfSdq.setTransferred(false);
                        JSONObject paramObject=JSONObject.fromObject(esfSdq);//整理参数信息
                        //根据受理编号查询转移信息（水电气）
                        String zyxx=HttpClientUtils.getJsonData(paramObject,"http://"+ip+":"+seam+"/api/services/app/BdcQuery/GetZYInfo4SDQ");
                        com.alibaba.fastjson.JSONObject zyxxObject=(com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.parse(zyxx);                        //整理数据发送到一窗受理
                        ownershipInFormationxx(zyxxObject,mapParmeter,Msgagger.ESFSDQSERVICE_CODE,false,getReceiving.getSlbh());//获取不动产权属信息
                        getProcessingAnnex(zyxxObject,mapParmeter);//附件上传
                        //发送一窗受理进行启动流程
                        String json=preservationRegistryData(mapParmeter,token,"/api/biz/RecService/DealRecieveFromOuter5");
                        JSONObject jsonObject=JSONObject.fromObject(json);
                        if ((Integer)jsonObject.get("status")==200){
                            log.info("存量房水电气流程数据保存成功及流程开启成功");
                        }else {
                            log.error("存量房信息保存失败,流程未开启");
                        }
                    }else if (getReceiving.getMessageType().equals(Msgagger.RESULTNOTICE)){
                        //登簿通知
                        EsfSdq esfSdq=new EsfSdq();
                        esfSdq.setSlbh(getReceiving.getSlbh());
                        esfSdq.setTransferred(true);
                        JSONObject jsonObject=JSONObject.fromObject(esfSdq);
                        //根据受理编号查询转移信息（水电气）
                        String jsonData=HttpClientUtils.getJsonData(jsonObject,"http://"+ip+":"+seam+"/api/services/app/BdcQuery/GetZYInfo4SDQ");
                        com.alibaba.fastjson.JSONObject zyxxObject=(com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.parse(jsonData);
                        ownershipInFormationxx(zyxxObject,mapParmeter,Msgagger.BDCQZSDZF_SERVICE_CODE,true,getReceiving.getSlbh());//获取不动产权属信息
                        mapParmeter.put("registerNumber",getReceiving.getSlbh());
                        String json=preservationRegistryData(mapParmeter,token,"/api/biz/RecService/DealRecieveFromOuter2");
                        JSONObject ycslObject=JSONObject.fromObject(json);
                    }

                } catch (Exception e) {
                    throw new Exception("Callable terminated with Exception!"); // call方法可以抛出异常
                }
                return null;
            }
        });
        executor.execute(future);
        long t = System.currentTimeMillis();
        try {
            returnVo.setCode(200);
            returnVo.setMessage("成功");
            JSONObject object = JSONObject.fromObject(returnVo);
            outputStream.write(object.toString().getBytes());
            outputStream.flush();
            outputStream.close();
            System.out.println(JSONObject.fromObject(returnVo));
            // 创建数据
//            String result = future.get(); //取得结果，同时设置超时执行时间为5秒。
            System.err.println("result is " + JSONObject.fromObject(returnVo) + ", time is " + (System.currentTimeMillis() - t));
            executor.shutdown();
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    /**
     * 整理二手房登簿成功数据
     * @param
     */
    private  void zlSJsqDbSj(List<RespServiceData> respServiceDataList){
        RespServiceData respServiceData=new RespServiceData();
        respServiceData.setServiceCode(Msgagger.DBSERVICECODE);
        SJ_Info_Handle_Result handleResult=new SJ_Info_Handle_Result();
        List<SJ_Info_Handle_Result> handleResultVoList=new ArrayList<>();
        handleResult.setHandleResult("登簿成功");
        handleResult.setHandleText(Msgagger.SUCCESSFUL_DENGBUCG);
        handleResult.setProvideUnit(Msgagger.REGISTRATION);
        handleResult.setDataComeFromMode(Msgagger.SUCCESSFUL_INTERFACE);
        handleResultVoList.add(handleResult);
        respServiceData.setServiceDataInfos(handleResultVoList);
        respServiceDataList.add(respServiceData);
    }




    /**
     * 整理二手房水电气权属信息
     * @param jsonObject
     * @return
     */
    private void  ownershipInFormationxx(com.alibaba.fastjson.JSONObject jsonObject,Map<String,String> stringMap,String serviceCode,boolean flag,String registerNumber){
        //先处理文件上传
        SJ_Sjsq sjSjsq=new SJ_Sjsq();
        sjSjsq.setNotifiedPersonName(jsonObject.getString("contacts"));//通知人姓名
        sjSjsq.setNotifiedPersonTelephone(jsonObject.getString("contactsPhone"));//通知人电话
        sjSjsq.setNotifiedPersonAddress(jsonObject.getString("contactsAdress"));//通知人地址
        sjSjsq.setDistrictCode(jsonObject.getString("businessAreas"));//区县编码
        List<RealPropertyCertificate> realPropertyCertificateList=new ArrayList<>();
        String array=jsonObject.getString("realEstateInfoList");
        com.alibaba.fastjson.JSONArray jsonArray= com.alibaba.fastjson.JSONArray.parseArray(array);
        if (null != jsonArray){
            for (int i=0;i<jsonArray.size();i++) {
                com.alibaba.fastjson.JSONObject jsonObject1=jsonArray.getJSONObject(i);
                RealPropertyCertificate realPropertyCertificate = realEstateMortgageComponent.getRealPropertyCertificatexx(jsonObject1);
                realPropertyCertificateList.add(realPropertyCertificate);
            }
        }
        RespServiceData respServiceData=new RespServiceData();
        respServiceData.setServiceCode(serviceCode);
        respServiceData.setServiceDataInfos(realPropertyCertificateList);
        List<RespServiceData> serviceDatas=new ArrayList<>();
        serviceDatas.add(respServiceData);
        if (flag==true){
            zlSJsqDbSj(serviceDatas);
            sjSjsq.setServiceDatas(serviceDatas);
            JSONArray serviceArray=JSONArray.fromObject(serviceDatas);
            stringMap.put("serviceDatas",serviceArray.toString());
        }else{
            //添加土地证
            sjSjsq.setServiceDatas(serviceDatas);
            sjSjsq.setRegisterNumber(registerNumber);
            JSONObject sjsqObject=JSONObject.fromObject(sjSjsq);
            stringMap.put("SJ_Sjsq",sjsqObject.toString());//收件
        }
    }

    /**
     * 处理
     * @param jsonObject
     * @return
     */
    private void getProcessingAnnex(com.alibaba.fastjson.JSONObject jsonObject, Map<String,String> stringMap){
        com.alibaba.fastjson.JSONArray fileArray=jsonObject.getJSONArray("fileInfoList");
        List<SJ_File> sjFileList=new ArrayList<>();
        if (null != fileArray){
            for (int i=0;i<fileArray.size();i++){
                com.alibaba.fastjson.JSONObject fileObject=fileArray.getJSONObject(i);
                String fileAddress=fileObject.getString("fileAddress");
                String fileType=fileObject.getString("fileType");
                byte[] bytes=bdcFTPDownloadComponent.downFile(StrUtil.getFTPRemotePathByFTPPath(fileAddress), StrUtil.getFTPFileNameByFTPPath(fileAddress),null);//连接一窗受理平台ftp
                Object uploadObject=toFTPUploadComponent.ycslUpload(bytes,StrUtil.getFTPFileNameByFTPPath(fileAddress),fileType);//获取上传路径和名称
                if (uploadObject==null){
                    log.error(Msgagger.FILE_FAIL);
                    throw new ZtgeoBizException(Msgagger.FILE_FAIL);
                }
                Map<String,Object> map=(Map<String,Object>)uploadObject;
                //覆盖原有url  名称
                SJ_File sj_file=new SJ_File();
                sj_file.setFileAddress(map.get("path").toString()+"\\"+map.get("fileName").toString());
                sj_file.setFileName(map.get("fileName").toString());
                sj_file.setFileType(fileType);
                sj_file.setFileSequence(fileObject.getString("fileSequence"));
                sj_file.setpName(fileObject.getString("pName"));
                sjFileList.add(sj_file);
            }
        }
        JSONArray jsonArray=JSONArray.fromObject(sjFileList);
        stringMap.put("fileVoList",jsonArray.toString());
    }




    public void GetReceiving(GetReceiving getReceiving, OutputStream outputStream) throws  IOException{
        ExecutorService executor = Executors.newCachedThreadPool();
        ReturnVo returnVo = new ReturnVo();
        Map<String, String> mapParmeter = new HashMap<>();
        Map<String,Object> map=new HashMap<>();
        FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
            public String call() throws Exception { //建议抛出异常
                try {
                    System.out.println("执行主线程");
                    com.alibaba.fastjson.JSONObject  tokenObject = httpCallComponent.getTokenYcsl(DJJUser.USERNAME, DJJUser.PASSWORD);//获得token
                    String token=getToken(tokenObject,"getSendRoom",getReceiving.getSlbh(),getReceiving.getMessageType(),null);
                    if (token==null){
                          return   Msgagger.USER_LOGIN_BAD;
                    }
                    if (getReceiving.getMessageType().equals(Msgagger.VERIFYNOTICE)){//审核
                        System.out.println("进入审核");
                        map.put("slbh",getReceiving.getSlbh());
                        //发送登记局获取数据整理发送一窗受理
                        String json = httpClientUtils.doGet("http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetVerifyInfo",map,null);
                        System.out.println(json);
                        JSONObject jsonObject=JSONObject.fromObject(json);
                        JSONArray verfyInfoArray=jsonObject.getJSONArray("verifyInfoVoList");
                        List<SJ_Info_Handle_Result> handleResultVoList=new ArrayList<>();
                        List<RespServiceData> serviceDatas=new ArrayList<>();
                        for (int i=0;i<verfyInfoArray.size();i++){
                            JSONObject verfyInfoObject=verfyInfoArray.getJSONObject(i);
                            SJ_Info_Handle_Result handleResult=new SJ_Info_Handle_Result();
                            handleResult.setHandleText(verfyInfoObject.getString("verifyOpinion"));
                            handleResult.setHandleResult(verfyInfoObject.getString("registerSubType")+ Msgagger.ADOPT);
                            handleResult.setProvideUnit(Msgagger.REGISTRATION);
                            handleResult.setDataComeFromMode(Msgagger.SUCCESSFUL_INTERFACE);
                            handleResultVoList.add(handleResult);
                        }
                        RespServiceData respServiceData=new RespServiceData();
                        respServiceData.setServiceCode(immovableHandleResultService);
                        respServiceData.setServiceDataInfos(handleResultVoList);
                        serviceDatas.add(respServiceData);
                        JSONArray jsonArray=JSONArray.fromObject(serviceDatas);
                        System.out.println("servicedatas"+jsonArray);
                        mapParmeter.put("serviceDatas",jsonArray.toString());
                        mapParmeter.put("registerNumber", jsonObject.getString("slbh"));//受理编号)
                    }else if (getReceiving.getMessageType().equals(Msgagger.RESULTNOTICE)){//登簿审核
                        System.out.println("进入登簿");
                        map.put("slbh",getReceiving.getSlbh());
                        //发送登记局获取数据整理发送一窗受理
                        String json = httpClientUtils.doGet("http://" + ip + ":" + seam + "/api/services/app/BdcQuery/GetCertificateInfo",map,null);
                        JSONObject jsonObject=JSONObject.fromObject(json);
                        mapParmeter.put("immovableSite",jsonObject.getString("sit"));
                        mapParmeter.put("registerNumber",jsonObject.getString("slbh"));
                        //登簿返回信息
                        JSONArray ficateInfoArray=jsonObject.getJSONArray("certificateInfoVoList");
                        List<SJ_Info_Handle_Result> handleResultVoList=new ArrayList<>();
                        List<RespServiceData> respServiceDataList=new ArrayList<>();
                        List<String> stringList=new ArrayList<>();
                        //获取受理编号信息
                        for (int i=0;i<ficateInfoArray.size();i++){
                            JSONObject verfyInfoObject=ficateInfoArray.getJSONObject(i);
                            stringList.add(verfyInfoObject.getString("registerSubType"));
                            RespServiceData RealEstateBookData=new RespServiceData();
                            RespServiceData getRealEstateBooking=getRealEstateBooking(verfyInfoObject.getString("certificateType"),
                                    verfyInfoObject.getString("certificateId"),RealEstateBookData);
                            if (getRealEstateBooking.getServiceCode().equals(Msgagger.DYZMHSERVICE_CODE)){
                                getRealEstateBooking.setServiceCode(Msgagger.DYZXSERVICECODE);
                            }
                            respServiceDataList.add(getRealEstateBooking);//不动产展示登簿信息
                        }
                            RespServiceData respServiceData=new RespServiceData();
                            respServiceData.setServiceCode(Msgagger.DBSERVICECODE);
                            //获取登记小类信息
                            for (String string:stringList) {
                            SJ_Info_Handle_Result handleResult=new SJ_Info_Handle_Result();
                            handleResult.setHandleResult(string+"登簿成功");
                            handleResult.setHandleText(Msgagger.SUCCESSFUL_DENGBUCG);
                            handleResult.setProvideUnit(Msgagger.REGISTRATION);
                            handleResult.setDataComeFromMode(Msgagger.SUCCESSFUL_INTERFACE);
                            handleResultVoList.add(handleResult);
                            respServiceData.setServiceDataInfos(handleResultVoList);
                        }
                        respServiceDataList.add(respServiceData);
                        JSONArray jsonArray=JSONArray.fromObject(respServiceDataList);
                        mapParmeter.put("serviceDatas",jsonArray.toString());
                    }else if (getReceiving.getMessageType().equals(Msgagger.ACCPETNOTICE)){
                        System.out.println("执行受理操作");
                        mapParmeter.put("registerNumber", getReceiving.getSlbh());
                    }
                    System.out.println(token);
                    //返回数据到一窗受理平台保存受理编号和登记编号
                    String resultJson = preservationRegistryData(mapParmeter, token,"/api/biz/RecService/DealRecieveFromOuter2");
                    System.err.println("result is " + resultJson );
                    return resultJson;
                } catch (Exception e) {
                    throw new Exception("Callable terminated with Exception!"); // call方法可以抛出异常
                }
            }
        });
        executor.execute(future);
        long t = System.currentTimeMillis();
//        try {
            returnVo.setCode(200);
            returnVo.setMessage("受理成功");
            JSONObject object=JSONObject.fromObject(returnVo);
            outputStream.write(object.toString().getBytes());
            outputStream.flush();
            outputStream.close();
            System.out.println(JSONObject.fromObject(returnVo));
            // 创建数据
//            String result = future.get(); //取得结果，同时设置超时执行时间为5秒。
            System.err.println("result is " + JSONObject.fromObject(returnVo) + ", time is " + (System.currentTimeMillis() - t));
            executor.shutdown();
    }

    /**
     * 通过类型，证号查询登记局数据
     * @return
     */
    private RespServiceData  getRealEstateBooking(String certificateType,String certificateId,RespServiceData respServiceData)  throws Exception{
        ObjectRestResponse resultRV = new ObjectRestResponse();
      switch (certificateType){
          case  "DYZMH":
               resultRV=realEstateMortgageComponent.getRealEstateMortgage(certificateId,null,true);
               List<MortgageService> mortgageServiceList=(List<MortgageService>) resultRV.getData();
               respServiceData.setServiceCode(Msgagger.DYZMHSERVICE_CODE);
               respServiceData.setServiceDataInfos(mortgageServiceList);
              break;
          case "YGZMH":
              resultRV=realEstateMortgageComponent.getMortgageCancellation(certificateId);
              List<RealPropertyCertificate> realPropertyCertificateList=(List<RealPropertyCertificate>) resultRV.getData();
              respServiceData.setServiceCode(Msgagger.YGZMSERVICE_CODE);
              respServiceData.setServiceDataInfos(realPropertyCertificateList);
              break;
      }
      return respServiceData;
    }

    public String getToken(com.alibaba.fastjson.JSONObject jsonObject,String method,String registerNumber,String noticeType,String receiptNumber ){
        Integer status=jsonObject.getInteger("status");
        if (status!=200){
            ExceptionRecord exceptionRecord=new ExceptionRecord();
            exceptionRecord.setId(IDUtil.getExceptionId());//主键id
            exceptionRecord.setRegisterNumber(registerNumber);
            exceptionRecord.setReceiptNumber(receiptNumber);
            exceptionRecord.setNoticeType(noticeType);
            exceptionRecord.setHappenTime(new Date());
            exceptionRecord.setNoticeText(jsonObject.toString());
            exceptionRecord.setHandleStatus(Msgagger.TO_BE_PROCESSED);
            exceptionRecord.setNoticeUrl("http://"+machineIp+":"+machinePost+"/"+method);
            exceptionRecordMapper.insertSelective(exceptionRecord);
            //记录失败信息
            log.error("用户名或密码错误,找不到对应用户");
            return null;
        }
        String data=jsonObject.getString("data");
        return data;
    }













    public String getRegistrationSubcategory(String registrationName){
        String serviceCode="";
        //判断模糊小类输出serviceCode
        if (registrationName.equals("抵押权注销登记")){
            serviceCode=Msgagger.DBSERVICECODE;
        }else if (registrationName.equals("预告证明号登记")){
            serviceCode="ImmovableElectronicCertificate";
        }else if (registrationName.equals("")){
            serviceCode="";
        }else if (registrationName.equals("")){
            serviceCode="";
        }
        return  serviceCode;
    }




    private String preservationRegistryData(Map<String,String> map,String token,String url){
        Map<String,String> header = new HashMap<String,String>();
        header.put("Authorization",token);
        String json = ParamHttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost,
                "application/json",
                "http://" + windowAcceptanceIp + ":" + windowAcceptanceSeam + url,
                map,header);
        com.alibaba.fastjson.JSONObject jsonObject=(com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.parse(json);
        System.out.println("chenbin返回信息为："+jsonObject);
        return json;
    }






}
