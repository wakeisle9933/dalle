package com.dalle.main.utils;

import java.io.IOException;
import java.util.logging.*;

public class LogUtils {

  private static final Logger logger = Logger.getLogger(LogUtils.class.getName());

  static {
    setupLogger();
  }

  public static void setupLogger() {
    try {
      // 사용자의 AppData\Roaming\Dalle 폴더에 로그 파일을 생성합니다.
      String userHome = System.getProperty("user.home");
      String logPath = userHome + "\\AppData\\Roaming\\Dalle\\dalle.log";
      FileHandler fileHandler = new FileHandler(logPath, true);
      SimpleFormatter simpleFormatter = new SimpleFormatter();
      fileHandler.setFormatter(simpleFormatter);
      logger.addHandler(fileHandler);
      logger.setLevel(Level.ALL);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "File logger not working.", e);
    }
  }
  public static void logException(Exception e) {
    logger.log(Level.SEVERE, e.getMessage(), e);
  }

  public static void writeLog(String message) {
    logger.log(Level.SEVERE, message);
  }

}
