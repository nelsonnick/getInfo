package com.wts;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

import static com.wts.util.getSecurity;

public class SecurityAnalysis {
  public static void analysis() throws Exception {

    System.out.println("               欢迎使用社保信息分析程序         ");
    System.out.println(" ");
    System.out.println(" ");
    System.out.println("------------------------结果说明------------------------");
    System.out.println(" ");
    System.out.println("1：用于分析本程序从《公共业务子系统》中下载的TXT文件！");
    System.out.println("2：Excel文件第一列为身份证号码，第二列内容必须为人员姓名！");
    System.out.println(" ");
    System.out.println("-------------------------------------------------------");
    System.out.println(" ");
    String result, months, month;
    do {
      // 输出提示文字
      System.out.print("请输入待查的Excel文件名：");
      InputStreamReader is_reader = new InputStreamReader(System.in);
      result = new BufferedReader(is_reader).readLine();
    } while (result.equals("")); // 当用户输入无效的时候，反复提示要求用户输入
    do {
      // 输出提示文字
      System.out.print("请输入要查询的6位年月：");
      InputStreamReader is_reader = new InputStreamReader(System.in);
      months = new BufferedReader(is_reader).readLine();
    } while (months.equals("")); // 当用户输入无效的时候，反复提示要求用户输入

    if (!months.matches("\\d{6}")) {
      System.out.print("输入的6位年月错误！");
      System.out.print("按回车关闭程序...");
      while (true) {
        if (System.in.read() == '\n')
          System.exit(0);
      }
    } else {
      month = months.substring(0, 4) + '.' + months.substring(4, 6);

      File file = new File("C:\\" + result + ".xlsx");
      if (!file.exists()) {
        System.out.print("C:\\" + result + ".xlsx文件不存在！");
        System.out.print("按回车关闭程序...");
        while (true) {
          if (System.in.read() == '\n')
            System.exit(0);
        }
      } else {
        XSSFWorkbook workbookBefore = new XSSFWorkbook(new FileInputStream("c:\\" + result + ".xlsx"));
        XSSFSheet sheetBefore = workbookBefore.getSheetAt(0);
        int count = sheetBefore.getRow(0).getPhysicalNumberOfCells();
        int totals = sheetBefore.getLastRowNum();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet1");
        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("证件号码");
        row.createCell(1).setCellValue("人员姓名");
        row.createCell(2).setCellValue("缴费年月");
        row.createCell(3).setCellValue("单位编号");
        row.createCell(4).setCellValue("单位名称");
        row.createCell(5).setCellValue("缴费性质");
        row.createCell(6).setCellValue("缴费情况");
        row.createCell(7).setCellValue("缴费时间");
        for (int k = 1; k < totals + 1; k++) {
          String id = sheetBefore.getRow(k).getCell(0).getStringCellValue();
          String name = sheetBefore.getRow(k).getCell(1).getStringCellValue();

          String fileName = "c:/" + result + "_社保下载数据/" + id + name + ".txt";
          File f = new File(fileName);
          if (!f.exists()) {
            int total = sheet.getLastRowNum();
            XSSFRow newRow = sheet.createRow(total + 1);
            newRow.createCell(0).setCellValue(id);
            newRow.createCell(1).setCellValue(name);
            newRow.createCell(2).setCellValue(month);
            newRow.createCell(3).setCellValue("无下载信息");
            newRow.createCell(4).setCellValue("无下载信息");
            newRow.createCell(5).setCellValue("无下载信息");
            newRow.createCell(6).setCellValue("无下载信息");
            newRow.createCell(7).setCellValue("无下载信息");
          } else {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String lineTxt = null;

            String xzbz = "", qsny = "", zzny = "", jfjs = "", zdlsh = "", dwbh = "", dwmc = "", dwjfjs = "", qrsj = "", aa = "", bb = "";
            while ((lineTxt = reader.readLine()) != null) {
              String[] msg = lineTxt.split("\\t");
              if (msg[1].equals("A")
                      && Double.parseDouble(msg[2]) <= Double.parseDouble(month)
                      && Double.parseDouble(msg[3]) >= Double.parseDouble(month)
                      ) {
                xzbz = msg[1];
                qsny = msg[2];
                zzny = msg[3];
                jfjs = msg[4];
                zdlsh = msg[5];
                dwbh = msg[6];
                dwmc = msg[7];
                dwjfjs = msg[8];
                qrsj = msg[9];
              }
            }
            if (!dwbh.equals("")) {
              if (zdlsh.equals("计划  ")) {
                aa = "正常缴费";
                bb = "当月缴费";
              } else if (zdlsh.trim().equals("未填单据")) {
                aa = "未交费";
                bb = "开出单据未缴费";
              } else{
                aa = "补缴";
                bb = qrsj;
              }
              int total = sheet.getLastRowNum();
              XSSFRow newRow = sheet.createRow(total + 1);
              newRow.createCell(0).setCellValue(id);
              newRow.createCell(1).setCellValue(name);
              newRow.createCell(2).setCellValue(month);
              newRow.createCell(3).setCellValue(dwbh);
              newRow.createCell(4).setCellValue(dwmc);
              newRow.createCell(5).setCellValue(getSecurity(dwbh));
              newRow.createCell(6).setCellValue(aa);
              newRow.createCell(7).setCellValue(bb);
            } else {
              int total = sheet.getLastRowNum();
              XSSFRow newRow = sheet.createRow(total + 1);
              newRow.createCell(0).setCellValue(id);
              newRow.createCell(1).setCellValue(name);
              newRow.createCell(2).setCellValue(month);
              newRow.createCell(3).setCellValue("无养老缴费记录");
              newRow.createCell(4).setCellValue("无养老缴费记录");
              newRow.createCell(5).setCellValue("无养老缴费记录");
              newRow.createCell(6).setCellValue("无养老缴费记录");
              newRow.createCell(7).setCellValue("无养老缴费记录");
            }
          }
        }
        FileOutputStream os = new FileOutputStream("c:/" + result + "_社保下载数据/结果" + months + ".xlsx");
        workbook.write(os);
        os.close();
      }
    }
    System.out.println("  ");
    System.out.println("  ");
    System.out.println("数据分析完成！");
    System.out.println("请查看文件--> c:/" + result + "_社保下载数据/结果" + months + ".xlsx");
    System.out.println("  ");
    System.out.println("按回车键退出程序...");
    while (true) {
      if (System.in.read() == '\n')
        System.exit(0);
    }
  }

}
