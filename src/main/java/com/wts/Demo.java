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
  private static String openWindow(CloseableHttpClient client) throws Exception {
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
    if (res.contains(start) && res.contains(end)) {
      return res.substring(res.indexOf(start) + 11, res.indexOf(end));
    } else {
      return "";
    }
  }

  /**
   * 点击查找，获取grbh
   */
  private static String getGrbh(CloseableHttpClient client, String paraValue) throws Exception {
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
//    String start = "tableMark='";
//    String end = "' class=\"datawindowDiv\">";
//    String tableMark = res.substring(res.indexOf(start) + 11, res.indexOf(end));

    String s1 = "init('true','true','[";
    String e1 = "]');</script>";
    String grbh = "";
    if (res.contains(s1) && res.contains(e1)) {
      JSONArray jsStrs = JSONArray.fromObject(res.substring(res.indexOf(s1) + 20, res.indexOf(e1) + 1));
      if (jsStrs.size() > 0) {
        JSONObject jsStr = jsStrs.getJSONObject(0);
        grbh = jsStr.getString("grbh");
      }
    }
    return grbh;
  }


  /**
   * 关闭窗体
   */

  private static void closeWindow(CloseableHttpClient client, String tableMark) throws Exception {
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
   * 获取流水号
   */
  private static String getDjlsh(CloseableHttpClient client, String grxm, String gmsfhm, String grbh, String datawindow) throws Exception {
    URI u = new URIBuilder()
            .setScheme("http")
            .setHost("10.153.50.108:7001")
            .setPath("/lemis3/lemis3SuccorOrgAdmit.do")
            .setParameter("method", "queryAdmitPer")
            .setParameter("_xmlString", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s dwbh=\"\" dwmc=\"\" sftc=\"\" gmsfhm=\"" + gmsfhm + "\" grxm=\"" + grxm + "\" grbh=\"" + grbh + "\" sfbl=\"\" sfyba=\"\" /></p>")
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
    if (res.contains(start) && res.contains(end)) {
      JSONArray jsStrs = JSONArray.fromObject(res.substring(res.indexOf(start) + 20, res.indexOf(end) + 1));
      if (jsStrs.size() > 0) {
        JSONObject jsStr = jsStrs.getJSONObject(0);
        djlsh = jsStr.getString("djlsh");
      }
    }
    return djlsh;
  }

  /**
   * 获取剩余月数
   */
  private static String getSyys(CloseableHttpClient client, String grxm, String gmsfhm, String grbh, String djlsh) throws Exception {
    URI u = new URIBuilder()
            .setScheme("http")
            .setHost("10.153.50.108:7001")
            .setPath("/lemis3/lemis3MeritStation.do")
            .setParameter("method", "enterBonusAddForSinglePer")
            .setParameter("_xmlString", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s djlsh=\"" + djlsh + "\"/><s grbh=\"" + grbh + "\"/><s gmsfhm=\"" + gmsfhm + "\"/><s grxm=\"" + grxm + "\"/><s btrylb=\"01\"/></p>")
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
    if (res.contains(start) && res.contains(end)) {
      return res.substring(res.indexOf(start) + 70, res.indexOf(end));
    } else {
      return "";
    }
  }

  /**
   * 生成补贴
   */
  private static String creatMoney(CloseableHttpClient client, String grxm, String gmsfhm, String grbh, String djlsh, String qsny, String zzny) throws Exception {
    URI u = new URIBuilder()
            .setScheme("http")
            .setHost("10.153.50.108:7001")
            .setPath("/lemis3/lemis3MeritStation.do")
            .setParameter("method", "createBonusAddForSinglePer")
            .setParameter("_xmlString", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s grbh=\"" + grbh + "\" msg=\"\" grxm=\"" + grxm + "\" gmsfhm=\"" + gmsfhm + "\" djlsh=\"" + djlsh + "\" btrylb=\"01\" syys=\"57\" qsny=\"" + qsny + "\" zzny=\"" + zzny + "\" /><d k=\"dw_ylbt\"></d></p>")
            .setParameter("_jbjgqxfw", "undefined")
            .setParameter("_sbjbjg", "undefined")
            .setParameter("_dwqxfw", "undefined")
            .build();
    HttpPost post = new HttpPost(u);
    CloseableHttpResponse response = client.execute(post);
    HttpEntity entity = response.getEntity();
    String res = EntityUtils.toString(entity, "UTF-8");
    if (res.indexOf("月的补贴已录入") > 0) {
      return "[]";
    } else {
      String start = "init('true','true','[";
      String end = "]');</script>";
      return res.substring(res.indexOf(start) + 20, res.indexOf(end) + 1);
    }
  }

  /**
   * 保存补贴
   */
  private static String saveMoney(CloseableHttpClient client, String grxm, String gmsfhm, String grbh, String djlsh, String qsny, String zzny, String syys, String yanglaobz, String yiliaobz, String shiyebz, String gangweibz) throws Exception {
    URI u = new URIBuilder()
            .setScheme("http")
            .setHost("10.153.50.108:7001")
            .setPath("/lemis3/lemis3MeritStation.do")
            .setParameter("method", "saveBonusAddForSinglePer")
            .setParameter("_xmlString", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s grbh=\"" + grbh + "\" msg=\"\" grxm=\"" + grxm + "\" gmsfhm=\"" + gmsfhm + "\" djlsh=\"" + djlsh + "\" btrylb=\"01\" syys=\"" + syys + "\" qsny=\"" + qsny + "\" zzny=\"" + zzny + "\" /><d k=\"dw_xzbt\"><r qsny=\"" + qsny + "\" zzny=\"" + zzny + "\" sfyxyq=\"0\" sfyxffylbt=\"1\" sfyxffyilbt=\"1\" yanglaobz=\"" + yanglaobz + "\" yiliaobz=\"" + yiliaobz + "\" shiyebz=\"" + shiyebz + "\" gangweibz=\"" + gangweibz + "\" /></d></p>")
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
  private static String getA(CloseableHttpClient client, String grxm, String gmsfhm, String qsny, String zzny) throws Exception {

    String datawindow = openWindow(client);
    if (datawindow.equals("")) {
      return "无法打开窗口！";
    }
    String grbh = getGrbh(client, gmsfhm);
    if (grbh.equals("")) {
      return "无法获取个人编号！";
    }
    String djlsh = getDjlsh(client, grxm, gmsfhm, grbh, datawindow);
    if (djlsh.equals("")) {
      return "无法获取登记流水号！";
    }
    String syys = getSyys(client, grxm, gmsfhm, grbh, djlsh);
    if (syys.equals("0")) {
      return "剩余补贴月数为零！";
    }
    JSONArray jsStrs = JSONArray.fromObject(creatMoney(client, grxm, gmsfhm, grbh, djlsh, qsny, zzny));
    String shiyebz = "", yiliaobz = "", yanglaobz = "", gangweibz = "", sfyxyq = "", sfyxffylbt = "", sfyxffyilbt = "";
    if (jsStrs.size() > 0) {
      JSONObject jsStr = jsStrs.getJSONObject(0);
      shiyebz = jsStr.getString("shiyebz");
      yiliaobz = jsStr.getString("yiliaobz");
      yanglaobz = jsStr.getString("yanglaobz");
      gangweibz = jsStr.getString("gangweibz");
      sfyxyq = jsStr.getString("sfyxyq");
      sfyxffylbt = jsStr.getString("sfyxffylbt");
      sfyxffyilbt = jsStr.getString("sfyxffyilbt");
      String res = saveMoney(client, grxm, gmsfhm, grbh, djlsh, qsny, zzny, syys, yanglaobz, yiliaobz, shiyebz, gangweibz);
      return res;
    } else {
      return "无法生成补贴！";
    }


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
    getA(client, "蔡继光", "370103196112290512","201704","201704");
  }
}
