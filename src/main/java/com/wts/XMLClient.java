package com.wts;

import okhttp3.*;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Iterator;

public class XMLClient {

  public static void main(String[] args) throws Exception {
    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("text/xml;charset=GBK");
    RequestBody body = RequestBody.create(mediaType, getXML(false, "132123197608250060"));
    Request request = new Request.Builder()
            .url("http://10.153.50.123:80/lbs/MainServlet")
            .post(body)
            .addHeader("content-type", "text/xml;charset=GBK")
            .addHeader("accept", "*.*;")
            .addHeader("host", "dareway")
            .addHeader("cache-control", "no-cache")
            .addHeader("postman-token", "71a5c099-7230-b17c-83cd-8a87479aae09")
            .build();

    Response response = client.newCall(request).execute();

    Document document = DocumentHelper.parseText(response.body().string());
    //获取根节点对象
    Element rootElement = document.getRootElement();
    //获取子节点
    Element resultset = rootElement.element("Body").element("business").element("resultset");

    for (Iterator it = resultset.elementIterator(); it.hasNext(); ) {
      Element element = (Element) it.next();
      System.out.println("sblb：" + element.attributeValue("sblb"));
      System.out.println("zdlsh：" + element.attributeValue("zdlsh"));
      System.out.println("qsny：" + element.attributeValue("qsny"));
      System.out.println("dwjfjs：" + element.attributeValue("dwjfjs"));
      System.out.println("jfrylb：" + element.attributeValue("jfrylb"));
      System.out.println("dwmc：" + element.attributeValue("dwmc"));
      System.out.println("jfjs：" + element.attributeValue("jfjs"));
      System.out.println("dwbh：" + element.attributeValue("dwbh"));
      System.out.println("zzny：" + element.attributeValue("zzny"));
    }

  }

  // 正常情况下的
  public static String getXML(Boolean isCommon, String id) {
    StringBuffer sb = new StringBuffer();
    String XML_HEADER = "<?xml version=\"1.0\" encoding=\"GBK\"?>";
    sb.append(XML_HEADER);
    sb.append("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">");
    sb.append("<soap:Header>");
    sb.append("<in:system xmlns:in=\"http://www.molss.gov.cn/\">");
    sb.append("<para usr=\"\"/>");
    sb.append("<para pwd=\"\"/>");
    sb.append("<para funid=\"F00.00.0C\"/>");
    sb.append("</in:system>");
    sb.append("</soap:Header>");
    sb.append("<soap:Body>");
    sb.append("<in:business xmlns:in=\"http://www.molss.gov.cn/\">");
    if (isCommon) {
      sb.append("<para sqlstr=\"select '01' sblb,xzbz,jfjs,dwjfjs,jfrylb,si.orgn_natl.dwbh dwbh,dwmc,substr(qsny,1,4)||'.'||substr(qsny,5) qsny,substr(zzny,1,4)||'.'||substr(zzny,5) zzny,'计划' zdlsh from si.emp_plan,si.orgn_natl where grbh='" + id + "' and si.emp_plan.dwbh=si.orgn_natl.dwbh\"/>");
    } else {
      sb.append("<para sqlstr=\"select sblb,xzbz,jfjs,dwjfjs,si.orgn_natl.dwbh dwbh,dwmc,substr(qsny,1,4)||'.'||substr(qsny,5) qsny,substr(zzny,1,4)||'.'||substr(zzny,5) zzny,nvl(zdlsh,'未填单据') zdlsh from si.emp_add,si.orgn_natl where grbh='" + id + "' and si.emp_add.dwbh=si.orgn_natl.dwbh\"/>");
    }
    sb.append("<para g_com_gzrybh=\"cxyh\"/>");
    sb.append("<para g_com_gzryxm=\"通用查询\"/>");
    sb.append("<para g_com_sbjgbh=\"370100\"/>");
    sb.append("<para g_com_xzbz=\"A\"/>");
    sb.append("<para g_com_passwd=\"246325262611261127792835\"/>");
    sb.append("<para g_com_passwd_md5=\"be91ffca1d2a70fc3c3880851dba5903\"/>");
    sb.append("<para g_com_app=\"公共业务子系统\"/>");
    sb.append("<para g_com_xtlb=\"001\"/>");
    sb.append("<para g_com_mac=\"00-00-00-00-00-00\"/>");
    sb.append("</in:business>");
    sb.append("</soap:Body>");
    sb.append("</soap:Envelope>");
    // 返回String格式
    return sb.toString();
  }


}
