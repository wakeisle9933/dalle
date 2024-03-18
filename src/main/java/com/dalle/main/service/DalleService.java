package com.dalle.main.service;

import com.dalle.main.dto.DalleRequestDto;

public interface DalleService {

  void onCreateImageClick(DalleRequestDto dalleRequestDto);
  void onSetImageFolder();
  void onMoveFolderClick();
  void onInputApiClick();

}
