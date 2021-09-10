package org.ks365.osmp.common.config;

import io.minio.MinioClient;
import org.ks365.osmp.common.entity.MinioProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * minIo配置
 *
 * @author tianslc
 */
@EnableCaching
@Configuration
public class MinioConfig {
    private final MinioProperties minioProperties;

    public MinioConfig(MinioProperties minioProperties) {
        this.minioProperties = minioProperties;
    }

    /**
     * 初始化minio
     *
     * @return
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder().endpoint(minioProperties.getUrl(), minioProperties.getPort(), false).credentials(minioProperties.getUsername(), minioProperties.getPassword()).build();
    }
}
