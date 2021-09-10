package org.ks365.osmp.common.utils;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.ks365.osmp.common.entity.ExportExcel;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * excel工具类
 *
 * @author tianslc
 */
public class ExcelUtils {
    public static String exportFailedList(List<Row> rowList, String title, String[] head) throws IOException {
        long time = System.currentTimeMillis();
        String fileName = time + ".xlsx";
        ExportExcel exportExcel = new ExportExcel(title, head);
        Sheet sheet = exportExcel.getSheet();
        int startInd = StringUtils.isNotBlank(title) ? 1 : 0;
        for (int i = 0; i < rowList.size(); i++) {
            Row oldRow = rowList.get(i);
            Row row = sheet.createRow(i + startInd + 1);
            for (int k = 0; k < head.length; k++) {
                if (oldRow.getCell(k) != null) {
                    CellType type = oldRow.getCell(k).getCellType();
                    if (type.equals(CellType.NUMERIC)) {
                        row.createCell(k).setCellValue(oldRow.getCell(k).getNumericCellValue());
                    } else {
                        row.createCell(k).setCellValue(oldRow.getCell(k).getStringCellValue());
                    }
                }
            }
        }
        File file = new File(FileUtils.getJarOuterPath("/temp/failed_list"));
        if (!file.exists()) {
            file.mkdirs();
        }
        exportExcel.writeFile(FileUtils.getJarOuterPath("/temp/failed_list/" + fileName));
        return "/temp/failed_list/" + fileName;
    }

    public static void commonExport(String fileName, String title, String[] heads, List<? extends Object> list, HttpServletResponse response) throws IOException {
        ExportExcel exportExcel = new ExportExcel(title, heads);
        exportExcel.setObjectList(list);
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        exportExcel.write(response.getOutputStream());
    }

    public static void commonLinkedExport(String fileName, String title, String[] heads, List<Map<String, Object>> list, HttpServletResponse response) throws IOException {
        ExportExcel exportExcel = new ExportExcel(title, heads);
        exportExcel.setMyList(list);
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        exportExcel.write(response.getOutputStream());
    }
}
