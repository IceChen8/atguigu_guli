package com.atguigu.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelWriteTest {

    @Test
    public void testWrite03() throws IOException {
        // 创建新的Excel 工作簿
        Workbook workbook = new HSSFWorkbook();

        // 在Excel工作簿中建一工作表，其名为缺省值 Sheet0
        //Sheet sheet = workbook.createSheet();

        // 如要新建一名为"会员登录统计"的工作表，其语句为：
        Sheet sheet = workbook.createSheet("会员登录统计");

        // 创建行（row 1）
        Row row1 = sheet.createRow(0);

        // 创建单元格（col 1-1）
        Cell cell11 = row1.createCell(0);
        cell11.setCellValue("统计时间");

        // 创建单元格（col 1-2）
        Cell cell12 = row1.createCell(1);
        cell12.setCellValue("名字");

        // 创建单元格（col 1-3）
        Cell cell13 = row1.createCell(2);
        cell13.setCellValue("年龄");

        // 创建行（row 2）
        Row row2 = sheet.createRow(1);

        // 创建单元格（col 2-1）
        Cell cell21 = row2.createCell(0);
        String dateTime = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
        cell21.setCellValue(dateTime);

        //创建单元格（col 2-2）
        Cell cell22 = row2.createCell(1);
        cell22.setCellValue("小明");

        //创建单元格（col 2-3）
        Cell cell23 = row2.createCell(2);
        cell23.setCellValue(18);

        // 新建一输出文件流（注意：要先创建文件夹）
        FileOutputStream out = new FileOutputStream("f:/guli-poi/write03.xls");
        // 把相应的Excel 工作簿存盘
        workbook.write(out);
        // 操作结束，关闭文件
        out.close();

        System.out.println("文件生成成功");
    }

    @Test
    public void testWrite07() throws IOException {
        // 创建新的Excel 工作簿
        Workbook workbook = new XSSFWorkbook();

        // 在Excel工作簿中建一工作表，其名为缺省值 Sheet0
        //Sheet sheet = workbook.createSheet();

        // 如要新建一名为"会员登录统计"的工作表，其语句为：
        Sheet sheet = workbook.createSheet("会员登录统计");

        // 创建行（row 1）
        Row row1 = sheet.createRow(0);

        // 创建单元格（col 1-1）
        Cell cell11 = row1.createCell(0);
        cell11.setCellValue("统计时间");

        // 创建单元格（col 1-2）
        Cell cell12 = row1.createCell(1);
        cell12.setCellValue("名字");

        // 创建单元格（col 1-3）
        Cell cell13 = row1.createCell(2);
        cell13.setCellValue("年龄");

        // 创建行（row 2）
        Row row2 = sheet.createRow(1);

        // 创建单元格（col 2-1）
        Cell cell21 = row2.createCell(0);
        String dateTime = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
        cell21.setCellValue(dateTime);

        //创建单元格（col 2-2）
        Cell cell22 = row2.createCell(1);
        cell22.setCellValue("小明");

        //创建单元格（col 2-3）
        Cell cell23 = row2.createCell(2);
        cell23.setCellValue(18);

        // 新建一输出文件流（注意：要先创建文件夹）
        FileOutputStream out = new FileOutputStream("f:/guli-poi/write03.xlsx");
        // 把相应的Excel 工作簿存盘
        workbook.write(out);
        // 操作结束，关闭文件
        out.close();

        System.out.println("文件生成成功");
    }

    //使用HSSF
    @Test
    public void testWrite03BigData() throws IOException {
        //记录开始时间
        long begin = System.currentTimeMillis();

        //创建一个HSSFWorkbook
        Workbook workbook = new HSSFWorkbook();

        //创建一个sheet，及表格
        Sheet sheet = workbook.createSheet();

        //xls文件最大支持65536行 //超出抛出java.lang.IllegalArgumentException: Invalid row number (65536) outside allowable range (0..65535)
        for (int rowNum = 0; rowNum < 65536; rowNum++) {
            //创建一个行
            Row row = sheet.createRow(rowNum);
            for (int cellNum = 0; cellNum < 10; cellNum++) {//创建单元格 及列
                Cell cell = row.createCell(cellNum);
                cell.setCellValue(cellNum);
            }
        }

        System.out.println("done");
        FileOutputStream out = new FileOutputStream("f:/guli-poi/test-write03-bigdata.xls");
        workbook.write(out);
        // 操作结束，关闭文件
        out.close();

        //记录结束时间
        long end = System.currentTimeMillis();
        System.out.println((double)(end - begin)/1000);
    }

    //使用XSSF
    @Test
    public void testWrite07BigData() throws IOException {
        //记录开始时间
        long begin = System.currentTimeMillis();
        //创建一个XSSFWorkbook
        Workbook workbook = new XSSFWorkbook();
        //创建一个表格
        Sheet sheet = workbook.createSheet();
        for (int rowNum = 0; rowNum < 200000; rowNum++){
            //创建一个行
            Row row = sheet.createRow(rowNum);
            for (int cellNum = 0; cellNum < 3; cellNum++){
                Cell cell = row.createCell(cellNum);//创建列，及一个格
                cell.setCellValue(cellNum);
            }

        }

        FileOutputStream out = new FileOutputStream("f:/guli-poi/test-write07-bigdata.xls");
        workbook.write(out);
        // 操作结束，关闭文件
        out.close();

        long end = System.currentTimeMillis();
        System.out.println((double)(end - begin)/1000);
    }

    /**
     * 以上测试结论：
     * 1、03版的比07版的效率高；
     * 2、03版的处理数据有限，最多65536行
     * 那么问题来了：
     * 如果：大数据量100W数据量，效率更高，插入怎么实现？
     */

    //使用SXSSF
    @Test
    public void testWrite07BigDataFast() throws IOException {
        //记录开始时间
        long begin = System.currentTimeMillis();

        //创建一个SXSSFWorkbook
        Workbook workbook = new SXSSFWorkbook();

        //创建一个表格
        Sheet sheet = workbook.createSheet();
        for (int rowNum = 0; rowNum < 200000; rowNum++){
            //创建一行
            Row row = sheet.createRow(rowNum);
            for (int cellNum = 0; cellNum < 5; cellNum++){
                Cell cell = row.createCell(cellNum);//创建列，及一个格
                cell.setCellValue(cellNum);
            }

        }

        //创建输出流指定输出的位置
        FileOutputStream out = new FileOutputStream("f:/guli-poi/test-write07-bigdata-fast.xlsx");
        //使用workbook写入
        workbook.write(out);
        // 操作结束，关闭文件
        out.close();

        //清除临时文件
        ((SXSSFWorkbook)workbook).dispose();

        //记录结束时间
        long end = System.currentTimeMillis();
        System.out.println((double)(end - begin)/1000);
    }
}
