package com.springbootutils.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2017/1/16.
 */
public class ExcelUtil {
    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    public static void main(String[] args) throws Exception {
        List<List<String>> data = getData(new File("d:/abc.xlsx"));
//        System.out.println(JSON.toJSONString(data, true));
//        List<String> title = new ArrayList<>();
//        title.add("项目");
//        title.add("地址");
//        title.add("电话");
//        List<List<String>> content = new ArrayList<>();
//        content.add(title);
//        List<String> line = new ArrayList<>();
//        line.add("12312312");
//        line.add("12312312312312312");
//        line.add("03763855255");
//        content.add(line);
//
//        Map<String, List<String>> validationInfo = new HashMap<>();
//        List<String> item = new ArrayList<>();
//        item.add("a");
//        item.add("ab");
//        item.add("abc");
//        item.add("abcd");
//        validationInfo.put("项目", item);
//        excelToFile(writeToExcel("xlsx", title, content, validationInfo), "d:/txt.xlsx");
    }

    /**
     * 将数据写入到excel表格中，并返回对应的文件
     *
     * @param name    文件名称
     * @param type    文件类型 .xls .xlsx
     * @param dirPath 文件所在的目录
     * @param title   文件的标题
     * @param content 文件的内容
     * @return
     * @throws Exception
     */
    public static File getFile(String name, String type, String dirPath, List<String> title, Map<String, List<String>> validationInfo, List<List<String>> content) throws Exception {
        File file = new File(dirPath, name + "." + type);
        OutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream = writeToOutputStream(type, title, content, validationInfo, fileOutputStream);
        fileOutputStream.close();
        return file;
    }

    /**
     * 数据写入outputstream并返回
     *
     * @param type
     * @param title
     * @param content
     * @param outputStream
     * @return
     * @throws Exception
     */
    public static OutputStream writeToOutputStream(String type, List<String> title, List<List<String>> content, Map<String, List<String>> validationInfo, OutputStream outputStream) throws Exception {

        if ("xls".equalsIgnoreCase(type)) {
            type = "xls";
        } else if ("xlsx".equalsIgnoreCase(type)) {
            type = "xlsx";
        } else {
            throw new Exception("非法格式");
        }
        Workbook workbook = writeToExcel(type, title, content, validationInfo);
        workbook.write(outputStream);
        return outputStream;
    }

    private static void excelToFile(Workbook workbook, String path) throws IOException {
        File file = new File(path);
        OutputStream out = new FileOutputStream(file);
        workbook.write(out);
        out.close();

    }

    /**
     * 把数据写入表格
     *
     * @param type    excel版本，03之前xls， 之后xlsx
     * @param title
     * @param content
     * @return
     * @throws Exception
     */
    private static Workbook writeToExcel(String type, List<String> title, List<List<String>> content, Map<String, List<String>> validationInfo) throws Exception {

        Workbook workbook = createExcel(type);

//        /**
//         * 对textStyle进行写，使用锁操作
//         */
//        ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
//        writeLock.lock();
//        try{
//            if(textStyle == null){
//                textStyle = (HSSFCellStyle) workbook.createCellStyle();
//                DataFormat format = workbook.createDataFormat();
//                textStyle.setDataFormat(format.getFormat("@"));
//            }
//        }finally {
//            writeLock.unlock();
//        }
//


        Sheet sheet = createSheet(workbook, null);
        writeTitle(workbook, sheet, title, validationInfo);
        boolean hasTitle = false;
        if (title != null && title.size() > 0) {
            hasTitle = true;
        }
        writeContent(workbook, sheet, content, hasTitle);
        return workbook;
    }

    private static Workbook createExcel(String type) throws Exception {
        Workbook workbook = null;
        if ("xls".equals(type)) {
            workbook = new HSSFWorkbook();
        } else if ("xlsx".equals(type)) {
            workbook = new XSSFWorkbook();
        } else {
            throw new Exception("需要创建的excel表格格式错误");
        }
        return workbook;
    }

    private static Sheet createSheet(Workbook workbook, String name) {
        if (StringUtils.isEmpty(name)) {
            name = "第一页";
        }
        Sheet sheet = workbook.createSheet();
        return sheet;
    }

    /**
     * 写入标题
     * 从第0行开始写入
     *
     * @param workbook
     * @param sheet
     * @param title
     */
    private static void writeTitle(Workbook workbook, Sheet sheet, List<String> title, Map<String, List<String>> validationInfo) {
        CellStyle cellStyle = createCellStyle(workbook, true);
        sheet.autoSizeColumn(1, true);//设置宽度自适应
        Row row = sheet.createRow(0);
        for (int i = 0; i < title.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(title.get(i));

            String item = title.get(i);
            if (MapUtils.isNotEmpty(validationInfo) && validationInfo.containsKey(item)) {
                List<String> dropList = validationInfo.get(item);
                DataValidationHelper dvHelper = sheet.getDataValidationHelper();
                DataValidationConstraint dvConstraint = dvHelper.createExplicitListConstraint(
                        dropList.toArray(new String[dropList.size()]));
                CellRangeAddressList addressList = new CellRangeAddressList(1, 200, i, i);
                DataValidation validation = dvHelper.createValidation(
                        dvConstraint, addressList);
                // Note the check on the actual type of the DataValidation object.
                // If it is an instance of the XSSFDataValidation class then the
                // boolean value 'false' must be passed to the setSuppressDropDownArrow()
                // method and an explicit call made to the setShowErrorBox() method.
                if (validation instanceof XSSFDataValidation) {
                    validation.setSuppressDropDownArrow(true);
                    validation.setShowErrorBox(true);
                } else {
                    // If the Datavalidation contains an instance of the HSSFDataValidation
                    // class then 'true' should be passed to the setSuppressDropDownArrow()
                    // method and the call to setShowErrorBox() is not necessary.
                    validation.setSuppressDropDownArrow(false);
                }
                sheet.addValidationData(validation);
            }


        }
    }

    /**
     * 写入数据，行索引需要加上1，标题占用一行
     *
     * @param workbook
     * @param sheet
     * @param content
     * @param hasTitle 是否有title，没有title从第0行开始，有的话从第一行开始
     */
    private static void writeContent(Workbook workbook, Sheet sheet, List<List<String>> content, boolean hasTitle) {
        if (CollectionUtils.isNotEmpty(content)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            CellStyle cellStyle = createCellStyle(workbook, false);
            for (int i = 0; i < content.size(); i++) {
                List rowData = content.get(i);
                Row row = null;
                if (hasTitle) {
                    row = sheet.createRow(i + 1);
                } else {
                    row = sheet.createRow(i);
                }
                for (int j = 0; j < rowData.size(); j++) {
                    String res = "";
                    Object val = rowData.get(j);
                    if (val == null) {

                    } else if (val instanceof String) {
                        res = (String) val;
                    } else if (val instanceof Date) {
                        res = format.format((Date) val);
                    } else if (val instanceof Integer) {
                        res = val + "";
                    } else if (val instanceof Double) {
                        res = val + "";
                    } else if (val instanceof Short) {
                        res = val + "";
                    } else if (val instanceof BigDecimal) {
                        res = val + "";
                    } else {
                        res = val.toString();
                    }

                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(res);
                    cell.setCellType(CellType.STRING);

                }
            }
        }

    }

    /**
     * 创建样式
     *
     * @param workbook
     * @param isTitle
     * @return
     */
    private static CellStyle createCellStyle(Workbook workbook, boolean isTitle) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setFontName("微软雅黑");
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        DataFormat format = workbook.createDataFormat();
        cellStyle.setDataFormat(format.getFormat("@"));

        if (isTitle) {
            cellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            font.setBold(true);
        }
        return cellStyle;
    }


    /**
     * 从文件中读取excel数据
     *
     * @param file
     * @param index 可变参数，表示获取到的sheet的index， 可为空或者一个integer数值，如果传入多个Integer，以第一个为准
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static List<List<String>> getData(File file, Integer... index) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(file);
        Integer idx = 0;
        if (index != null && index.length > 0) {
            idx = index[0];
        }
        try {
            return getData(workbook, idx);
        } finally {
            workbook.close();
        }
    }

    /**
     * 从流中读取excel数据
     *
     * @param inputStream
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */

    /***
     * 从输入流中读取数据
     * @Time 2017/11/26 10:00
     * @Author daocers
     * @param   inputStream 输入流
     * @param index excel 的sheet索引，从0开始，默认为0
     * @return java.util.List<java.util.List < java.lang.String>>
     */
    public static List<List<String>> getData(InputStream inputStream, Integer... index) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(inputStream);
        Integer idx = 0;
        if (index != null && index.length > 0) {
            idx = index[0];
        }
        try {
            return getData(workbook, idx);
        } finally {
            workbook.close();
        }
    }

    public static List<List<List<String>>> getDataAboutSheet(InputStream inputStream, Integer... index) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(inputStream);
        List<List<List<String>>> datas = new ArrayList<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            try {
                datas.add(getData(workbook, i));
            } catch (Exception e) {
                datas.add(new ArrayList<>());
            } finally {
                workbook.close();
            }
        }
        return datas;
    }

    /**
     * 获取workbook中的数据
     * 空白单元格按照空字符串处理
     * 索引默认为0
     *
     * @param workbook
     * @param sheetIndex
     * @return
     */
    private static List<List<String>> getData(Workbook workbook, Integer sheetIndex) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (sheetIndex == null) {
            sheetIndex = 0;
        }
        List<List<String>> res = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        logger.debug("当前sheet： {}， 有 {} 行", new Integer[]{sheetIndex, sheet.getLastRowNum()});
        Integer cellCount = null;
        //for (Row row : sheet) {
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            List<String> rowData = new ArrayList<>();
            if (row == null) {
                if (res.size() != 0) {
                    for (int j = 0; j < res.get(0).size(); j++) {
                        rowData.add(null);
                    }
                }
                res.add(rowData);
                continue;
            }
            if (cellCount == null) {
                cellCount = Integer.valueOf(row.getLastCellNum());
            }
            Integer rowIndex = row.getRowNum();
            for (int j = 0; j < cellCount; j++) {
                Cell cell = row.getCell(j);
                Integer rowNum = row.getRowNum();
                logger.debug("当前row： {}， 有 {} 列", new Object[]{rowNum, row.getLastCellNum()});
                String data = "";
                if (cell != null) {
                    switch (cell.getCellType()) {
                        case STRING:
                            data = cell.getRichStringCellValue().getString();
                            break;
                        case NUMERIC:
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                                data = format.format(date);
                            } else {
                                data = cell.getNumericCellValue() + "";
                                if (data.contains(".") && data.contains("E")) {
                                    data = Double.valueOf(cell.getNumericCellValue()).longValue() + "";
                                }
                                System.out.println("data," + data);
                            }
                            break;
                        case BLANK:
                            data = "";
                            break;
                        case FORMULA:
                            data = cell.getCellFormula();
                            break;
                        case BOOLEAN:
                            data = cell.getBooleanCellValue() + "";
                            break;
                        default:
                            data = cell.getStringCellValue();
                    }
                }

                rowData.add(data);
            }
            res.add(rowData);
        }
        return res;

    }


    /**
     * 下载模板
     *
     * @param fileName       文件名称，不带后缀
     * @param title          表头
     * @param content        内容
     * @param validationInfo 校验信息，表头名称，List<String>数据信息
     * @return void
     * @Time 2017/12/14 0:04
     * @Author daocers
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, String fileName, List<String> title, List<List<String>> content, Map<String, List<String>> validationInfo) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        fileName += "-" + format.format(new Date()) + ".xlsx";
        // 给文件名编码,防止ie下载时文件名乱码
        if (request.getHeader("USER-AGENT").toLowerCase().contains("edge") // Edge-win10新的浏览器内核
                || request.getHeader("USER-AGENT").toLowerCase().contains("trident")) { // trident-IE浏览器内核
            fileName = URLEncoder.encode(fileName, "UTF-8");
            fileName = fileName.replace("+", "%20"); // 处理空格变“+”的问题
        } else { // 谷歌 火狐 360
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
        }
        // 设置返回值头
        response.setContentType("application/octet-stream;");
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        // 写入到文件
        OutputStream out = response.getOutputStream();
        ExcelUtil.writeToOutputStream("xlsx", title, content, validationInfo, out);
        out.close();
    }


}
