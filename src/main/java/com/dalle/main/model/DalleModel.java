package com.dalle.main.model;

import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DalleModel {
  private String name;
  private String quality;
  private double price;

  private static final Map<String, ObservableList<String>> MODEL_RESOLUTIONS = new HashMap<>();

  static {
    MODEL_RESOLUTIONS.put("dall-e-3", FXCollections.observableArrayList(
        "1024x1024",
        "1024x1792", "1792x1024"
    ));
    MODEL_RESOLUTIONS.put("dall-e-2", FXCollections.observableArrayList(
        "1024x1024",
        "512x512",
        "256x256"
    ));
  }

  public static ObservableList<String> getResolutions(String modelName) {
    return MODEL_RESOLUTIONS.get(modelName);
  }

  public DalleModel(String name, String quality, double price) {
    this.name = name;
    this.quality = quality;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public String getQuality() {
    return quality;
  }

  public double getPrice() {
    return price;
  }

  @Override
  public String toString() {
    return String.format("%s - %s - $%.3f / image", name, quality, price);
  }
}
