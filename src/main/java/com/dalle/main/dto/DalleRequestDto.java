package com.dalle.main.dto;

import com.dalle.main.model.DalleModel;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;

public class DalleRequestDto {

  private DalleModel dalleModel;
  private String prompt;
  private String size;
  private ImageView createdImage;
  private ProgressIndicator loadingIndicator;

  private DalleRequestDto(Builder builder) {
    this.dalleModel = builder.dalleModel;
    this.prompt = builder.prompt;
    this.size = builder.size;
    this.createdImage = builder.createdImage;
    this.loadingIndicator = builder.loadingIndicator;
  }

  // Getter methods
  public DalleModel getDalleModel() {
    return dalleModel;
  }

  public String getPrompt() {
    return prompt;
  }

  public String getSize() {
    return size;
  }

  public ImageView getCreatedImage() {
    return createdImage;
  }

  public ProgressIndicator getLoadingIndicator() {
    return loadingIndicator;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private DalleModel dalleModel;
    private String prompt;
    private String size;
    private ImageView createdImage;
    private ProgressIndicator loadingIndicator;

    public Builder setDalleModel(DalleModel dalleModel) {
      this.dalleModel = dalleModel;
      return this;
    }

    public Builder setPrompt(String prompt) {
      this.prompt = prompt;
      return this;
    }

    public Builder setSize(String size) {
      this.size = size;
      return this;
    }

    public Builder setCreatedImage(ImageView createdImage) {
      this.createdImage = createdImage;
      return this;
    }

    public Builder setLoadingIndicator(ProgressIndicator loadingIndicator) {
      this.loadingIndicator = loadingIndicator;
      return this;
    }

    public DalleRequestDto build() {
      return new DalleRequestDto(this);
    }
  }

}
