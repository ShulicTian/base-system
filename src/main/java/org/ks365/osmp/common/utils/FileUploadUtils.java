package org.ks365.osmp.common.utils;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 文件上传工具类
 *
 * @author tianslc
 */
public class FileUploadUtils {
    private static final MinioClient minioClient = SpringContextHolder.getBean("minioClient");

    /**
     * 上传文件流
     *
     * @param bucketName  桶名称（根文件夹,只能为单级目录，且不能有特殊字符 例：static）
     * @param objectName  对象名称（文件全称，可以加目录 例：test/test.txt,不能以/开头）
     * @param inputStream 文件流
     * @param size        文件大小
     */
    public static void uploadFileByStream(String bucketName, String objectName, InputStream inputStream, long size) {
        if (createBucket(bucketName)) {
            try {
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream, size, -1)
                        .build());
            } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传本地文件（路径相同则会覆盖）
     *
     * @param bucketName 桶名称（根文件夹,只能为单级目录，且不能有特殊字符 例：static）
     * @param objectName 对象名称（文件全称，可以加目录 例：test/test.txt,不能以/开头）
     * @param path       文件全路径（例：/static/image/aaa.jpg）
     */
    public static void uploadFileByPath(String bucketName, String objectName, String path) {
        if (createBucket(bucketName) && getFileStream(bucketName, objectName) == null) {
            try {
                minioClient.uploadObject(
                        UploadObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .filename(path)
                                .build());
            } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件
     *
     * @param bucketName 桶名称（根文件夹,只能为单级目录，且不能有特殊字符 例：static）
     * @param objectName 对象名称（文件全称，可以加目录 例：test/test.txt,不能以/开头）
     */
    public static boolean removeFile(String bucketName, String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return true;
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 下载到本地文件
     *
     * @param bucketName 桶名称（根文件夹,只能为单级目录，且不能有特殊字符 例：static）
     * @param objectName 对象名称（文件全称，可以加目录 例：test/test.txt,不能以/开头）
     * @param fileName   本地文件名称（文件全称，可以加目录 例：test/test.txt,不能以/开头）
     */
    public static void downloadToLocalFile(String bucketName, String objectName, String fileName) {
        try {
            minioClient.downloadObject(
                    DownloadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .filename(fileName)
                            .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件流
     *
     * @param bucketName 桶名称（根文件夹,只能为单级目录，且不能有特殊字符 例：static）
     * @param objectName 对象名称（文件全称，可以加目录 例：test/test.txt,不能以/开头）
     */
    public static InputStream getFileStream(String bucketName, String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
        } catch (InvalidKeyException | NoSuchAlgorithmException | ErrorResponseException | InvalidResponseException | ServerException | InsufficientDataException | XmlParserException | InternalException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 下载文件
     *
     * @param bucketName 桶名称（根文件夹,只能为单级目录，且不能有特殊字符 例：static）
     * @param objectName 对象名称（文件全称，可以加目录 例：test/test.txt,不能以/开头）
     */
    public static void downloadFile(String bucketName, String objectName, HttpServletResponse response) {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()); BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream())) {
            int len;
            byte[] buf = new byte[1024];
            while ((len = stream.read(buf)) != -1) {
                bufferedOutputStream.write(buf, 0, len);
            }
            bufferedOutputStream.flush();
        } catch (InvalidKeyException | NoSuchAlgorithmException | ErrorResponseException | InvalidResponseException | ServerException | InsufficientDataException | XmlParserException | InternalException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件流（断点）
     *
     * @param bucketName 桶名称（根文件夹,只能为单级目录，且不能有特殊字符 例：static）
     * @param objectName 对象名称（文件全称，可以加目录 例：test/test.txt,不能以/开头）
     * @param offset     其实位置
     * @param length     长度
     */
    public static InputStream getOffsetFileStream(String bucketName, String objectName, long offset, long length) {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .offset(offset)
                        .length(length)
                        .build())) {
            return stream;
        } catch (InvalidKeyException | NoSuchAlgorithmException | ErrorResponseException | InvalidResponseException | ServerException | InsufficientDataException | XmlParserException | InternalException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件http url
     *
     * @param bucketName 桶名称（根文件夹,只能为单级目录，且不能有特殊字符 例：static）
     * @param objectName 对象名称（文件全称，可以加目录 例：test/test.txt,不能以/开头）
     * @param expiry     有效期（秒）
     */
    public static String getFileHttpUrl(String bucketName, String objectName, int expiry) {
        try {
            String key = Base64.getUrlEncoder().encodeToString((bucketName + objectName).getBytes());
            String url = RedisUtils.get(key);
            if (StringUtils.isEmpty(url)) {
                url = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(bucketName)
                                .object(objectName)
                                .expiry(expiry)
                                .build());
                RedisUtils.set(key, url, expiry);
            }
            return url;
        } catch (ErrorResponseException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException | ServerException | InsufficientDataException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建桶
     *
     * @param bucketName 桶名称（根文件夹,只能为单级目录，且不能有特殊字符 例：static）
     * @return
     */
    private static boolean createBucket(String bucketName) {
        try {
            boolean flag = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!flag) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            return true;
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            e.printStackTrace();
        }
        return false;
    }
}
