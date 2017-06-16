package com.wts;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import static com.wts.util.*;

public class CommerceDownload {
  public static void download() throws Exception {
    System.out.println("               欢迎使用济南市工商信息批量下载程序         ");
    System.out.println(" ");
    System.out.println(" ");
    System.out.println("------------------------结果说明------------------------");
    System.out.println(" ");
    System.out.println("1：下载结果跟《济南市事中事后监管服务系统》的工商查询结果保持一致！");
    System.out.println("2：下载结果为TXT文本文件！");
    System.out.println("3：Excel文件第一列为身份证号码，第二列内容必须为人员姓名！");
    System.out.println("4：下载过程采用多线程，多核CPU有额外优势！");
    System.out.println("5：下载速度约每分钟600人次，单次下载超过1000人时，系统服务器可能会无法响应，有必要时请重新启动程序！");
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
      XSSFWorkbook workbookBefore = new XSSFWorkbook(new FileInputStream("c:\\" + result + ".xlsx"));
      XSSFSheet sheetBefore = workbookBefore.getSheetAt(0);
      int total = sheetBefore.getLastRowNum();
      int total1,total2,total3,total4,total5,total6,total7;
      if (total<8) {
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
      File files = new File("C:/" + result+ "_工商下载数据");
      files.mkdir();
      copyFile("c:\\" + result + ".xlsx", "c:/" + result + "_工商下载数据/" + result + ".xlsx");
      if (total>8) {
        CommerceThread t1 = new CommerceThread(result, 1, total1, sheetBefore);
        CommerceThread t2 = new CommerceThread(result, total1, total2, sheetBefore);
        CommerceThread t3 = new CommerceThread(result, total2, total3, sheetBefore);
        CommerceThread t4 = new CommerceThread(result, total3, total4, sheetBefore);
        CommerceThread t5 = new CommerceThread(result, total4, total5, sheetBefore);
        CommerceThread t6 = new CommerceThread(result, total5, total6, sheetBefore);
        CommerceThread t7 = new CommerceThread(result, total6, total7, sheetBefore);
        CommerceThread t8 = new CommerceThread(result, total7, total + 1, sheetBefore);
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
        CommerceThread t = new CommerceThread(result, 1, total, sheetBefore);
        t.start();
        do {
        } while (!t.exit);
      }
//      for (int j=1;j<total;j++){
//        String id = sheetBefore.getRow(j).getCell(0).getStringCellValue();
//        String name = sheetBefore.getRow(j).getCell(1).getStringCellValue();
//        System.out.println("正在下载第" + j + "行人员，身份证号码为：" + id + "，姓名为：" + name);
//        search(result, id, name);
//      }

    }
    System.out.println("  ");
    System.out.println("  ");
    System.out.println("请查看下载后的文件--> c:/" + result + "_工商下载数据/");
    System.out.println("  ");
    System.out.println("按回车键退出程序...");
    while (true) {
      if (System.in.read() == '\n')
        System.exit(0);
    }
  }

}
