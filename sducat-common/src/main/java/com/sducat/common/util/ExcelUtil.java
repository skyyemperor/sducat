package com.sducat.common.util;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;

/**
 * @author dsp
 * @Comments : 导入导出Excel工具类
 * 此类事实现操作指定的excel文件中的指定sheet页、
 * 读取指定的单元格内容、获取sheet中最后一行的行号的功能
 **/
public class ExcelUtil {
    private static XSSFSheet ExcelWSheet;
    /**
     * excel文件对象
     */
    private static XSSFWorkbook ExcelWBook;
    /**
     * 单元格对象
     */
    private static XSSFCell ExcelCell;

    /**
     * 舍得需要操作的excel的文件路径和sheet名称
     * 在读,写excel文件时，均需先调用此方法，设定要操作的excel的路径和sheet名称
     *
     * @param Path
     * @param SheetName
     */
    public static void setExcelFile(String Path, String SheetName) {
        FileInputStream ExcelFile;

        try {
            /**
             * 实例化excel文件的FileInputStream对象
             */
            ExcelFile = new FileInputStream(Path);
            /**
             * 实例化EXCEL文件的execlWXSSFWorkbook对象
             */
            ExcelWBook = new XSSFWorkbook(ExcelFile);
            /**
             * 实例化 XSSFCell 对象,指定excel文件中的sheet名称，后续用于sheet中行、列和单元格的操作
             */
            ExcelWSheet = ExcelWBook.getSheet(SheetName);

        } catch (Exception e) {
            /**
             *  TODO Auto-generated catch block
             */
            e.printStackTrace();
        }
    }

    /**
     * 读取excel文件中指定的单元格的函数，此函数只支持扩展名为.xlsx的excel文件
     *
     * @param rowNum
     * @param colNum
     * @return
     * @throws Exception
     */
    public static String getCellData(int rowNum, int colNum) {
        try {
            /**
             * 通过函数参数知道单元格的行号与列号，获取指定的单元格对象
             */
            ExcelCell = ExcelWSheet.getRow(rowNum).getCell(colNum);
            if(ExcelCell == null) return "";
            /**
             * 如果单元格的内容为字符串类型，则使用getStringCellValue方法来获取单元格内容
             * 如果单元格的内容为数字类型，   则使用getNumericCellValue方法来获取单元格内容
             */

            return ExcelCell.getCellType() == XSSFCell.CELL_TYPE_STRING ?
                    ExcelCell.getStringCellValue() + "" :
                    String.valueOf(Math.round(ExcelCell.getNumericCellValue()));
        } catch (Exception e) {
            e.printStackTrace();
            /**
             * 读取遇到异常，则返回空字符串
             */
            return "";
        }
    }

    /**
     * 获取excel文件的最后一行的行号
     *
     * @return
     */
    public static int getLastRowNum() {
        /**
         * 函数返回sheet的最后一行行号
         */
        return ExcelWSheet.getLastRowNum();
    }
}