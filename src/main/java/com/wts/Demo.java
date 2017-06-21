package com.wts;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;

public class Demo {

  /**
   * 打开窗口
   */
  private static String DKCK(CloseableHttpClient client) throws Exception {
    URI u = new URIBuilder()
            .setScheme("http")
            .setHost("10.153.50.108:7001")
            .setPath("/lemis3/lemis3SuccorOrgAdmit.do")
            .setParameter("method", "enterOrgAdmitPerManage")
            .setParameter("_jbjgqxfw", "undefined")
            .setParameter("_sbjbjg", "undefined")
            .setParameter("_dwqxfw", "undefined")
            .build();
    HttpPost post = new HttpPost(u);
    CloseableHttpResponse response = client.execute(post);
    HttpEntity entity = response.getEntity();
    String res = EntityUtils.toString(entity, "UTF-8");
    String start = "tableMark='";
    String end = "' oncontextmenu=showMenu(";
    return res.substring(res.indexOf(start) + 11, res.indexOf(end));

  }

  /**
   * 点击查找，获取grbh
   */
  private static String DJCZ(CloseableHttpClient client, String paraValue) throws Exception {
    URI u = new URIBuilder()
            .setScheme("http")
            .setHost("10.153.50.108:7001")
            .setPath("/lemis3/lemis3Lov.do")
            .setParameter("method", "queryBonusPersonInfo")
            .setParameter("containerName", "formAdmitPerQuery")
            .setParameter("_xmlString", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s paraValue=\"" + paraValue + "\"/><s btrylb=\"01\"/></p>")
            .setParameter("_jbjgqxfw", "undefined")
            .setParameter("_sbjbjg", "undefined")
            .setParameter("_dwqxfw", "undefined")
            .build();
    HttpPost post = new HttpPost(u);
    CloseableHttpResponse response = client.execute(post);
    HttpEntity entity = response.getEntity();
    String res = EntityUtils.toString(entity, "UTF-8");
    String start = "tableMark='";
    String end = "' class=\"datawindowDiv\">";
    String tableMark = res.substring(res.indexOf(start) + 11, res.indexOf(end));

    String s1 = "init('true','true','[";
    String e1 = "]');</script>";
    String grbh = "";
    JSONArray jsStrs = JSONArray.fromObject(res.substring(res.indexOf(s1) + 20, res.indexOf(e1) + 1));
    if (jsStrs.size() > 0) {
      JSONObject jsStr = jsStrs.getJSONObject(0);
      grbh = jsStr.getString("grbh");
    }
    return grbh;
  }

  /**
   * 关闭窗体
   */
  private static void GBCT(CloseableHttpClient client, String tableMark) throws Exception {
    URI u = new URIBuilder()
            .setScheme("http")
            .setHost("10.153.50.108:7001")
            .setPath("/lemis3/cleanSession.do")
            .setParameter("tableMark", tableMark)
            .build();
    HttpPost post = new HttpPost(u);
    CloseableHttpResponse response = client.execute(post);
    HttpEntity entity = response.getEntity();
    String res = EntityUtils.toString(entity, "UTF-8");
  }


  /**
   * 获取流水
   */
  private static String HQLS(CloseableHttpClient client, String grxm,String gmsfhm, String grbh, String datawindow) throws Exception {
    URI u = new URIBuilder()
            .setScheme("http")
            .setHost("10.153.50.108:7001")
            .setPath("/lemis3/lemis3SuccorOrgAdmit.do")
            .setParameter("method", "queryAdmitPer")
            .setParameter("_xmlString", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s dwbh=\"\" dwmc=\"\" sftc=\"\" gmsfhm=\"" + gmsfhm + "\" grxm=\""+grxm+"\" grbh=\"" + grbh + "\" sfbl=\"\" sfyba=\"\" /></p>")
            .setParameter("tableMark", datawindow)
            .setParameter("_jbjgqxfw", "undefined")
            .setParameter("_sbjbjg", "undefined")
            .setParameter("_dwqxfw", "undefined")
            .build();
    HttpPost post = new HttpPost(u);
    CloseableHttpResponse response = client.execute(post);
    HttpEntity entity = response.getEntity();
    String res = EntityUtils.toString(entity, "UTF-8");

    String start = "init('true','true','[";
    String end = "]');columninput";
    String djlsh = "";
    JSONArray jsStrs = JSONArray.fromObject(res.substring(res.indexOf(start) + 20, res.indexOf(end) + 1));
    if (jsStrs.size() > 0) {
      JSONObject jsStr = jsStrs.getJSONObject(0);
      djlsh = jsStr.getString("djlsh");
    }
    return djlsh;
  }

  /**
   * 获取剩余补贴月数
   */
  private static String HQSY(CloseableHttpClient client, String grxm,String gmsfhm, String grbh,String djlsh) throws Exception {
    URI u = new URIBuilder()
            .setScheme("http")
            .setHost("10.153.50.108:7001")
            .setPath("/lemis3/lemis3MeritStation.do")
            .setParameter("method", "enterBonusAddForSinglePer")
            .setParameter("_xmlString", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s djlsh=\""+djlsh+"\"/><s grbh=\""+grbh+"\"/><s gmsfhm=\""+gmsfhm+"\"/><s grxm=\""+grxm+"\"/><s btrylb=\"01\"/></p>")
            .setParameter("_jbjgqxfw", "undefined")
            .setParameter("_sbjbjg", "undefined")
            .setParameter("_dwqxfw", "undefined")
            .build();
    HttpPost post = new HttpPost(u);
    CloseableHttpResponse response = client.execute(post);
    HttpEntity entity = response.getEntity();
    String res = EntityUtils.toString(entity, "UTF-8");
    String start = "dataType=\"number\" style=\"text-align:right;display:none;color:\" value=\"";
    String end = "\" defaultZero=\"true\" required=\"false\" /><input type='text' readonly=true  class=\"label\" tabindex=\"-1\" id=\"syys_label\" name=\"syys_label\" style=\"TEXT-ALIGN: right;color: \" title=\"";

    return res.substring(res.indexOf(start) + 70, res.indexOf(end) );
  }

  /**
   * 生成补贴
   */
  private static String SCBT(CloseableHttpClient client, String grxm,String gmsfhm, String grbh, String djlsh, String qsny, String zzny) throws Exception {
    URI u = new URIBuilder()
            .setScheme("http")
            .setHost("10.153.50.108:7001")
            .setPath("/lemis3/lemis3MeritStation.do")
            .setParameter("method", "createBonusAddForSinglePer")
            .setParameter("_xmlString", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s grbh=\""+grbh+"\" msg=\"\" grxm=\""+grxm+"\" gmsfhm=\""+gmsfhm+"\" djlsh=\""+djlsh+"\" btrylb=\"01\" syys=\"57\" qsny=\""+qsny+"\" zzny=\""+zzny+"\" /><d k=\"dw_ylbt\"></d></p>")
            .setParameter("_jbjgqxfw", "undefined")
            .setParameter("_sbjbjg", "undefined")
            .setParameter("_dwqxfw", "undefined")
            .build();
    HttpPost post = new HttpPost(u);
    CloseableHttpResponse response = client.execute(post);
    HttpEntity entity = response.getEntity();
    String res = EntityUtils.toString(entity, "UTF-8");
    if (res.indexOf("月的补贴已录入")>0){
      return "[]";
    }else{
      String start = "init('true','true','[";
      String end = "]');</script>";
      return res.substring(res.indexOf(start) + 20, res.indexOf(end) + 1);
    }
  }

  /**
   * 保存补贴
   */
  private static String BCBT(CloseableHttpClient client, String grxm,String gmsfhm, String grbh, String djlsh,String qsny, String zzny, String syys,String yanglaobz,String yiliaobz,String shiyebz,String gangweibz) throws Exception {
    URI u = new URIBuilder()
            .setScheme("http")
            .setHost("10.153.50.108:7001")
            .setPath("/lemis3/lemis3MeritStation.do")
            .setParameter("method", "saveBonusAddForSinglePer")
            .setParameter("_xmlString",  "<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s grbh=\""+grbh+"\" msg=\"\" grxm=\""+grxm+"\" gmsfhm=\""+gmsfhm+"\" djlsh=\""+djlsh+"\" btrylb=\"01\" syys=\""+syys+"\" qsny=\""+qsny+"\" zzny=\""+zzny+"\" /><d k=\"dw_xzbt\"><r qsny=\""+qsny+"\" zzny=\""+zzny+"\" sfyxyq=\"0\" sfyxffylbt=\"1\" sfyxffyilbt=\"1\" yanglaobz=\""+yanglaobz+"\" yiliaobz=\""+yiliaobz+"\" shiyebz=\""+shiyebz+"\" gangweibz=\""+gangweibz+"\" /></d></p>")
            .setParameter("_jbjgqxfw", "undefined")
            .setParameter("_sbjbjg", "undefined")
            .setParameter("_dwqxfw", "undefined")
            .build();
    HttpPost post = new HttpPost(u);
    CloseableHttpResponse response = client.execute(post);
    HttpEntity entity = response.getEntity();
    String res = EntityUtils.toString(entity, "UTF-8");
    String start = "alert('";
    String end = "');";
    return res.substring(res.indexOf(start) + 7, res.indexOf(end) + 1);
  }

  /**
   * 发起请求
   */
  private static void getA(CloseableHttpClient client, String gmsfhm) throws Exception {
    String datawindow = DKCK(client);
    String grbh = DJCZ(client, gmsfhm);

    String djlsh = HQLS(client, gmsfhm, grbh, datawindow);
    JSONArray jsStrs = JSONArray.fromObject(SCBT(client,gmsfhm,grbh,djlsh,"201704","201704"));
    String shiyebz="",yiliaobz="",yanglaobz="",gangweibz="",sfyxyq="",sfyxffylbt="",sfyxffyilbt="";
    if (jsStrs.size() > 0) {
      JSONObject jsStr = jsStrs.getJSONObject(0);
      shiyebz=jsStr.getString("shiyebz");
      yiliaobz=jsStr.getString("yiliaobz");
      yanglaobz=jsStr.getString("yanglaobz");
      gangweibz=jsStr.getString("gangweibz");
      sfyxyq=jsStr.getString("sfyxyq");
      sfyxffylbt=jsStr.getString("sfyxffylbt");
      sfyxffyilbt=jsStr.getString("sfyxffyilbt");
    }
    System.out.println(jsStrs.toString());
    String res=HQSY(client,djlsh,gmsfhm,grbh);
    System.out.println(res);

  }

  public static void main(String[] args) throws Exception {
    URI u = new URIBuilder()
            .setScheme("http")
            .setHost("10.153.50.108:7001")
            .setPath("/lemis3/logon.do")
            .setParameter("method", "doLogon")
            .setParameter("userid", "hyzt")
            .setParameter("passwd", util.getMD5("7957908"))
            .setParameter("userLogSign", "0")
            .setParameter("passWordLogSign", "0")
            .setParameter("screenHeight", "768")
            .setParameter("screenWidth", "1024")
            .setParameter("mode", "")
            .build();
    HttpPost post = new HttpPost(u);
    // 创建默认的httpClient实例.
    CloseableHttpClient client = HttpClients.createDefault();
    CloseableHttpResponse response = client.execute(post);
    HttpEntity entity = response.getEntity();
    if (entity != null) {
      System.out.println("  ");
      System.out.println("是否成功登录数据库: " + EntityUtils.toString(entity, "UTF-8"));
    }
    Thread.sleep(1000);
    Thread.sleep(1000);
    Thread.sleep(1000);
    System.out.println("登录....");
    getA(client, "370103196112290512","蔡继光");
  }
}
