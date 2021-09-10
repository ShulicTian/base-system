package org.ks365.osmp.common.utils;


import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;

/**
 * 文件处理工具类
 *
 * @author rses
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    /**
     * 写入本地文件
     *
     * @param is
     * @param path
     */
    public static void writeToLocal(InputStream is, String path) {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path))) {
            byte[] arr = new byte[1024];
            int len = 0;
            while ((len = is.read(arr)) != -1) {
                outputStream.write(arr, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取jar包内文件
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static InputStream getLocalFileStream(String path) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(path);
        return classPathResource.getInputStream();
    }

    /**
     * 获取jar包外路径
     *
     * @param path
     * @return
     */
    public static String getJarOuterPath(String path) {
        ApplicationHome applicationHome = new ApplicationHome(FileUtils.class);
        File source = applicationHome.getSource();
        return source.getParentFile().getPath() + path;
    }

    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os       输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException {
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件
     * @return
     */
    public static boolean deleteFile(String filePath) {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 文件名称验证
     *
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename) {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * 下载文件名重新编码
     *
     * @param request  请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName)
            throws UnsupportedEncodingException {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE")) {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        } else if (agent.contains("Chrome")) {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        } else {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }


    /**
     * 获取文件扩展名(后缀)
     *
     * @param filename
     * @return
     */
    public static String getFileExtension(String filename) {
        if (!StringUtils.isEmpty(filename)) {
            String string = filename.trim();
            int index = filename.lastIndexOf(".");
            if (index > 0 && index < string.length() - 1) {
                return string.substring(index + 1);
            }
        }
        return null;
    }

    /**
     * 判断文件是否是图片
     */
    public static boolean isImage(File file) {
        if (!file.exists()) {
            return false;
        }
        BufferedImage image = null;
        try {
            //如果是spring中MultipartFile类型，则代码如下
            // image = ImageIO.read(file.getInputStream());
            image = ImageIO.read(file);
            if (image == null || image.getWidth() <= 0 || image.getHeight() <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
