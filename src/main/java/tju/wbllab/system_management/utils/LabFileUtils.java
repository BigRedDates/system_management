package tju.wbllab.system_management.utils;


import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class LabFileUtils {
    private final static Logger logger = LoggerFactory.getLogger(LabFileUtils.class);
    /**
     * 读取配置文件
     */
        public static   String  readConfigurationFile(String path){
            String str;
            File configFile = new File(path);
            try {
                 str = FileUtils.readFileToString(configFile,"UTF-8");
            } catch (IOException e) {
                logger.error("配置文件读取错误", e);
                return null;
            }
            return   str;
        }





}
