package com.traffgun.acc.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UpdateCommentRequest {
    private String text;
    private List<MultipartFile> attachmentsToAdd;
    private List<Long> attachmentsToDelete;
}