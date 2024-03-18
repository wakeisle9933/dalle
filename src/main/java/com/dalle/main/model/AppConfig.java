package com.dalle.main.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {

  private static final String PROPERTIES_FILE = "config.properties";

  public static String getApiCode() {
    try (FileInputStream input = new FileInputStream(PROPERTIES_FILE)) {
      Properties prop = new Properties();
      prop.load(input);
      if(prop.getProperty("apiCode") == null || prop.getProperty("apiCode").isEmpty()) {
        return null;
      } else {
        return prop.getProperty("apiCode");
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static void setApiCode(String newApiCode) {
    Properties prop = new Properties();
    try (FileInputStream in = new FileInputStream(PROPERTIES_FILE)) {
      prop.load(in);
    } catch (IOException e) {
      e.printStackTrace();
    }
    // folder 값을 업데이트합니다.
    prop.setProperty("apiCode", newApiCode);

    // 변경된 프로퍼티를 다시 파일에 저장합니다.
    try (FileOutputStream output = new FileOutputStream(PROPERTIES_FILE)) {
      prop.store(output, null);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public static File getFolder() {
    try (FileInputStream input = new FileInputStream(PROPERTIES_FILE)) {
      Properties prop = new Properties();
      prop.load(input);
      if(prop.getProperty("folder") == null || prop.getProperty("folder").isEmpty()) {
        return new File(System.getProperty("user.home") + "/Desktop");
      } else {
        return new File(prop.getProperty("folder"));
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return new File(System.getProperty("user.home") + "/Desktop");
  }

  public static void setFolder(File newFolderLocation) {
    Properties prop = new Properties();
    try (FileInputStream in = new FileInputStream(PROPERTIES_FILE)) {
      prop.load(in);
    } catch (IOException e) {
      e.printStackTrace();
    }
    // folder 값을 업데이트합니다.
    prop.setProperty("folder", newFolderLocation.getAbsolutePath());

    // 변경된 프로퍼티를 다시 파일에 저장합니다.
    try (FileOutputStream output = new FileOutputStream(PROPERTIES_FILE)) {
      prop.store(output, null);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

}
