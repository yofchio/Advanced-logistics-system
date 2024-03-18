package com.example.financialmanagement.controller;

/**
 * @author YANG FUCHAO
 * @version 1.0
 * @date 2023-07-03 10:20
 */


import com.alibaba.fastjson.JSON;
import com.aliyun.goodstech20191230.models.ClassifyCommodityResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.TeaModel;
import com.example.financialmanagement.beans.HttpResponseEntity;
import com.example.financialmanagement.common.Constans;
import com.example.financialmanagement.entity.CentralStation;
import com.example.financialmanagement.feign.FeignApi;
import com.example.financialmanagement.service.impl.AliyunOss;
import com.example.financialmanagement.service.impl.FinancialServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/financial")
public class AiliyunGoodAction {
  private final Logger logger = LoggerFactory.getLogger(FinancialAction.class);

  @Autowired
  public  FeignApi feignApi;

  public static com.aliyun.goodstech20191230.Client createClient(String accessKeyId,
      String accessKeySecret) throws Exception {
        /*
          初始化配置对象com.aliyun.teaopenapi.models.Config
          Config对象存放AccessKeyId、AccessKeySecret、endpoint等配置
         */
    com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
        .setAccessKeyId(accessKeyId)
        .setAccessKeySecret(accessKeySecret);
    // 访问的域名
    config.endpoint = "goodstech.cn-shanghai.aliyuncs.com";
    return new com.aliyun.goodstech20191230.Client(config);
  }

  @RequestMapping(value = "/goodAili",method = RequestMethod.POST,consumes =
      MediaType.MULTIPART_FORM_DATA_VALUE, headers = "Accept"
      + "=application/json")
  public HttpResponseEntity goodAili(@RequestPart("file") MultipartFile file) {
    HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
    try {
      String url= AliyunOss.uploadFile(file);
      System.out.println("url"+url);
      CentralStation res=getGood(url);
      if(res!=null)
      {
        httpResponseEntity.setData(res);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
      }else
      {
        httpResponseEntity.setCode(Constans.EXIST_CODE);
        httpResponseEntity.setMessage(Constans.Aili_FAIL);
      }
    } catch (Exception e) {
      logger.info("goodAili 阿里云的商品分类>>>>>>>>>>>" + e.getLocalizedMessage());
      httpResponseEntity.setCode(Constans.EXIST_CODE);
      httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
    }
    return httpResponseEntity;
  }

    public  CentralStation getGood( String url) throws Exception {

    // 创建AccessKey ID和AccessKey Secret，请参考https://help.aliyun.com/document_detail/175144.html。
    // 如果您使用的是RAM用户的AccessKey，还需要为子账号授予权限AliyunVIAPIFullAccess，请参考https://help.aliyun.com/document_detail/145025.html。
    // 从环境变量读取配置的AccessKey ID和AccessKey Secret。运行代码示例前必须先配置环境变量。
//    String accessKeyId = System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID");
//    String accessKeySecret = System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET");
      System.out.println(url);
      //和OSS不是同一个
      com.aliyun.goodstech20191230.Client client = AiliyunGoodAction.createClient("", "");
    com.aliyun.goodstech20191230.models.ClassifyCommodityRequest classifyCommodityRequest = new com.aliyun.goodstech20191230.models.ClassifyCommodityRequest()
        .setImageURL(url);
    com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
    try {
      ClassifyCommodityResponse classifyCommodityResponse = client.classifyCommodityWithOptions(classifyCommodityRequest, runtime);
      // 获取整体结果
      System.out.println(com.aliyun.teautil.Common.toJSONString(TeaModel.buildMap(classifyCommodityResponse)));
      // 获取单个字段
      String res=
          com.aliyun.teautil.Common.toJSONString(TeaModel.buildMap(classifyCommodityResponse));
//      String res=classifyCommodityResponse.getBody().toString();
      System.out.println(res);
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(res);

      // Extracting fields from the "data" object
      JsonNode dataNode = rootNode.get("body").get("Data");
      double score = dataNode.get("Categories").get(0).get("Score").asDouble();
      String categoryName = dataNode.get("Categories").get(0).get("CategoryName").asText();
      int statusCode = rootNode.get("statusCode").asInt();

      // Print the extracted values
      System.out.println("Score: " + score);
      System.out.println("CategoryName: " + categoryName);
      System.out.println("statusCode: " + statusCode);

      HttpResponseEntity resgood= feignApi.getAliGoodName(categoryName);
      /*查询并统计结果*/
      String jsonString2 = JSON.toJSONString(resgood.getData());  // 将对象转换成json格式数据

      CentralStation centralStation = JSON.parseObject(jsonString2,CentralStation.class);
      return centralStation;
    } catch (TeaException teaException) {
      // 获取整体报错信息
      System.out.println(com.aliyun.teautil.Common.toJSONString(teaException));
      // 获取单个字段
      System.out.println(teaException.getCode());
    }

    return null;
  }
}
