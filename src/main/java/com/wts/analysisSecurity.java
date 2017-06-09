package com.wts;

import java.io.BufferedReader;
import java.io.FileReader;

public class analysisSecurity {
  public static void getIt(Integer i, String id, String name, String result) throws Exception {
    String fileName = "c:/" + result + "/" + id + name + ".txt";
    FileReader fileReader=new FileReader(fileName);
    BufferedReader reader=new BufferedReader(fileReader);

    while((reader.readLine())!=null)
    {
      System.out.println(reader.readLine());
    }
  }

  public static void main(String[] args)  throws Exception{
    getIt(1,"37010419550706161X","黄庆镇","a");
  }
}
