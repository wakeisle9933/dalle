package com.dalle.main.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class AppConfig {

  private static final String PROPERTIES_FILE = "config.properties";

  private static Properties loadProperties() throws IOException {
    Properties prop = new Properties();
    Path configPath = getConfigPath();
    if (Files.exists(configPath)) {
      try (InputStream input = Files.newInputStream(configPath)) {
        prop.load(input);
      }
    } else {
      try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
        if (input != null) {
          prop.load(input);
          saveProperties(prop);
        } else {
          // config.properties 파일이 없는 경우, 기본 속성을 설정합니다.
          prop.setProperty("apiCode", "");
          prop.setProperty("folder", System.getProperty("user.home") + "/Desktop");
          saveProperties(prop);
        }
      }
    }
    return prop;
  }

  private static void saveProperties(Properties prop) throws IOException {
    Path configPath = getConfigPath();
    Path parentPath = configPath.getParent();
    if (parentPath != null) {
      Files.createDirectories(parentPath);
    }
    try (FileOutputStream output = new FileOutputStream(configPath.toFile())) {
      prop.store(output, null);
    }
  }

  private static Path getConfigPath() {
    String userHome = System.getProperty("user.home");
    Path configPath = Paths.get(userHome, "AppData", "Roaming", "Dalle", PROPERTIES_FILE);
    return configPath;
  }

  public static String getApiCode() {
    try {
      Properties prop = loadProperties();
      return prop.getProperty("apiCode", "");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return "";
  }

  public static void setApiCode(String newApiCode) {
    try {
      Properties prop = loadProperties();
      prop.setProperty("apiCode", newApiCode);
      saveProperties(prop);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public static File getFolder() {
    try {
      Properties prop = loadProperties();
      return new File(prop.getProperty("folder", System.getProperty("user.home") + "/Desktop"));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return new File(System.getProperty("user.home") + "/Desktop");
  }

  public static void setFolder(File newFolderLocation) {
    try {
      Properties prop = loadProperties();
      prop.setProperty("folder", newFolderLocation.getAbsolutePath());
      saveProperties(prop);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

}
