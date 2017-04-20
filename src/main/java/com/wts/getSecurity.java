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
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;

import static com.wts.util.getCharacterPosition;

public class getSecurity {

    /**
     * 保存记录
     * */
    private static void save(JSONArray jsStrs, XSSFSheet SheetBefore, XSSFSheet SheetAfter, int count, int i) {
        XSSFRow nextRows = SheetAfter.createRow(SheetAfter.getLastRowNum()+1);
        for (int j = 0; j < count; j++) {
            nextRows.createCell(j).setCellValue(SheetBefore.getRow(i).getCell(j).toString());
        }
        if (jsStrs.size() > 0) {
            JSONObject jsStr = jsStrs.getJSONObject(0);
            nextRows.createCell(count).setCellValue(jsStr.getString("grxm"));
            nextRows.createCell(count + 1).setCellValue(jsStr.getString("dwmc"));
            nextRows.createCell(count + 2).setCellValue(jsStr.getString("dwsbjfbh"));
            nextRows.createCell(count + 3).setCellValue(jsStr.getString("dwxz"));
            nextRows.createCell(count + 4).setCellValue(util.getSecurity(jsStr.getString("dwsbjfbh")));
            nextRows.createCell(count + 5).setCellValue(jsStr.getString("zzny"));
        }else{
            nextRows.createCell(count).setCellValue("");
            nextRows.createCell(count + 1).setCellValue("");
            nextRows.createCell(count + 2).setCellValue("");
            nextRows.createCell(count + 3).setCellValue("");
            nextRows.createCell(count + 4).setCellValue("");
            nextRows.createCell(count + 5).setCellValue("无缴费记录");
        }
    }
    /**
     * 保存记录
     * */
    private static void saveNo( XSSFSheet SheetBefore, XSSFSheet SheetAfter, int count, int i) {
        XSSFRow nextRows = SheetAfter.createRow(SheetAfter.getLastRowNum()+1);
        for (int j = 0; j < count; j++) {
            nextRows.createCell(j).setCellValue(SheetBefore.getRow(i).getCell(j).toString());
        }
        nextRows.createCell(count).setCellValue("");
        nextRows.createCell(count + 1).setCellValue("");
        nextRows.createCell(count + 2).setCellValue("");
        nextRows.createCell(count + 3).setCellValue("");
        nextRows.createCell(count + 4).setCellValue("");
        nextRows.createCell(count + 5).setCellValue("无缴费记录");
    }
    /**
     * 发起请求
     * */
    private static void send(CloseableHttpClient login_httpclient,XSSFSheet sheetBefore, XSSFSheet sheetAfter, int count, int i,String personNumber) throws Exception{
        URI gsUrl = new URIBuilder()
                .setScheme("http")
                .setHost("10.153.50.108:7001")
                .setPath("/lemis3/lemis3Person.do")
                .setParameter("method", "querySbjPersonInfo")
                .setParameter("_xmlString", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s gmsfhm=\"" + personNumber + "\" xshs=\"200\" /></p>")
                .setParameter("_jbjgqxfw", "undefined")
                .setParameter("_sbjbjg", "undefined")
                .setParameter("_dwqxfw", "undefined")
                .build();
        HttpPost gs_post = new HttpPost(gsUrl);
        // 创建默认的httpClient实例.
        CloseableHttpResponse gs_response = login_httpclient.execute(gs_post);
        HttpEntity gs_entity = gs_response.getEntity();
        String res = EntityUtils.toString(gs_entity, "UTF-8");
        String s1 = "init('true','true','[";
        String e1 = "]');</script>";
        String s2 = "init\\(\\'true\\'\\,\\'true\\'\\,\\'\\[";
        String e2 = "\\]\\'\\)\\;\\<\\/script\\>";
      if (util.getCount(res, s1) == 0) {
            saveNo(sheetBefore, sheetAfter, count, i);
        }else if (util.getCount(res, s1) == 1) {
            if (res.substring(res.indexOf(s1) + 20, res.indexOf(e1) + 1).indexOf("dwsbjfbh") > 0) {
                JSONArray jsStrs = JSONArray.fromObject(res.substring(res.indexOf(s1) + 20, res.indexOf(e1) + 1));
                save(jsStrs, sheetBefore, sheetAfter, count, i);
            }else{
                saveNo(sheetBefore, sheetAfter, count, i);
            }
        } else if (util.getCount(res, s1) == 2) {
            if (res.substring(res.lastIndexOf(s1) + 20, res.lastIndexOf(e1) + 1).lastIndexOf("dwsbjfbh") <= 0 && res.substring(res.indexOf(s1) + 20, res.indexOf(e1) + 1).indexOf("dwsbjfbh") <= 0) {
              saveNo(sheetBefore, sheetAfter, count, i);
            } else {
              if (res.substring(res.lastIndexOf(s1) + 20, res.lastIndexOf(e1) + 1).lastIndexOf("dwsbjfbh") > 0) {
                JSONArray jsStrs1 = JSONArray.fromObject(res.substring(res.lastIndexOf(s1) + 20, res.lastIndexOf(e1) + 1));
                save(jsStrs1, sheetBefore, sheetAfter, count, i);
              }
              if (res.substring(res.indexOf(s1) + 20, res.indexOf(e1) + 1).indexOf("dwsbjfbh") > 0) {
                JSONArray jsStrs2 = JSONArray.fromObject(res.substring(res.indexOf(s1) + 20, res.indexOf(e1) + 1));
                save(jsStrs2, sheetBefore, sheetAfter, count, i);
              }
            }
        } else if (util.getCount(res, s1) == 3) {
            if (res.substring(res.indexOf(s1) + 20, res.indexOf(e1) + 1).indexOf("dwsbjfbh") <= 0 && res.substring(res.lastIndexOf(s1) + 20, res.lastIndexOf(e1) + 1).lastIndexOf("dwsbjfbh") <= 0 && res.substring(getCharacterPosition(res,s2,2)+ 20,getCharacterPosition(res,e2,2)+1).lastIndexOf("dwsbjfbh") <= 0) {
              saveNo(sheetBefore, sheetAfter, count, i);
            }else {
              if (res.substring(res.indexOf(s1) + 20, res.indexOf(e1) + 1).indexOf("dwsbjfbh") > 0) {
                JSONArray jsStrs1 = JSONArray.fromObject(res.substring(res.indexOf(s1) + 20, res.indexOf(e1) + 1));
                save(jsStrs1, sheetBefore, sheetAfter, count, i);
              }
              if (res.substring(getCharacterPosition(res, s2, 2) + 20, getCharacterPosition(res, e2, 2) + 1).lastIndexOf("dwsbjfbh") > 0) {
                JSONArray jsStrs2 = JSONArray.fromObject(res.substring(getCharacterPosition(res, s2, 2) + 20, getCharacterPosition(res, e2, 2) + 1));
                save(jsStrs2, sheetBefore, sheetAfter, count, i);
              }
              if (res.substring(res.lastIndexOf(s1) + 20, res.lastIndexOf(e1) + 1).lastIndexOf("dwsbjfbh") > 0) {
                JSONArray jsStrs3 = JSONArray.fromObject(res.substring(res.lastIndexOf(s1) + 20, res.lastIndexOf(e1) + 1));
                save(jsStrs3, sheetBefore, sheetAfter, count, i);
              }
            }
        } else {
          if (res.substring(res.indexOf(s1) + 20, res.indexOf(e1) + 1).indexOf("dwsbjfbh") > 0) {
            JSONArray jsStrs1 = JSONArray.fromObject(res.substring(res.indexOf(s1) + 20, res.indexOf(e1) + 1));
            save(jsStrs1, sheetBefore, sheetAfter, count, i);
          }
          if (res.substring(getCharacterPosition(res, s2, 2) + 20, getCharacterPosition(res, e2, 2) + 1).lastIndexOf("dwsbjfbh") > 0) {
            JSONArray jsStrs2 = JSONArray.fromObject(res.substring(getCharacterPosition(res, s2, 2) + 20, getCharacterPosition(res, e2, 2) + 1));
            save(jsStrs2, sheetBefore, sheetAfter, count, i);
          }
        }
        gs_response.close();
    }
//    public static void main(String[] args) throws Exception {
    public static void security() throws Exception {
        System.out.println("               欢迎使用济南市社保信息批量速查程序         ");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("------------------------结果说明------------------------");
        System.out.println(" ");
        System.out.println("1：核查结果跟劳动993系统中的社保查询结果保持一致！");
        System.out.println("2：查询结果仅包含最近一次的缴费情况！");
        System.out.println("3：查询人员同时存在多个缴纳类型时，程序会尝试全部抓取下来！");
        System.out.println(" ");
        System.out.println("-------------------------------------------------------");
        System.out.println(" ");
        String result;
        do {
            // 输出提示文字
            System.out.print("请输入待查的Excel文件名：");
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
            URI loginUri = new URIBuilder()
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
            HttpPost login_post = new HttpPost(loginUri);
            // 创建默认的httpClient实例.
            CloseableHttpClient login_httpclient = HttpClients.createDefault();
            CloseableHttpResponse login_response = login_httpclient.execute(login_post);
            HttpEntity login_entity = login_response.getEntity();
            if (login_entity != null) {
                System.out.println("  ");
                System.out.println("是否成功接入社保数据库: " + EntityUtils.toString(login_entity, "UTF-8"));
            }
            System.out.println("3秒后开始抓取数据....");
            Thread.sleep(1000);
            System.out.println("2秒后开始抓取数据....");
            Thread.sleep(1000);
            System.out.println("1秒后开始抓取数据....");
            Thread.sleep(1000);
            System.out.println("开始抓取....");
            XSSFWorkbook workbookAfter = new XSSFWorkbook();
            XSSFSheet sheetAfter = workbookAfter.createSheet("sheet1");
            XSSFWorkbook workbookBefore = new XSSFWorkbook(new FileInputStream("c:\\" + result + ".xlsx"));
            XSSFSheet sheetBefore = workbookBefore.getSheetAt(0);
            XSSFRow rowAfter = sheetAfter.createRow(0);
            int count = sheetBefore.getRow(0).getPhysicalNumberOfCells();
            int total = sheetBefore.getLastRowNum();
            for (int j = 0; j < count; j++) {
                rowAfter.createCell(j).setCellValue(sheetBefore.getRow(0).getCell(j).toString());
            }
            rowAfter.createCell(count).setCellValue("个人姓名");//grxm
            rowAfter.createCell(count + 1).setCellValue("单位名称");//dwmc
            rowAfter.createCell(count + 2).setCellValue("单位编号");//dwsbjfbh
            rowAfter.createCell(count + 3).setCellValue("单位性质"); //dwxz
            rowAfter.createCell(count + 4).setCellValue("缴纳险种");
            rowAfter.createCell(count + 5).setCellValue("缴纳月份");
            for (int i = 1; i < total + 1; i++) {
                String personNumber = sheetBefore.getRow(i).getCell(0).getStringCellValue();
                System.out.println("正在抓取第"+i+"行人员，身份证号码为："+personNumber);
                send(login_httpclient,sheetBefore,sheetAfter,count,i,personNumber);
            }
            login_response.close();
            login_httpclient.close();
            FileOutputStream os = new FileOutputStream("c:\\" + result + "_社保数据抓取后.xlsx");
            workbookAfter.write(os);
            os.close();
            System.out.println("  ");
            System.out.println("  ");
            System.out.println("社保数据抓取完成！");
            System.out.println("请查看文件--> c:\\" + result + "_社保数据抓取后.xlsx");
            System.out.println("  ");
            System.out.println("按回车键退出程序...");
            while (true) {
                if (System.in.read() == '\n')
                    System.exit(0);
            }
        }
    }
}
