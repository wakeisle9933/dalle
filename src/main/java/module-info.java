module com.example.demo {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;
  requires java.net.http;
  requires com.google.gson;
  requires javafx.swing;

  opens com.dalle.main to javafx.fxml;
  exports com.dalle.main;
  exports com.dalle.main.model;
  opens com.dalle.main.model to javafx.fxml;
  exports com.dalle.main.controller;
  opens com.dalle.main.controller to javafx.fxml;
}