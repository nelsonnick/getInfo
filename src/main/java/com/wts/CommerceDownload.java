package com.wts;

import okhttp3.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.List;

import static com.wts.util.*;

public class CommerceDownload {

    public static void search(Integer j,String result, String id, String name) throws Exception {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
        RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"keyword\"\r\n\r\n"+id+"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
        Request request = new Request.Builder()
                .url("http://www.jnjgfw.gov.cn/command/search?keyword="+id)
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("connection", "keep-alive")
                .addHeader("access-control-allow-headers", "X-Requested-With")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        Document doc = Jsoup.parse(response.body().string());
        List<Element> mastheads = doc.select("div.result");
        for (int i=0;i<mastheads.size();i++){
            String e=mastheads.get(i).select("div.result_title>a").first().attr("href");
            String f=e.substring(22,e.length()-2).split(",")[2];
            String regNo=f.substring(1,f.length()-1);
            getDetail(result, regNo, id, name);
            System.out.println("第" + j + "行人员，身份证号码为：" + id + "，姓名为：" + name + "的数据已下载");
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
        xydm = mastheads.get(0).select("td.fieldInput").get(0).text().trim();
        yyzz = mastheads.get(0).select("td.fieldInput").get(1).text().trim();
        lx = mastheads.get(1).select("td.fieldInput").get(0).text().trim();
        jyz = mastheads.get(1).select("td.fieldInput").get(1).text().trim();
        zcrq = mastheads.get(2).select("td.fieldInput").get(0).text().trim();
        hzrq = mastheads.get(2).select("td.fieldInput").get(1).text().trim();
        yyqx1 = mastheads.get(3).select("td.fieldInput").get(0).text().trim();
        yyqx2 = mastheads.get(3).select("td.fieldInput").get(1).text().trim();
        djjg = mastheads.get(4).select("td.fieldInput").get(0).text().trim();
        djzt = mastheads.get(4).select("td.fieldInput").get(1).text().trim();
        zczb = mastheads.get(5).select("td.fieldInput").get(0).text().trim();
        zs = mastheads.get(6).select("td.fieldInput").get(0).text().trim();
        jycs = mastheads.get(7).select("td.fieldInput").get(0).text().trim();
        jyfw = mastheads.get(8).select("td.fieldInput").get(0).text().trim();

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

    public static void main(String[] args) throws Exception {
        //getDetail("a", "370100200276590", "370111196910143517");

        search(1,"a", "370111196910143517", "李东杰");
    }

}
