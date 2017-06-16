package com.wts;

import okhttp3.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class CommerceThread extends Thread {
  private String result;
  private Integer start;
  private Integer end;
  private XSSFSheet sheet;
  public volatile boolean exit = false;

  public CommerceThread(String result, Integer start, Integer end, XSSFSheet sheet) {
    this.result = result;
    this.start = start;
    this.end = end;
    this.sheet = sheet;
  }

  public void run() {
    try {
      for (int j = start; j < end; j++) {
        String id = sheet.getRow(j).getCell(0).getStringCellValue();
        String name = sheet.getRow(j).getCell(1).getStringCellValue();
        String fileName = "c:/" + result + "_工商下载数据/" + id + name + ".txt";
        File file = new File(fileName);
        if (!file.exists()) {
          System.out.println("正在下载第" + j + "行人员，身份证号码为：" + id + "，姓名为：" + name);
          search(result, id, name);
        } else {
          System.out.println("第" + j + "行人员，身份证号码为：" + id + "，姓名为：" + name + "的工商数据已下载");
        }
        super.run();
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    this.exit = true;
  }

  public static void search(String result, String id, String name) throws Exception {
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(0, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS)
            .build();

    MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
    RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"keyword\"\r\n\r\n" + id + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
    Request request = new Request.Builder()
            .url("http://www.jnjgfw.gov.cn/command/search?keyword=" + id)
            .post(body)
            .addHeader("content-type", "application/x-www-form-urlencoded")
            .addHeader("connection", "keep-alive")
            .addHeader("access-control-allow-headers", "X-Requested-With")
            .addHeader("cache-control", "no-cache")
            .build();
    Response response = client.newCall(request).execute();
    Document doc = Jsoup.parse(response.body().string());
    List<Element> mastheads = doc.select("div.result");
    if (mastheads.size() != 0) {
      for (int i = 0; i < mastheads.size(); i++) {
        String e = mastheads.get(i).select("div.result_title>a").first().attr("href");
        String f = e.substring(22, e.length() - 2).split(",")[2];
        String regNo = f.substring(1, f.length() - 1);
        getDetail(result, regNo, id, name);
      }
    } else {
      blankFile(result, id, name);
    }
  }

  public static void getDetail(String result, String regNo, String id, String name) throws IOException {

    String qymc = "", xydm = "", yyzz = "", lx = "", jyz = "", zcrq = "", hzrq = "", djjg = "", djzt = "", zs = "", jyfw = "", jycs = "", zczb = "", yyqx1 = "", yyqx2 = "";
    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
            .url("http://www.jnjgfw.gov.cn/command/search/detail?entName=&uniscId=&orgCode=&regNo=" + regNo + "&taxpayId=&keyword=" + id)
            .get()
            .addHeader("content-type", "text/html;charset=UTF-8")
            .addHeader("connection", "keep-alive")
            .addHeader("access-control-allow-headers", "X-Requested-With")
            .addHeader("cache-control", "no-cache")
            .build();

    Response response = client.newCall(request).execute();
    Document doc = Jsoup.parse(response.body().string());
    Element masthead = doc.select("div.qyqx-title>span").first();
    qymc = masthead.text();

    List<Element> mastheads = doc.select("table.table-bordered").first().select("tr");
    xydm = blank(mastheads.get(0).select("td.fieldInput").get(0).text().trim());
    yyzz = blank(mastheads.get(0).select("td.fieldInput").get(1).text().trim());
    lx = blank(mastheads.get(1).select("td.fieldInput").get(0).text().trim());
    jyz = blank(mastheads.get(1).select("td.fieldInput").get(1).text().trim());
    zcrq = blank(mastheads.get(2).select("td.fieldInput").get(0).text().trim());
    hzrq = blank(mastheads.get(2).select("td.fieldInput").get(1).text().trim());
    yyqx1 = blank(mastheads.get(3).select("td.fieldInput").get(0).text().trim());
    yyqx2 = blank(mastheads.get(3).select("td.fieldInput").get(1).text().trim());
    djjg = blank(mastheads.get(4).select("td.fieldInput").get(0).text().trim());
    djzt = blank(mastheads.get(4).select("td.fieldInput").get(1).text().trim());
    zczb = blank(mastheads.get(5).select("td.fieldInput").get(0).text().trim());
    zs = blank(mastheads.get(6).select("td.fieldInput").get(0).text().trim());
    jycs = blank(mastheads.get(7).select("td.fieldInput").get(0).text().trim());
    jyfw = blank(mastheads.get(8).select("td.fieldInput").get(0).text().trim());

    String fileName = "c:/" + result + "_工商下载数据/" + id + name + ".txt";
    File file = new File(fileName);
    if (!file.exists()) {
      file.createNewFile();
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      out.write(qymc + "\t");//企业名称
      out.write(xydm + "\t");//信用代码
      out.write(yyzz + "\t");//营业执照
      out.write(lx + "\t");//类型
      out.write(jyz + "\t");//经营者
      out.write(zcrq + "\t");//注册日期
      out.write(hzrq + "\t");//核准日期
      out.write(yyqx1 + "\t");//营业期限自
      out.write(yyqx2 + "\t");//营业期限至
      out.write(djjg + "\t");//登记机关
      out.write(djzt + "\t");//登记状态
      out.write(zczb + "\t");//注册资本
      out.write(zs + "\t");//住所
      out.write(jycs + "\t");//经营场所
      out.write(jyfw + "\t");//经营范围
      out.write("\r\n");
      out.flush(); // 把缓存区内容压入文件
      out.close(); // 最后记得关闭文件
    } else {
      BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
      out.write(qymc + "\t");//企业名称
      out.write(xydm + "\t");//信用代码
      out.write(yyzz + "\t");//营业执照
      out.write(lx + "\t");//类型
      out.write(jyz + "\t");//经营者
      out.write(zcrq + "\t");//注册日期
      out.write(hzrq + "\t");//核准日期
      out.write(yyqx1 + "\t");//营业期限自
      out.write(yyqx2 + "\t");//营业期限至
      out.write(djjg + "\t");//登记机关
      out.write(djzt + "\t");//登记状态
      out.write(zczb + "\t");//注册资本
      out.write(zs + "\t");//住所
      out.write(jycs + "\t");//经营场所
      out.write(jyfw + "\t");//经营范围
      out.write("\r\n");
      out.flush(); // 把缓存区内容压入文件
      out.close(); // 最后记得关闭文件
    }
  }

  public static void blankFile(String result, String id, String name) throws IOException {
    String fileName = "c:/" + result + "_工商下载数据/" + id + name + ".txt";
    File file = new File(fileName);
    if (!file.exists()) {
      file.createNewFile();
    }
  }

  public static String blank(String str) {
    if (str.equals("")) {
      return " ";
    } else {
      return str;
    }
  }

  public static void main(String[] args) throws Exception {
    //getDetail("a", "370100200276590", "370111196910143517");
    search("b", "120107196508244538", "杨洪银");
    //search("a", "370111196910143517", "李东杰");
  }

}
