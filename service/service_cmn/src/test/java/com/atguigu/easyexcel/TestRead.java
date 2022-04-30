package com.atguigu.easyexcel;

import com.alibaba.excel.EasyExcel;

public class TestRead {
    public static void main(String[] args){
        //读取路径
        String fileName = "F:\\IDEASAVE\\hospital\\logdoc\\Excelexporttest\\01.xlsx";
        EasyExcel.read(fileName, UserReadData.class, new ExcelListener()).sheet().doRead();
    }

}
