package com.dalle.main.serviceImpl;

import com.dalle.main.dto.DalleRequestDto;
import com.dalle.main.model.AppConfig;
import com.dalle.main.service.DalleService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class DalleServiceImpl implements DalleService {

  public void onCreateImageClick(DalleRequestDto dalleRequestDto) {
    // 모델, 프롬프트, 해상도 중 하나라도 선택되지 않았을 경우 처리
    if (dalleRequestDto.getDalleModel() == null ||
        dalleRequestDto.getPrompt().isEmpty() ||
        dalleRequestDto.getSize() == null) {
      Platform.runLater(() -> {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Please Make sure you've selected a Model, Resolution, and entered an API Key");
        alert.showAndWait();
      });
      return;
    }

    String model = dalleRequestDto.getDalleModel().getName();
    String quality = dalleRequestDto.getDalleModel().getQuality();
    String prompt = dalleRequestDto.getPrompt();
    String size = dalleRequestDto.getSize();
    String requestBody;

    if(quality.isEmpty()) {
      requestBody = String.format(
          "{\"model\": \"%s\", \"prompt\": \"%s\", \"n\": 1, \"size\": \"%s\"}",
          model, prompt, size
      );
    } else {
      requestBody = String.format(
          "{\"model\": \"%s\", \"prompt\": \"%s\", \"n\": 1, \"size\": \"%s\" , \"quality\": \"%s\"}",
          model, prompt, size, quality
      );
    }

    HttpRequest request;
    try {
      request = HttpRequest.newBuilder()
          .uri(new URI("https://api.openai.com/v1/images/generations"))
          .header("Content-Type", "application/json")
          .header("Authorization", "Bearer " + AppConfig.getApiCode())
          .POST(HttpRequest.BodyPublishers.ofString(requestBody))
          .build();
    } catch (URISyntaxException e) {
      e.printStackTrace();
      return;
    }

    dalleRequestDto.getLoadingIndicator().setManaged(true);// 로딩 인디케이터 표시
    dalleRequestDto.getCreatedImage().setManaged(false);
    dalleRequestDto.getCreatedImage().setVisible(false);
    dalleRequestDto.getLoadingIndicator().setVisible(true);

    HttpClient client = HttpClient.newHttpClient();
    client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenApply(responseBody -> {
          Gson gson = new Gson();
          JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
          JsonArray dataArray = jsonObject.getAsJsonArray("data");
          return dataArray.get(0).getAsJsonObject().get("url").getAsString();
        })
        .thenAccept(imageUrl -> {
          Image image = new Image(imageUrl);
          dalleRequestDto.getCreatedImage().setImage(image);
          dalleRequestDto.getLoadingIndicator().setVisible(false);
          dalleRequestDto.getCreatedImage().setManaged(true);
          dalleRequestDto.getCreatedImage().setVisible(true);
          // 이미지 저장
          String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
          String fileName = "generated_image_" + timeStamp + ".png";
          File file = new File(AppConfig.getFolder(), fileName);
          try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
          } catch (IOException e) {
            e.printStackTrace();
          }
        })
        .exceptionally(ex -> {
          ex.printStackTrace();
          return null;
        })
        .whenComplete((result, ex) -> dalleRequestDto.getLoadingIndicator().setManaged(false)); // 로딩 인디케이터 숨김
  }

  @FXML
  public void onSetImageFolder() {
    // 폴더 선택 대화상자 생성
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(AppConfig.getFolder()); // 현재 폴더 설정
    fileChooser.setDialogTitle("이미지 폴더 선택"); // 대화상자 제목 설정
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // 폴더만 선택 가능하도록 설정

    // 폴더 선택 대화상자 표시
    int result = fileChooser.showOpenDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      // 선택한 폴더로 static 변수 업데이트
      AppConfig.setFolder(fileChooser.getSelectedFile());
    }
  }

  @FXML
  public void onMoveFolderClick() {
    try {
      if (AppConfig.getFolder().exists() && AppConfig.getFolder().isDirectory()) {
        Desktop.getDesktop().browse(AppConfig.getFolder().toURI());
      } else {
        System.out.println("Folder does not exist.");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void onInputApiClick() {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Input OpenAI API Code");
    dialog.setHeaderText(null);
    dialog.setContentText("Please enter your OpenAI API code:");

    if(AppConfig.getApiCode() != null) {
      dialog.getEditor().setText(AppConfig.getApiCode());
    }

    Optional<String> result = dialog.showAndWait();
    result.ifPresent(AppConfig::setApiCode);
  }

}
