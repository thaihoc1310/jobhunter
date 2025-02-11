package vn.thaihoc.jobhunter.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.thaihoc.jobhunter.domain.file.ResUploadFileDTO;
import vn.thaihoc.jobhunter.service.FileService;
import vn.thaihoc.jobhunter.util.annotation.ApiMessage;
import vn.thaihoc.jobhunter.util.error.StorageException;

@RestController
@RequestMapping("/api/v1")
public class FileController {
    private final FileService fileService;
    @Value("${thaihoc.upload-file.base-uri}")
    private String baseURI;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @ApiMessage("Upload single file")
    public ResponseEntity<ResUploadFileDTO> upload(@RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException {
        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(fileName.toLowerCase()::endsWith);
        if (!isValid) {
            throw new StorageException("File extension is not allowed");
        }
        this.fileService.createDirectory(baseURI + folder);
        String uploadFile = this.fileService.store(file, folder);
        ResUploadFileDTO resUploadFileDTO = new ResUploadFileDTO(
                URLDecoder.decode(uploadFile, StandardCharsets.UTF_8.toString()), Instant.now());
        return ResponseEntity.ok().body(resUploadFileDTO);

    }
}
