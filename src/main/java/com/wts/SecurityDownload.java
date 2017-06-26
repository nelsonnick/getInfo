package com.wts;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

import static com.wts.util.*;

public class SecurityDownload {
  public static void download() throws Exception {
    System.out.println("               欢迎使用济南市社保信息批量下载程序         ");
    System.out.println(" ");
    System.out.println(" ");
    System.out.println("------------------------结果说明------------------------");
    System.out.println(" ");
    System.out.println("1：下载结果跟公共业务子系统中的社保下载结果保持一致！");
    System.out.println("2：下载结果为TXT文本文件！");
    System.out.println("3：Excel文件第一列为身份证号码，第二列内容必须为人员姓名！");
    System.out.println("4：下载过程采用多线程，多核CPU有额外优势！");
    System.out.println(" ");
    System.out.println("-------------------------------------------------------");
    System.out.println(" ");
    String result;
    do {
      // 输出提示文字
      System.out.print("请输入待下载的Excel文件名：");
      InputStreamReader is_reader = new InputStreamReader(System.in);
      result = new BufferedReader(is_reader).readLine();
    } while (result.equals("")); // 当用户输入无效的时候，反复提示要求用户输入
    File file = new File("C:\\" + result + ".xlsx");
    if (!file.exists()) {
      System.out.print("C:\\" + result + ".xlsx文件不存在！");
      System.out.print("按回车关闭程序...");
      while (true) {
        if (System.in.read() == '\n')
          System.exit(0);
      }
    } else {
//      IPset();
//      do {
//        Thread.sleep(500);
//      } while (!IPget().equals("10.153.73.166"));
//      System.out.println("切换到内网");

      XSSFWorkbook workbookBefore = new XSSFWorkbook(new FileInputStream("c:\\" + result + ".xlsx"));
      XSSFSheet sheetBefore = workbookBefore.getSheetAt(0);
      int total = sheetBefore.getLastRowNum()+1;
      System.out.println("总人数："+total);
      int total1,total2,total3,total4,total5,total6,total7;
      if (total<=8) {
        total1=total;
        total2=0;
        total3=0;
        total4=0;
        total5=0;
        total6=0;
        total7=0;
      }else{
        total1=(int)Math.ceil(total/8)+1;
        total2=(int)Math.ceil(total/8)*2+1;
        total3=(int)Math.ceil(total/8)*3+1;
        total4=(int)Math.ceil(total/8)*4+1;
        total5=(int)Math.ceil(total/8)*5+1;
        total6=(int)Math.ceil(total/8)*6+1;
        total7=(int)Math.ceil(total/8)*7+1;
      }

      //  创建文件夹
      File files = new File("C:/" + result+ "_社保下载数据");
      files.mkdir();
      copyFile("c:\\" + result + ".xlsx", "c:/" + result + "_社保下载数据/" + result + ".xlsx");
      if (total>8) {
        SecurityThread t1 = new SecurityThread(result, 1, total1, sheetBefore);
        SecurityThread t2 = new SecurityThread(result, total1, total2, sheetBefore);
        SecurityThread t3 = new SecurityThread(result, total2, total3, sheetBefore);
        SecurityThread t4 = new SecurityThread(result, total3, total4, sheetBefore);
        SecurityThread t5 = new SecurityThread(result, total4, total5, sheetBefore);
        SecurityThread t6 = new SecurityThread(result, total5, total6, sheetBefore);
        SecurityThread t7 = new SecurityThread(result, total6, total7, sheetBefore);
        SecurityThread t8 = new SecurityThread(result, total7, total + 2, sheetBefore);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();
        do {
        } while (!(t1.exit && t2.exit && t3.exit && t4.exit && t5.exit && t6.exit && t7.exit && t8.exit));
      }else{
        SecurityThread t = new SecurityThread(result, 1, total, sheetBefore);
        t.start();
        do {
        } while (!t.exit);
      }
      //      for (int i = 1; i < total1; i++) {
//        String personNumber = sheetBefore.getRow(i).getCell(0).getStringCellValue();
//        String personName = sheetBefore.getRow(i).getCell(1).getStringCellValue();
//        download(i, personNumber, personName, result);
//      }

//      IPback();
//      System.out.println("切换回外网");
      System.out.println("  ");
      System.out.println("  ");
      System.out.println("请查看下载后的文件--> c:/" + result + "_社保下载数据/");
      System.out.println("  ");
      System.out.println("按回车键退出程序...");
      while (true) {
        if (System.in.read() == '\n')
          System.exit(0);
      }
    }
  }

//  public static void download(Integer i, String id, String name, String result) throws Exception {
//    String fileName = "c:/" + result + "/" + id + name + ".txt";
//    File file = new File(fileName);
//    if (!file.exists()) {
//      System.out.println("正在下载第" + i + "行人员，身份证号码为：" + id + "，姓名为：" + name);
//      OkHttpClient client = new OkHttpClient();
//      MediaType mediaType = MediaType.parse("text/xml;charset=GBK");
//      RequestBody body1 = RequestBody.create(mediaType, getXML(1, id));
//      RequestBody body2 = RequestBody.create(mediaType, getXML(2, id));
//      RequestBody body3 = RequestBody.create(mediaType, getXML(3, id));
//      Request request1 = new Request.Builder()
//              .url("http://10.153.50.123:80/lbs/MainServlet")
//              .post(body1)
//              .addHeader("content-type", "text/xml;charset=GBK")
//              .addHeader("accept", "*.*;")
//              .addHeader("host", "dareway")
//              .addHeader("cache-control", "no-cache")
//              .addHeader("postman-token", "71a5c099-7230-b17c-83cd-8a87479aae09")
//              .build();
//      Request request2 = new Request.Builder()
//              .url("http://10.153.50.123:80/lbs/MainServlet")
//              .post(body2)
//              .addHeader("content-type", "text/xml;charset=GBK")
//              .addHeader("accept", "*.*;")
//              .addHeader("host", "dareway")
//              .addHeader("cache-control", "no-cache")
//              .addHeader("postman-token", "71a5c099-7230-b17c-83cd-8a87479aae09")
//              .build();
//      Request request3 = new Request.Builder()
//              .url("http://10.153.50.123:80/lbs/MainServlet")
//              .post(body3)
//              .addHeader("content-type", "text/xml;charset=GBK")
//              .addHeader("accept", "*.*;")
//              .addHeader("host", "dareway")
//              .addHeader("cache-control", "no-cache")
//              .addHeader("postman-token", "71a5c099-7230-b17c-83cd-8a87479aae09")
//              .build();
//
//      Response response1 = client.newCall(request1).execute();
//      Response response2 = client.newCall(request2).execute();
//      Response response3 = client.newCall(request3).execute();
//      Document document1 = DocumentHelper.parseText(response1.body().string());
//      Document document2 = DocumentHelper.parseText(response2.body().string());
//      Document document3 = DocumentHelper.parseText(response3.body().string());
//      //获取根节点对象
//      Element rootElement1 = document1.getRootElement();
//      Element rootElement2 = document2.getRootElement();
//      Element rootElement3 = document3.getRootElement();
//      //获取子节点
//      Element resultset1 = rootElement1.element("Body").element("business").element("resultset");
//      Element resultset2 = rootElement2.element("Body").element("business").element("resultset");
//      Element resultset3 = rootElement3.element("Body").element("business").element("resultset");
//
//      List<Element> elements2 = new ArrayList<Element>();
//      List<Element> elements3 = new ArrayList<Element>();
//
//      for (Iterator it = resultset2.elementIterator(); it.hasNext(); ) {
//        elements2.add((Element) it.next());
//      }
//      for (Iterator it = resultset3.elementIterator(); it.hasNext(); ) {
//        elements3.add((Element) it.next());
//      }
//
//      file.createNewFile();
//      BufferedWriter out = new BufferedWriter(new FileWriter(file));
//      out.write("sblb\txzbz\tqsny\tzzny\tjfjs\tzdlsh\tdwbh\tdwmc\tdwjfjs\tqrsj\r\n"); // \r\n即为换行
//
//      for (Iterator it = resultset1.elementIterator(); it.hasNext(); ) {
//        Element element = (Element) it.next();
//        out.write(element.attributeValue("sblb") + "\t");
//        out.write(element.attributeValue("xzbz") + "\t");
//        out.write(element.attributeValue("qsny") + "\t");
//        out.write(element.attributeValue("zzny") + "\t");
//        out.write(element.attributeValue("jfjs") + "\t");
//        out.write(element.attributeValue("zdlsh") + "\t");
//        out.write(element.attributeValue("dwbh") + "\t");
//        out.write(element.attributeValue("dwmc") + "\t");
//        out.write(element.attributeValue("dwjfjs") + "\t");
//        out.write("\t\r\n");
//      }
//      for (int m = 0; m < elements2.size(); m++) {
//        out.write(elements2.get(m).attributeValue("sblb") + "\t");
//        out.write(elements2.get(m).attributeValue("xzbz") + "\t");
//        out.write(elements2.get(m).attributeValue("qsny") + "\t");
//        out.write(elements2.get(m).attributeValue("zzny") + "\t");
//        out.write(elements2.get(m).attributeValue("jfjs") + "\t");
//        out.write(elements2.get(m).attributeValue("zdlsh") + "\t");
//        out.write(elements2.get(m).attributeValue("dwbh") + "\t");
//        out.write(elements2.get(m).attributeValue("dwmc") + "\t");
//        out.write(elements2.get(m).attributeValue("dwjfjs") + "\t");
//        for (int n = 0; n < elements3.size(); n++) {
//          if (elements2.get(m).attributeValue("zdlsh").equals(elements3.get(n).attributeValue("zdlsh"))) {
//            out.write(getTime(elements3.get(n).attributeValue("qrsj")) + "\t");
//            break;
//          }
//        }
//        out.write("\r\n");
//      }
//      out.flush(); // 把缓存区内容压入文件
//      out.close(); // 最后记得关闭文件
//    } else {
//      System.out.println("第" + i + "行人员，身份证号码为：" + id + "，姓名为：" + name + "的数据已下载");
//    }
//  }
//
//  // 1:正常缴费
//  // 2:补缴
//  // 3:补缴时间
//  public static String getXML(Integer type, String id) {
//    StringBuffer sb = new StringBuffer();
//    String XML_HEADER = "<?xml version=\"1.0\" encoding=\"GBK\"?>";
//    sb.append(XML_HEADER);
//    sb.append("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">");
//    sb.append("<soap:Header>");
//    sb.append("<in:system xmlns:in=\"http://www.molss.gov.cn/\">");
//    sb.append("<para usr=\"\"/>");
//    sb.append("<para pwd=\"\"/>");
//    sb.append("<para funid=\"F00.00.0C\"/>");
//    sb.append("</in:system>");
//    sb.append("</soap:Header>");
//    sb.append("<soap:Body>");
//    sb.append("<in:business xmlns:in=\"http://www.molss.gov.cn/\">");
//    if (type == 1) {
//      sb.append("<para sqlstr=\"select '01' sblb,xzbz,jfjs,dwjfjs,jfrylb,si.orgn_natl.dwbh dwbh,dwmc,substr(qsny,1,4)||'.'||substr(qsny,5) qsny,substr(zzny,1,4)||'.'||substr(zzny,5) zzny,'计划' zdlsh from si.emp_plan,si.orgn_natl where grbh='" + id + "' and si.emp_plan.dwbh=si.orgn_natl.dwbh\"/>");
//    } else if (type == 2) {
//      sb.append("<para sqlstr=\"select sblb,xzbz,jfjs,dwjfjs,si.orgn_natl.dwbh dwbh,dwmc,substr(qsny,1,4)||'.'||substr(qsny,5) qsny,substr(zzny,1,4)||'.'||substr(zzny,5) zzny,nvl(zdlsh,'未填单据') zdlsh from si.emp_add,si.orgn_natl where grbh='" + id + "' and si.emp_add.dwbh=si.orgn_natl.dwbh order by qsny,xzbz\"/>");
//    } else if (type == 3) {
//      sb.append("<para sqlstr=\"select zdlsh,qrsj from si.bill_genl   where djzt='2' and zdlsh in         (select zdlsh from si.emp_add where grbh='" + id + "' and zdlsh is not null)\"/>");
//    } else {
//      sb.append("");
//    }
//    sb.append("<para g_com_gzrybh=\"cxyh\"/>");
//    sb.append("<para g_com_gzryxm=\"通用查询\"/>");
//    sb.append("<para g_com_sbjgbh=\"370100\"/>");
//    sb.append("<para g_com_xzbz=\"A\"/>");
//    sb.append("<para g_com_passwd=\"246325262611261127792835\"/>");
//    sb.append("<para g_com_passwd_md5=\"be91ffca1d2a70fc3c3880851dba5903\"/>");
//    sb.append("<para g_com_app=\"公共业务子系统\"/>");
//    sb.append("<para g_com_xtlb=\"001\"/>");
//    sb.append("<para g_com_mac=\"00-00-00-00-00-00\"/>");
//    sb.append("</in:business>");
//    sb.append("</soap:Body>");
//    sb.append("</soap:Envelope>");
//    // 返回String格式
//    return sb.toString();
//  }
//

}
