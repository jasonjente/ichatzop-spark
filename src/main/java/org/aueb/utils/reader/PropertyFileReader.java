package org.aueb.utils.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyFileReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyFileReader.class);
    public static void main(String[] args) {
        readFile();
    }
    private static final Map<String, String> properties = new HashMap<>();
    static {
        readFile();
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getOrDefault(key, defaultValue);
    }

    private static void readFile(){
        LOGGER.info("ReadFile - Enter");
        FileInputStream fis = null;
        try {
            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            String appConfigPath = rootPath + "application.properties";
            LOGGER.info("searching for property file");
            Properties appProps = new Properties();
            fis = new FileInputStream(appConfigPath);
            appProps.load(fis);

            for (String key: appProps.stringPropertyNames()){
                String value = appProps.getProperty(key);
                properties.put(key, value);
                LOGGER.info("Inserting key: {} - value: {} ", key, value);
            }
            fis.close();
        } catch (Exception e){
            LOGGER.error("Error loading prop file", e);
        }
    }
}
