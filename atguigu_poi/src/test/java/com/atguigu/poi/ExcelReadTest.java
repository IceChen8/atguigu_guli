package com.atguigu.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;

public class ExcelReadTest {

    //03读xls
    @Test
    public void testRead03() throws Exception{

        InputStream is = new FileInputStream("f:/guli-poi/write03.xls");

        Workbook workbook = new HSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);

        // 读取第一行第一列
        Row row = sheet.getRow(0);
        Cell cell = row.getCell(2);

        // 输出单元内容
        System.out.println(cell.getStringCellValue());

        // 操作结束，关闭文件
        is.close();
    }

    //07读xlsx
    @Test
    public void testRead07() throws Exception{

        InputStream is = new FileInputStream("f:/guli-poi/write03.xlsx");

        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);

        // 读取第一行第一列
        Row row = sheet.getRow(0);
        Cell cell = row.getCell(2);

        // 输出单元内容
        System.out.println(cell.getStringCellValue());

        // 操作结束，关闭文件
        is.close();
    }
}
