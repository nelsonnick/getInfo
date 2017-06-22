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
import org.dom4j.Element;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
   * 查找人员第一页
   * 返回值为总页数。页面上是每页16条记录，但API返回的是每页12条记录，所以总页面有出入
   */
  private static String getTotal(CloseableHttpClient client, String tableMark) throws Exception {
    URI u = new URIBuilder()
            .setScheme("http")
            .setHost("10.153.50.108:7001")
            .setPath("/lemis3/lemis3SuccorOrgAdmit.do")
            .setParameter("method", "queryAdmitPer")
            .setParameter("_xmlString", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s dwbh=\"\" dwmc=\"\" sftc=\"0\" gmsfhm=\"\" grxm=\"\" grbh=\"\" sfbl=\"\" sfyba=\"\" /></p>")
            .setParameter("tableMark", tableMark)
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
    String pageStart = "记录&nbsp;&nbsp;&nbsp;&nbsp;1/";
    String pageEnd = "页</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    //return res.substring(res.indexOf(start) + 20, res.indexOf(end)) +":"+ res.substring(res.indexOf(pageStart) + 28, res.indexOf(pageEnd));
    return res.substring(res.indexOf(pageStart) + 28, res.indexOf(pageEnd));

  }

  /**
   * 查找人员第N页
   */
  private static String getPersonPage(CloseableHttpClient client, String tableMark, String nextPage) throws Exception {
    URI u = new URIBuilder()
            .setScheme("http")
            .setHost("10.153.50.108:7001")
            .setPath("/lemis3/pilot.do")
            .setParameter("method", "view")
            .setParameter("nextPage", nextPage)
            .setParameter("tableMark", tableMark)
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
    return res.substring(res.indexOf(start) + 21, res.indexOf(end));
  }

  /**
   * 合并人员
   */
  private static String mergePerson(CloseableHttpClient client, String tableMark, Integer page) throws Exception {
    String str = "";
    for (int i = 1; i < page + 1; i++) {
      str = str + getPersonPage(client, tableMark, i + "") + ",";
    }
    return "[" + str.substring(0, str.length() - 1) + "]";
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
   * 获取登记流水号
   */
  private static String getDjlsh(CloseableHttpClient client, String gmsfhm, String grbh, String datawindow) throws Exception {
    String grxm = "";
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
  private static String getSyys(CloseableHttpClient client, String gmsfhm, String grbh, String djlsh) throws Exception {
    String grxm = "";
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
  private static String creatMoney(CloseableHttpClient client, String gmsfhm, String grbh, String djlsh, String qsny, String zzny) throws Exception {
    String grxm = "";
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
  private static String saveMoney(CloseableHttpClient client, String gmsfhm, String grbh, String djlsh, String qsny, String zzny, String syys, String yanglaobz, String yiliaobz, String shiyebz, String gangweibz, String sfyxyq, String sfyxffylbt, String sfyxffyilbt) throws Exception {
    String grxm = "";
    URI u = new URIBuilder()
            .setScheme("http")
            .setHost("10.153.50.108:7001")
            .setPath("/lemis3/lemis3MeritStation.do")
            .setParameter("method", "saveBonusAddForSinglePer")
            .setParameter("_xmlString", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s grbh=\"" + grbh + "\" msg=\"\" grxm=\"" + grxm + "\" gmsfhm=\"" + gmsfhm + "\" djlsh=\"" + djlsh + "\" btrylb=\"01\" syys=\"" + syys + "\" qsny=\"" + qsny + "\" zzny=\"" + zzny + "\" /><d k=\"dw_xzbt\"><r qsny=\"" + qsny + "\" zzny=\"" + zzny + "\" sfyxyq=\"" + sfyxyq + "\" sfyxffylbt=\"" + sfyxffylbt + "\" sfyxffyilbt=\"" + sfyxffyilbt + "\" yanglaobz=\"" + yanglaobz + "\" yiliaobz=\"" + yiliaobz + "\" shiyebz=\"" + shiyebz + "\" gangweibz=\"" + gangweibz + "\" /></d></p>")
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
    return res.substring(res.indexOf(start) + 7, res.indexOf(end));
  }

  /**
   * 查找
   */
  private static String goGet(CloseableHttpClient client) throws Exception {
    String datawindow = openWindow(client);
    if (datawindow.equals("")) {
      return "无法打开窗口！";
    }
    Integer total = Integer.parseInt(getTotal(client, datawindow));
    String mergePerson = mergePerson(client, datawindow,total);
    String personNext = getPersonPage(client, datawindow, "1");
    JSONArray jsStrs = JSONArray.fromObject(mergePerson);
    List<PersonQY> personQYs = new ArrayList<PersonQY>();
    if (jsStrs.size() > 0) {
      for (int j=0;j<jsStrs.size()+1;j++){
        JSONObject jsStr = jsStrs.getJSONObject(j);
        PersonQY p=new PersonQY();
        p.setDwmc(jsStr.getString("dwmc"));
        p.setQyrdbh(jsStr.getString("qyrdbh"));
        p.setDwbh(jsStr.getString("dwbh"));
        p.setDjlsh(jsStr.getString("djlsh"));
        p.setWtgyy(jsStr.getString("wtgyy"));
        p.setKnrybh(jsStr.getString("knrybh"));
        p.setTcrq(jsStr.getString("tcrq"));
        p.setSpzt(jsStr.getString("spzt"));
        p.setCjjg(jsStr.getString("cjjg"));
        p.setCjrq(jsStr.getString("cjrq"));
        p.setGmsfhm(jsStr.getString("gmsfhm"));
        p.setXb(jsStr.getString("xb"));
        p.setCjr(jsStr.getString("cjr"));
        p.setSfyba(jsStr.getString("sfyba"));
        p.setGrbh(jsStr.getString("grbh"));
        personQYs.add(p);
      }
    }

    return personNext;
  }


  /**
   * 保存
   */
  private static String goSave(CloseableHttpClient client, String grxm, String gmsfhm, String month) throws Exception {

    String datawindow = openWindow(client);
    if (datawindow.equals("")) {
      return "无法打开窗口！";
    }
    //System.out.println(datawindow);
    String grbh = getGrbh(client, gmsfhm);
    if (grbh.equals("")) {
      return "无法获取个人编号！";
    }
    //System.out.println(grbh);
    String djlsh = getDjlsh(client, gmsfhm, grbh, datawindow);
    if (djlsh.equals("")) {
      return "无法获取登记流水号！";
    }
    //System.out.println(djlsh);
    String syys = getSyys(client, gmsfhm, grbh, djlsh);
    if (syys.equals("0")) {
      return "剩余补贴月数为零！";
    }
    //System.out.println(syys);
    String creat = creatMoney(client, gmsfhm, grbh, djlsh, month, month);
    if (creat.equals("[]")) {
      return month + "的补贴已录入";
    }
    //System.out.println(creat);
    JSONArray jsStrs = JSONArray.fromObject(creat);
    String shiyebz = "", yiliaobz = "", yanglaobz = "", gangweibz = "", sfyxyq = "", sfyxffylbt = "", sfyxffyilbt = "";

    JSONObject jsStr = jsStrs.getJSONObject(0);
    shiyebz = jsStr.getString("shiyebz");
    yiliaobz = jsStr.getString("yiliaobz");
    yanglaobz = jsStr.getString("yanglaobz");
    gangweibz = jsStr.getString("gangweibz");
    sfyxyq = jsStr.getString("sfyxyq");
    sfyxffylbt = jsStr.getString("sfyxffylbt");
    sfyxffyilbt = jsStr.getString("sfyxffyilbt");
    String save = saveMoney(client, gmsfhm, grbh, djlsh, month, month, syys, yanglaobz, yiliaobz, shiyebz, gangweibz, sfyxyq, sfyxffylbt, sfyxffyilbt);
    if (!save.equals("保存成功！")) {
      return save;
    }
    return gmsfhm + grxm + "--" + month + "补贴已保存";
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
    System.out.println("登录....");
    // System.out.println(goSave(client, "蔡继光", "370103196112290512", "201706"));
    System.out.println(goGet(client));
  }
}
