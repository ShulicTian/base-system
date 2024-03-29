/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package org.ks365.osmp.common.entity;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.ks365.osmp.common.annotation.ExcelField;
import org.ks365.osmp.common.utils.DateUtils;
import org.ks365.osmp.common.utils.Encodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 导出Excel文件（导出“XLSX”格式，支持大数据量导出 @see org.apache.poi.ss.SpreadsheetVersion）
 *
 * @author ThinkGem
 * @version 2013-04-21
 */
public class ExportExcel {

    private static Logger log = LoggerFactory.getLogger(ExportExcel.class);
    /**
     * 注解列表（Object[]{ ExcelField, Field/Method }）
     */
    List<Object[]> annotationList = Lists.newArrayList();
    /**
     * 工作薄对象
     */
    private SXSSFWorkbook wb;
    /**
     * 工作表对象
     */
    private Sheet sheet;
    /**
     * 样式列表
     */
    private Map<String, CellStyle> styles;
    /**
     * 当前行号
     */
    private int rownum;

    /**
     * 构造函数
     *
     * @param title 表格标题，传“空值”，表示无标题
     * @param cls   实体对象，通过annotation.ExportField获取标题
     */
    public ExportExcel(String title, Class<?> cls) {
        this(title, cls, 1);
    }

    /**
     * 构造函数
     *
     * @param title  表格标题，传“空值”，表示无标题
     * @param cls    实体对象，通过annotation.ExportField获取标题
     * @param type   导出类型（1:导出数据；2：导出模板）
     * @param groups 导入分组
     */
    public ExportExcel(String title, Class<?> cls, int type, int... groups) {
        // Get annotation field
        Field[] fs = cls.getDeclaredFields();
        for (Field f : fs) {
            ExcelField ef = f.getAnnotation(ExcelField.class);
            if (ef != null && (ef.type() == 0 || ef.type() == type)) {
                if (groups != null && groups.length > 0) {
                    boolean inGroup = false;
                    for (int g : groups) {
                        if (inGroup) {
                            break;
                        }
                        for (int efg : ef.groups()) {
                            if (g == efg) {
                                inGroup = true;
                                annotationList.add(new Object[]{ef, f});
                                break;
                            }
                        }
                    }
                } else {
                    annotationList.add(new Object[]{ef, f});
                }
            }
        }
        // Get annotation method
        Method[] ms = cls.getDeclaredMethods();
        for (Method m : ms) {
            ExcelField ef = m.getAnnotation(ExcelField.class);
            if (ef != null && (ef.type() == 0 || ef.type() == type)) {
                if (groups != null && groups.length > 0) {
                    boolean inGroup = false;
                    for (int g : groups) {
                        if (inGroup) {
                            break;
                        }
                        for (int efg : ef.groups()) {
                            if (g == efg) {
                                inGroup = true;
                                annotationList.add(new Object[]{ef, m});
                                break;
                            }
                        }
                    }
                } else {
                    annotationList.add(new Object[]{ef, m});
                }
            }
        }
        // Field sorting
        Collections.sort(annotationList, new Comparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return new Integer(((ExcelField) o1[0]).sort()).compareTo(new Integer(((ExcelField) o2[0]).sort()));
            }

            ;
        });
        // Initialize
        List<String> headerList = Lists.newArrayList();
        for (Object[] os : annotationList) {
            String t = ((ExcelField) os[0]).title();
            // 如果是导出，则去掉注释
            if (type == 1) {
                String[] ss = StringUtils.split(t, "**", 2);
                if (ss.length == 2) {
                    t = ss[0];
                }
            }
            headerList.add(t);
        }
        initialize(title, headerList);
    }

    /**
     * 构造函数
     *
     * @param title   表格标题，传“空值”，表示无标题
     * @param headers 表头数组
     */
    public ExportExcel(String title, String[] headers) {
        initialize(title, Lists.newArrayList(headers));
    }

    /**
     * 构造函数
     *
     * @param title      表格标题，传“空值”，表示无标题
     * @param headerList 表头列表
     */
    public ExportExcel(String title, List<String> headerList) {
        initialize(title, headerList);
    }

    /**
     * 新的构造方法，指定表名及表的列数
     *
     * @param title 表头
     * @author tang
     */
    public ExportExcel(String title, int size) {
        this.wb = new SXSSFWorkbook(500);
        this.sheet = wb.createSheet("Export");
        this.styles = createStyles(wb);
        // Create title
        if (StringUtils.isNotBlank(title)) {
            Row titleRow = sheet.createRow(rownum++);
            titleRow.setHeightInPoints(30);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(styles.get("title"));
            titleCell.setCellValue(title);
            this.myAddMergedRegion(titleRow.getRowNum(), titleRow.getRowNum(), titleRow.getRowNum(), size - 1);
        }
        log.debug("Initialize success.");
    }

    /**
     * 初始化函数
     *
     * @param title      表格标题，传“空值”，表示无标题
     * @param headerList 表头列表
     */
    private void initialize(String title, List<String> headerList) {
        this.wb = new SXSSFWorkbook(500);
        this.sheet = wb.createSheet("Export");
        this.styles = createStyles(wb);
        // Create title
        if (StringUtils.isNotBlank(title)) {
            Row titleRow = sheet.createRow(rownum++);
            titleRow.setHeightInPoints(30);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(styles.get("title"));
            titleCell.setCellValue(title);
            sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), titleRow.getRowNum(), headerList.size() - 1));
        }
        // Create header
        if (headerList == null) {
            throw new RuntimeException("headerList not null!");
        }
        Row headerRow = sheet.createRow(rownum++);
        headerRow.setHeightInPoints(16);
        for (int i = 0; i < headerList.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellStyle(styles.get("header"));
            String[] ss = StringUtils.split(headerList.get(i), "**", 2);
            if (ss.length == 2) {
                cell.setCellValue(ss[0]);
                Comment comment = this.sheet.createDrawingPatriarch().createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
                comment.setString(new XSSFRichTextString(ss[1]));
                cell.setCellComment(comment);
            } else {
                cell.setCellValue(headerList.get(i));
            }
//            sheet.autoSizeColumn(i);
        }
        for (int i = 0; i < headerList.size(); i++) {
            int colWidth = sheet.getColumnWidth(i) * 2;
            sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);
        }
        log.debug("Initialize success.");
    }

    /**
     * 创建表格样式
     *
     * @param wb 工作薄对象
     * @return 样式列表
     */
    private Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();

        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBold(true);
        style.setFont(titleFont);
        styles.put("title", style);

        style = wb.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        // style.setWrapText(true); //设置自动换行
        styles.put("data", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.LEFT);
        styles.put("data1", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.CENTER);
        styles.put("data2", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        styles.put("data3", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        // style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);

        return styles;
    }

    /**
     * 添加一行
     *
     * @return 行对象
     */
    public Row addRow() {
        return sheet.createRow(rownum++);
    }

    /**
     * 添加一个单元格
     *
     * @param row    添加的行
     * @param column 添加列号
     * @param val    添加值
     * @return 单元格对象
     */
    public Cell addCell(Row row, int column, Object val) {
        return this.addCell(row, column, val, 0, Class.class);
    }

    /**
     * 添加一个单元格
     *
     * @param row    添加的行
     * @param column 添加列号
     * @param val    添加值
     * @param align  对齐方式（1：靠左；2：居中；3：靠右）
     * @return 单元格对象
     */
    public Cell addCell(Row row, int column, Object val, int align, Class<?> fieldType) {
        Cell cell = row.createCell(column);
        String cellFormatString = "@";
        try {
            if (val == null) {
                cell.setCellValue("");
            } else if (fieldType != Class.class) {
                cell.setCellValue((String) fieldType.getMethod("setValue", Object.class).invoke(null, val));
            } else {
                if (val instanceof String) {
                    cell.setCellValue((String) val);
                } else if (val instanceof Integer) {
                    cell.setCellValue((Integer) val);
                    cellFormatString = "0";
                } else if (val instanceof Long) {
                    cell.setCellValue((Long) val);
                    cellFormatString = "0";
                } else if (val instanceof Double) {
                    cell.setCellValue((Double) val);
                    cellFormatString = "0.00";
                } else if (val instanceof Float) {
                    cell.setCellValue((Float) val);
                    cellFormatString = "0.00";
                } else if (val instanceof Date) {
                    cell.setCellValue((Date) val);
                    cellFormatString = "yyyy-MM-dd HH:mm";
                } else {
                    cell.setCellValue((String) Class
                            .forName(
                                    this.getClass().getName()
                                            .replaceAll(this.getClass().getSimpleName(), "fieldtype." + val.getClass().getSimpleName() + "Type"))
                            .getMethod("setValue", Object.class).invoke(null, val));
                }
            }
            if (val != null) {
                CellStyle style = styles.get("data_column_" + column);
                if (style == null) {
                    style = wb.createCellStyle();
                    style.cloneStyleFrom(styles.get("data" + (align >= 1 && align <= 3 ? align : "")));
                    style.setDataFormat(wb.createDataFormat().getFormat(cellFormatString));
                    styles.put("data_column_" + column, style);
                }
                cell.setCellStyle(style);
            }
        } catch (Exception ex) {
            log.info("Set cell value [" + row.getRowNum() + "," + column + "] error: " + ex.toString());
            cell.setCellValue(val.toString());
        }
        return cell;
    }

    /**
     * 自定义设置list，与自定义表头配合使用
     *
     * @param list
     * @return
     */
    public <E> ExportExcel setMyList(List<Map<String, Object>> list) {
        String temp = "";
        for (Map<String, Object> map : list) {
            Row row = this.addRow();
            Set<String> keySet = map.keySet();
            int pos = 0;
            for (String str : keySet) {
                if (map.get(str) instanceof Date) {
                    temp = DateUtils.formatDate((Date) map.get(str), "yyyy-MM-dd HH:mm:ss");
                } else {
                    temp = map.get(str) + "";
                }
                this.addCell(row, pos++, temp);
            }
        }
        return this;
    }

    /**
     * 输出数据流
     *
     * @param os 输出数据流
     */
    public ExportExcel write(OutputStream os) throws IOException {
        wb.write(os);
        return this;
    }

    /**
     * 输出到客户端
     *
     * @param fileName 输出文件名
     */
    public ExportExcel write(HttpServletResponse response, String fileName) throws IOException {
        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(fileName));
        write(response.getOutputStream());
        return this;
    }

    /**
     * 输出到文件
     *
     * @param name 输出文件名
     */
    public ExportExcel writeFile(String name) throws FileNotFoundException, IOException {
        FileOutputStream os = new FileOutputStream(name);
        this.write(os);
        return this;
    }

    /**
     * 输出到文件
     *
     * @param file 输出文件
     */
    public ExportExcel writeFile(File file) throws IOException {
        FileOutputStream os = new FileOutputStream(file);
        this.write(os);
        return this;
    }

    /**
     * 清理临时文件
     */
    public ExportExcel dispose() {
        wb.dispose();
        return this;
    }

    /**
     * 写一行数据
     *
     * @param dataObj  对象数组
     * @param startRow 开始行号
     * @param style    样式 data2居中 data1居左 data3居右 header 表头 title 标题
     * @author tang
     */
    public void writeToRow(Object[] dataObj, int startRow, String style) {
        // 获取样式对象、使用默认的居中样式
        CellStyle cellStyle = this.styles.get(style);
        Row row = this.sheet.createRow(startRow);
        StringBuffer sb = new StringBuffer();
        String cellFormatString = "@";
        for (int i = 0; i < dataObj.length; i++) {
            Cell cell = row.createCell(i);
            Object val = dataObj[i];
            if (val instanceof String) {
                cell.setCellValue((String) val);
            } else if (val instanceof Integer) {
                cell.setCellValue((Integer) val);
                cellFormatString = "0";
            } else if (val instanceof Long) {
                cell.setCellValue((Long) val);
                cellFormatString = "0";
            } else if (val instanceof Double) {
                cell.setCellValue((Double) val);
                cellFormatString = "0.00";
            } else if (val instanceof Float) {
                cell.setCellValue((Float) val);
                cellFormatString = "0.00";
            } else if (val instanceof Date) {
                cell.setCellValue((Date) val);
                cellFormatString = "yyyy-MM-dd HH:mm";
            } else {
                cell.setCellValue((String) val);
            }
            cellStyle.setDataFormat(wb.createDataFormat().getFormat(cellFormatString));
            cell.setCellStyle(cellStyle);
            sb.append(val.toString() + " ,");
        }
        log.debug("Write success: [" + row.getRowNum() + "] " + sb.toString());
    }

    /**
     * 合并单元格
     *
     * @param firstLine 第一行行号
     * @param lastLine  最后一行行号
     * @param firstRow  第一列列号
     * @param lastRow   最后一列列号
     * @author tang
     */
    public void myAddMergedRegion(int firstLine, int lastLine, int firstRow, int lastRow) {
        CellRangeAddress cra = new CellRangeAddress(firstLine, lastLine, firstRow, lastRow);
        this.sheet.addMergedRegion(cra);
        // 设置合并单元格样式
        RegionUtil.setBorderBottom(BorderStyle.THIN, cra, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, cra, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, cra, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, cra, sheet);
        RegionUtil.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex(), cra, sheet);
    }

    /**
     * 切换工作表
     *
     * @param sheetIndex
     * @author weldon
     */
    public void changeSheet(int sheetIndex) {
        this.sheet = this.wb.getSheetAt(sheetIndex);
    }

    /**
     * 修改工作表名称
     *
     * @param sheetIndex
     * @param sheetName
     * @author weldon
     */
    public void modifySheetName(int sheetIndex, String sheetName) {
        this.wb.setSheetName(sheetIndex, sheetName);
    }

    /**
     * 新建一个工作表并切换至新工作表
     *
     * @param sheetName
     * @author weldon
     */
    public void newSheet(String sheetName) {
        this.sheet = wb.createSheet(sheetName);
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public CellStyle createCellStyle() {
        return wb.createCellStyle();
    }

    public void writeRow(String[] cols) {
        Row row = addRow();
        int i = 0;
        for (String col : cols) {
            row.createCell(i).setCellValue(col);
            i++;
        }
    }


    /**
     * 初始化函数
     *
     * @param headerList 表头列表
     */
    public void initSheet(String title, List<String> headerList, int startRow) {
        if (headerList == null) {
            throw new RuntimeException("headerList not null!");
        }
        rownum = startRow;
        if (StringUtils.isNotBlank(title)) {
            Row titleRow = sheet.createRow(rownum++);
            titleRow.setHeightInPoints(30);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(styles.get("title"));
            titleCell.setCellValue(title);
            sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, headerList.size() - 1));
        }
        Row headerRow = sheet.createRow(rownum++);
        headerRow.setHeightInPoints(16);
        for (int i = 0; i < headerList.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellStyle(styles.get("header"));
            String[] ss = StringUtils.split(headerList.get(i), "**", 2);
            if (ss.length == 2) {
                cell.setCellValue(ss[0]);
                Comment comment = this.sheet.createDrawingPatriarch().createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
                comment.setString(new XSSFRichTextString(ss[1]));
                cell.setCellComment(comment);
            } else {
                cell.setCellValue(headerList.get(i));
            }
//            sheet.autoSizeColumn(i);
        }
        for (int i = 0; i < headerList.size(); i++) {
            int colWidth = sheet.getColumnWidth(i) * 2;
            sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);
        }
        log.debug("Initialize success.");
    }

    public void setObjectList(List<? extends Object> list) {
        list.forEach(obj -> {
            Row row = this.addRow();
            LinkedHashMap<String, Object> map = objectToLinkedMap(obj);
            Set<String> keySet = map.keySet();
            int pos = 0;
            for (String str : keySet) {
                if (StringUtils.isNotEmpty(str)) {
                    this.addCell(row, pos++, map.get(str) + "");
                }
            }
        });
    }

    private LinkedHashMap<String, Object> objectToLinkedMap(Object obj) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = null;
            try {
                value = field.get(obj);
                if (value != null) {
                    if (value instanceof Date) {
                        value = DateUtils.formatDate((Date) value, "yyyy-MM-dd HH:mm:ss");
                    }
                    map.put(fieldName, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return map;
    }
}
