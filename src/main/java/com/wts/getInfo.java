package com.wts;


import java.io.BufferedReader;
import java.io.InputStreamReader;

import static com.wts.CommerceGet.commerce;
import static com.wts.SecurityDownload.download;
import static com.wts.SecurityGet.security;
import static com.wts.SecurityAnalysis.analysis;

public class getInfo {
    public static void main(String[] args) throws Exception{
        System.out.println("                        欢迎使用工商、社保快速核查程序");
        System.out.println("                                                                  制作人：王天硕");
        System.out.println(" ");
        System.out.println("------------------------------------用前须知------------------------------------");
        System.out.println(" ");
        System.out.println("1：使用本程序前，请先确定内外网环境！");
        System.out.println("2：待查的Excel文件必须放置在C盘根目录下！");
        System.out.println("3：Excel文件后缀为xlsx，不支持xls后缀的低版本Excel文件！");
        System.out.println("4：Excel文件第一行为标题，如果没有可以不填写！");
        System.out.println("5：Excel文件第一列内容必须为身份证号码！");
        System.out.println("6：Excel文件中的信息必须完整，即不能有空白的单元格！");
        System.out.println("7：第一、二项功能中建议单次查询人数不要超过1000人，否则抓取速度会非常慢！");
//        System.out.println("8：需要内网环境时，操作前会自动切换IP地址为10.153.73.166，操作后切换为自动获取!");
        System.out.println(" ");
        System.out.println("请选择要进行的操作，然后按回车");
        System.out.println(" ");
        System.out.println("1、核查工商信息--劳动993(内网)");
        System.out.println("2、核查社保信息--劳动993(内网)");
        System.out.println("3、下载社保信息--公共业务子系统(内网)");
        System.out.println("4、分析社保信息--公共业务子系统(内网)");
        System.out.println("5、下载工商信息--济南市事中事后监管服务系统(外网)");
        System.out.println("6、分析工商信息--济南市事中事后监管服务系统(外网)");
        System.out.println(" ");
        String result;
        do {
            // 输出提示文字
            System.out.print("请输入：");
            InputStreamReader is_reader = new InputStreamReader(System.in);
            result = new BufferedReader(is_reader).readLine();
        } while (!result.equals("1")
                && !result.equals("2")
                && !result.equals("3")
                && !result.equals("4")
                && !result.equals("5")
                && !result.equals("6")); // 当用户输入无效的时候，反复提示要求用户输入
        if (result.equals("1")){
            commerce();
        }else if (result.equals("2")){
            security();
        }else if (result.equals("3")){
            download();
        }else if (result.equals("4")){
            analysis();
        }else if (result.equals("5")){
            com.wts.CommerceDownload.download();
        }else if (result.equals("6")){
            com.wts.CommerceAnalysis.analysis();
        }
    }
}
