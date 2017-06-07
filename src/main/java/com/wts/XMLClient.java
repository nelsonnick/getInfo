package com.wts;

import okhttp3.*;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;

public class XMLClient {

  public static void main(String[] args) throws Exception {
    IPset();
    do {
      Thread.sleep(500);
    } while (!IPget().equals("10.153.73.166"));
    System.out.println("切换到内网");
    String id = "132123197902260016";
    OkHttpClient client = new OkHttpClient();
    MediaType mediaType = MediaType.parse("text/xml;charset=GBK");
    RequestBody body1 = RequestBody.create(mediaType, getXML(1, id));
    RequestBody body2 = RequestBody.create(mediaType, getXML(2, id));
    RequestBody body3 = RequestBody.create(mediaType, getXML(3, id));
    Request request1 = new Request.Builder()
            .url("http://10.153.50.123:80/lbs/MainServlet")
            .post(body1)
            .addHeader("content-type", "text/xml;charset=GBK")
            .addHeader("accept", "*.*;")
            .addHeader("host", "dareway")
            .addHeader("cache-control", "no-cache")
            .addHeader("postman-token", "71a5c099-7230-b17c-83cd-8a87479aae09")
            .build();
    Request request2 = new Request.Builder()
            .url("http://10.153.50.123:80/lbs/MainServlet")
            .post(body2)
            .addHeader("content-type", "text/xml;charset=GBK")
            .addHeader("accept", "*.*;")
            .addHeader("host", "dareway")
            .addHeader("cache-control", "no-cache")
            .addHeader("postman-token", "71a5c099-7230-b17c-83cd-8a87479aae09")
            .build();
    Request request3 = new Request.Builder()
            .url("http://10.153.50.123:80/lbs/MainServlet")
            .post(body3)
            .addHeader("content-type", "text/xml;charset=GBK")
            .addHeader("accept", "*.*;")
            .addHeader("host", "dareway")
            .addHeader("cache-control", "no-cache")
            .addHeader("postman-token", "71a5c099-7230-b17c-83cd-8a87479aae09")
            .build();

    Response response1 = client.newCall(request1).execute();
    Response response2 = client.newCall(request2).execute();
    Response response3 = client.newCall(request3).execute();
    Document document1 = DocumentHelper.parseText(response1.body().string());
    Document document2 = DocumentHelper.parseText(response2.body().string());
    Document document3 = DocumentHelper.parseText(response3.body().string());
    //获取根节点对象
    Element rootElement1 = document1.getRootElement();
    Element rootElement2 = document2.getRootElement();
    Element rootElement3 = document3.getRootElement();
    //获取子节点
    Element resultset1 = rootElement1.element("Body").element("business").element("resultset");
    Element resultset2 = rootElement2.element("Body").element("business").element("resultset");
    Element resultset3 = rootElement3.element("Body").element("business").element("resultset");

    String fileName = "c:/" + id + ".txt";
    File file = new File(fileName);
    if (file.exists() && file.isFile()) {
      file.delete();
    }
    file.createNewFile();
    BufferedWriter out = new BufferedWriter(new FileWriter(file));
    out.write("sblb\txzbz\tqsny\tzzny\tjfjs\tzdlsh\tdwbh\tdwmc\tdwjfjs\tqrsj\r\n"); // \r\n即为换行
    int i1 = 0;
    for (Iterator it1 = resultset1.elementIterator(); it1.hasNext(); ) {
      Element element1 = (Element) it1.next();
      i1 = i1 + 1;
      System.out.println(i1);
    }
    String[] sblb_1 = new String[i1];
    String[] xzbz_1 = new String[i1];
    String[] qsny_1 = new String[i1];
    String[] zzny_1 = new String[i1];
    String[] jfjs_1 = new String[i1];
    String[] zdlsh_1 = new String[i1];
    String[] dwbh_1 = new String[i1];
    String[] dwmc_1 = new String[i1];
    String[] dwjfjs_1 = new String[i1];
    int i=0;
    for (Iterator it1 = resultset1.elementIterator(); it1.hasNext(); ) {
      Element element1 = (Element) it1.next();
      sblb_1[i] = element1.attributeValue("sblb");
      i = i + 1;
    }
//    out.write(element1.attributeValue("sblb") + "\t");
//    out.write(element1.attributeValue("xzbz") + "\t");
//    out.write(element1.attributeValue("qsny") + "\t");
//    out.write(element1.attributeValue("zzny") + "\t");
//    out.write(element1.attributeValue("jfjs") + "\t");
//    out.write(element1.attributeValue("zdlsh") + "\t");
//    out.write(element1.attributeValue("dwbh") + "\t");
//    out.write(element1.attributeValue("dwmc") + "\t");
//    out.write(element1.attributeValue("dwjfjs") + "\t");
//    out.write("\t\r\n");

    out.flush(); // 把缓存区内容压入文件
    out.close(); // 最后记得关闭文件
    IPback();
    System.out.println("切换回外网");
  }

  // 1:正常缴费
  // 2:补缴
  // 3:补缴时间
  public static String getXML(Integer type, String id) {
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
    if (type == 1) {
      sb.append("<para sqlstr=\"select '01' sblb,xzbz,jfjs,dwjfjs,jfrylb,si.orgn_natl.dwbh dwbh,dwmc,substr(qsny,1,4)||'.'||substr(qsny,5) qsny,substr(zzny,1,4)||'.'||substr(zzny,5) zzny,'计划' zdlsh from si.emp_plan,si.orgn_natl where grbh='" + id + "' and si.emp_plan.dwbh=si.orgn_natl.dwbh\"/>");
    } else if (type == 2) {
      sb.append("<para sqlstr=\"select sblb,xzbz,jfjs,dwjfjs,si.orgn_natl.dwbh dwbh,dwmc,substr(qsny,1,4)||'.'||substr(qsny,5) qsny,substr(zzny,1,4)||'.'||substr(zzny,5) zzny,nvl(zdlsh,'未填单据') zdlsh from si.emp_add,si.orgn_natl where grbh='" + id + "' and si.emp_add.dwbh=si.orgn_natl.dwbh order by qsny,xzbz\"/>");
    } else if (type == 3) {
      sb.append("<para sqlstr=\"select zdlsh,qrsj from si.bill_genl   where djzt='2' and zdlsh in         (select zdlsh from si.emp_add where grbh='" + id + "' and zdlsh is not null)\"/>");
    } else {
      sb.append("");
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

  public static String IPget() {
    String Ip = null;
    try {
      Ip = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    return Ip;
  }

  public static void IPset() throws Exception {
    Runtime.getRuntime().exec("netsh    interface    ip    set    addr    \"本地连接\"    static    10.153.73.166    255.255.255.0     10.153.73.254     ");
  }

  public static void IPback() throws Exception {
    Runtime.getRuntime().exec("netsh    interface    ip    set    address name = \"本地连接\"    source = dhcp");
  }
}