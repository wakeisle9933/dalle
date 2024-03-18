package com.dalle.main.controller;

import com.dalle.main.dto.DalleRequestDto;
import com.dalle.main.model.DalleModel;
import com.dalle.main.serviceImpl.DalleServiceImpl;
import com.dalle.main.utils.ImageUtils;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DalleController implements Initializable {

  @FXML
  private TextArea promptTextArea;

  @FXML
  private ImageView createdImage;

  @FXML
  private ComboBox<DalleModel> modelComboBox;

  @FXML
  private ComboBox<String> resolutionComboBox;

  @FXML
  private ProgressIndicator loadingIndicator;

  @FXML
  private Spinner<Integer> countSpinner;

  DalleServiceImpl dalleService = new DalleServiceImpl();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // 콤보박스에 DallE 모델 추가
    ObservableList<DalleModel> models = FXCollections.observableArrayList(
        new DalleModel("dall-e-3", "hd", 0.080),
        new DalleModel("dall-e-3", "standard", 0.040),
        new DalleModel("dall-e-2", "", 0.020)
    );
    modelComboBox.setItems(models);

    // 모델 콤보박스의 값 변경 이벤트 처리
    modelComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        resolutionComboBox.setItems(DalleModel.getResolutions(newValue.getName()));
      }
    });

    // 마우스 우클릭 시 클릭 메뉴 생성
    ContextMenu contextMenu = new ContextMenu();

    // 새 탭에서 열기
    MenuItem openInNewTabMenuItem = new MenuItem("Open in New Tab");
    openInNewTabMenuItem.setOnAction(event -> {
      Image image = createdImage.getImage();
      if (image != null) {
        String imageUrl = image.getUrl();
        try {
          Desktop.getDesktop().browse(new URI(imageUrl));
        } catch (IOException | URISyntaxException e) {
          e.printStackTrace();
        }
      }
    });
    contextMenu.getItems().add(openInNewTabMenuItem);

    // 이미지 복사
    MenuItem copyMenuItem = new MenuItem("Copy Image");
    copyMenuItem.setOnAction(event -> ImageUtils.copyImageToClipboard(createdImage.getImage()));
    contextMenu.getItems().add(copyMenuItem);

    // 이미지 저장
    MenuItem saveMenuItem = new MenuItem("Save Image");
    saveMenuItem.setOnAction(event -> ImageUtils.saveImage(createdImage));
    contextMenu.getItems().add(saveMenuItem);

    createdImage.setOnContextMenuRequested(event -> {
      contextMenu.show(createdImage, event.getScreenX(), event.getScreenY());
    });
  }

  @FXML
  protected void onCreateImageClick() {
    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    int count = countSpinner.getValue();
    for (int i = 0; i < count; i++) {
      executor.submit(() -> {
        dalleService.onCreateImageClick(DalleRequestDto.builder()
        .setDalleModel(modelComboBox.getValue())
        .setPrompt(promptTextArea.getText())
        .setSize(resolutionComboBox.getValue())
        .setCreatedImage(createdImage)
        .setLoadingIndicator(loadingIndicator)
        .build());
      });
    }
    executor.shutdown();
  }

  @FXML
  protected void onSetImageFolder() {
    dalleService.onSetImageFolder();
  }

  @FXML
  protected void onMoveFolderClick() {
    dalleService.onMoveFolderClick();
  }

  @FXML
  protected void onInputApiClick() {
    dalleService.onInputApiClick();
  }

  @FXML
  private void onGitHubLinkClick() {
    String link = "https://github.com/wakeisle9933";
    try {
      Desktop.getDesktop().browse(new URI(link));
    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
    }
  }

}