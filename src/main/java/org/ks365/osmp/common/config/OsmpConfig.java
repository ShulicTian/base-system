package org.ks365.osmp.common.config;

import java.io.File;
import java.util.Objects;

public class OsmpConfig {

    private static final String DOWNLOAD_PATH = File.separator + "templates" + File.separator + "import" + File.separator;

    public static String getDownloadPath() {
        return Objects.requireNonNull(OsmpConfig.class.getClassLoader().getResource(DOWNLOAD_PATH)).getPath();
    }

}
