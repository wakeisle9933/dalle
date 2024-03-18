package com.dalle.main.utils;

import com.dalle.main.model.AppConfig;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

public class ImageUtils {

  public static void saveImage(ImageView createdImage) {
    Image image = createdImage.getImage();
    if (image != null) {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save Image");
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
      fileChooser.setInitialDirectory(AppConfig.getFolder());
      String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
      String defaultFileName = "generated_image_" + timeStamp + ".png";
      fileChooser.setInitialFileName(defaultFileName); // 기본 파일 이름 설정
      File file = fileChooser.showSaveDialog(createdImage.getScene().getWindow());
      if (file != null) {
        try {
          BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
          ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static void copyImageToClipboard(Image image) {
    if (image != null) {
      Clipboard clipboard = Clipboard.getSystemClipboard();
      ClipboardContent content = new ClipboardContent();
      ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
      BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
      try {
        ImageIO.write(bufferedImage, "png", byteOutput);
        byte[] imageBytes = byteOutput.toByteArray();
        ByteArrayInputStream byteInput = new ByteArrayInputStream(imageBytes);
        content.putImage(new Image(byteInput));
        clipboard.setContent(content);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
