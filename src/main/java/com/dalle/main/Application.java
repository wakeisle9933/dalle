package com.dalle.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 940, 540); // 크기 조절
    stage.getIcons().add(new Image("file:src/main/resources/images/dalle.png"));
    stage.setTitle("DALL·E Image Generator by My Codegate");
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}