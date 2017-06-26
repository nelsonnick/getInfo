package com.wts;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

import static com.wts.util.getSecurity;

public class CommerceAnalysis {
  public static void analysis() throws Exception {

    System.out.println("               欢迎使用工商信息分析程序         ");
    System.out.println(" ");
    System.out.println(" ");
    System.out.println("------------------------结果说明------------------------");
    System.out.println(" ");
    System.out.println("1：用于分析本程序从《济南市事中事后监管服务系统》中下载的TXT文件！");
    System.out.println("2：Excel文件第一列为身份证号码，第二列内容必须为人员姓名！");
    System.out.println("3：分析结果中不含有联系电话等信息，如有需要，请通过993二次核查！");
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
        row.createCell(2).setCellValue("企业名称");
        row.createCell(3).setCellValue("信用代码");
        row.createCell(4).setCellValue("营业执照");
        row.createCell(5).setCellValue("类型");
        row.createCell(6).setCellValue("经营者");
        row.createCell(7).setCellValue("注册日期");
        row.createCell(8).setCellValue("核准日期");
        row.createCell(9).setCellValue("营业期限自");
        row.createCell(10).setCellValue("营业期限至");
        row.createCell(11).setCellValue("登记机关");
        row.createCell(12).setCellValue("登记状态");
        row.createCell(13).setCellValue("注册资本");
        row.createCell(14).setCellValue("住所");
        row.createCell(15).setCellValue("经营场所");
        row.createCell(16).setCellValue("经营范围");
        for (int k = 1; k < totals + 1; k++) {
          String id = sheetBefore.getRow(k).getCell(0).getStringCellValue();
          String name = sheetBefore.getRow(k).getCell(1).getStringCellValue();

          String fileName = "c:/" + result + "_工商下载数据/" + id + name + ".txt";
          File f = new File(fileName);
          if (!f.exists()) {
            int total = sheet.getLastRowNum()+1;
            XSSFRow newRow = sheet.createRow(total + 1);
            newRow.createCell(0).setCellValue(id);
            newRow.createCell(1).setCellValue(name);
            newRow.createCell(2).setCellValue("无下载信息");
            newRow.createCell(3).setCellValue("无下载信息");
            newRow.createCell(4).setCellValue("无下载信息");
            newRow.createCell(5).setCellValue("无下载信息");
            newRow.createCell(6).setCellValue("无下载信息");
            newRow.createCell(7).setCellValue("无下载信息");
            newRow.createCell(8).setCellValue("无下载信息");
            newRow.createCell(9).setCellValue("无下载信息");
            newRow.createCell(10).setCellValue("无下载信息");
            newRow.createCell(11).setCellValue("无下载信息");
            newRow.createCell(12).setCellValue("无下载信息");
            newRow.createCell(13).setCellValue("无下载信息");
            newRow.createCell(14).setCellValue("无下载信息");
            newRow.createCell(15).setCellValue("无下载信息");
            newRow.createCell(16).setCellValue("无下载信息");
          } else {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String lineTxt = null;
            String qymc = "", xydm = "", yyzz = "", lx = "", jyz = "", zcrq = "", hzrq = "", yyqx1 = "", yyqx2 = "", djjg = "", djzt = "", zczb = "", zs = "", jycs = "", jyfw = "";
            while ((lineTxt = reader.readLine()) != null) {
              String[] msg = lineTxt.split("\\t");
              int total = sheet.getLastRowNum();
              XSSFRow newRow = sheet.createRow(total + 1);
              newRow.createCell(0).setCellValue(id);
              newRow.createCell(1).setCellValue(name);
              newRow.createCell(2).setCellValue(msg[0]);
              newRow.createCell(3).setCellValue(msg[1]);
              newRow.createCell(4).setCellValue(msg[2]);
              newRow.createCell(5).setCellValue(msg[3]);
              newRow.createCell(6).setCellValue(msg[4]);
              newRow.createCell(7).setCellValue(msg[5]);
              newRow.createCell(8).setCellValue(msg[6]);
              newRow.createCell(9).setCellValue(msg[7]);
              newRow.createCell(10).setCellValue(msg[8]);
              newRow.createCell(11).setCellValue(msg[9]);
              newRow.createCell(12).setCellValue(msg[10]);
              newRow.createCell(13).setCellValue(msg[11]);
              newRow.createCell(14).setCellValue(msg[12]);
              newRow.createCell(15).setCellValue(msg[13]);
              newRow.createCell(16).setCellValue(msg[14]);
            }
          }
        }
        FileOutputStream os = new FileOutputStream("c:/" + result + "_工商下载数据/结果.xlsx");
        workbook.write(os);
        os.close();

    }
    System.out.println("  ");
    System.out.println("  ");
    System.out.println("数据分析完成！");
    System.out.println("请查看文件--> c:/" + result + "_工商下载数据/结果.xlsx");
    System.out.println("  ");
    System.out.println("按回车键退出程序...");
    while (true) {
      if (System.in.read() == '\n')
        System.exit(0);
    }
  }

}
