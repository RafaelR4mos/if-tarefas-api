package com.br.todo.controller;

import com.br.todo.dto.image.ImageDTO;
import com.br.todo.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/task/{idTask}")
    public ResponseEntity<ImageDTO> uploadImageToTask(@PathVariable Integer idTask, @RequestPart("image") MultipartFile image) throws IOException {
        return new ResponseEntity<>(imageService.UploadImageToTask(idTask, image), HttpStatus.CREATED);
    }

    @DeleteMapping("/{idImage}")
    public ResponseEntity<?> deleteImage(@PathVariable Integer idImage, @RequestHeader("Authorization") String authHeader) {
        imageService.removeImageFromTask(idImage, authHeader);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
