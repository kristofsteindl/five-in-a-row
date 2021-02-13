package com.ksteindl.fiveinarow.components;

import com.ksteindl.fiveinarow.App;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyService {

    private static PropertyService INSTANCE;
    private static final Logger logger = LogManager.getLogger(PropertyService.class);

    public static PropertyService getInstance() {
        if (INSTANCE == null) {
            synchronized (PropertyService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PropertyService();
                }
            }
        }
        return INSTANCE;
    }

    private Properties mergedProperties;
    private Map<String, Integer> intProperties = new HashMap<>();

    private PropertyService() {
        mergedProperties = initGameProperties();
    }

    public String get(String key) {
        return mergedProperties.getProperty(key);
    }

    public Integer getInt(String key) {
        return intProperties.get(key);
    }

    private Properties initGameProperties() {
        Properties businessLogicProperties = getBundledProperties("/business-logic.properties");
        Properties defaultProperties = getBundledProperties("/default-fiar.properties");
        Properties customProperties = getCustomProperties();
        loadBundledIntProperties(businessLogicProperties);
        loadBundledIntProperties(defaultProperties);
        loadCustomIntProperties(customProperties);
        return overloadProperties(businessLogicProperties, defaultProperties, customProperties);
    }

    private Properties overloadProperties(Properties... propertieses) {
        Properties mergedProperties = new Properties();
        for (Properties properties : propertieses) {
            mergedProperties.putAll(properties);
        }
        return mergedProperties;
    }

    private Properties getBundledProperties(String pathOfBundledProperties) {
        Properties bundeldProperties = new Properties();
        try (InputStream input = App.class.getResourceAsStream(pathOfBundledProperties)) {
            bundeldProperties.load(input);
        } catch (IOException exception) {
            logger.error("IOException was thown in SettingService initialization", exception);
            throw new RuntimeException(exception);
        }
        return bundeldProperties;
    }

    private Properties getCustomProperties() {
        Properties customProperties = new Properties();
        try (var customPropsInputStream = new FileInputStream("custom-fiar.properties")) {
            customProperties.load(customPropsInputStream);
        } catch (IOException exception) {
            logger.info("custom-fiar.properties was not found, default properties will be applied");
        }
        return customProperties;
    }

    private void loadCustomIntProperties(Properties properties) {
        for (String key : properties.stringPropertyNames()) {
            try {
                Integer intValue = Integer.parseInt(properties.getProperty(key));
                if (key.equals("tableHeight") || key.equals("tableWidth")) {
                    int maxDimension = intProperties.get("maxDimension");
                    if (intValue <= maxDimension) {
                        intProperties.put(key, intValue);
                    } else {
                        logger.error(String.format("%s should be maximum %s, falling back to default (%s)", key, maxDimension, intProperties.get(key)));
                    }
                } else {
                    intProperties.put(key, intValue);
                }
            } catch (NumberFormatException exception) {
                logger.info(String.format("For key %s, value %s can not be parsed to int.", key, properties.getProperty(key)));
            }
        }
    }

    private void loadBundledIntProperties(Properties properties) {
        for (String key : properties.stringPropertyNames()) {
            try {
                Integer intValue = Integer.parseInt(properties.getProperty(key));
                intProperties.put(key, intValue);
            } catch (NumberFormatException exception) {
                logger.trace("Automatic attempt failed to parse value to int");
            }
        }
    }
}
