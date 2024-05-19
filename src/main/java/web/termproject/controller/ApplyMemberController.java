package web.termproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.termproject.service.ApplyMemberService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/applyMember")
@RequiredArgsConstructor
public class ApplyMemberController {
    private final ApplyMemberService applyMemberService;

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadImage(@RequestParam("applyFilePath") String applyFilePath, @RequestParam("clubId") Long clubId) throws MalformedURLException {
        return applyMemberService.downloadImage(applyFilePath, clubId);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadFiles(@RequestParam("files") MultipartFile file, @RequestParam("clubId") Long clubId, @RequestParam("memberId") Long memberId) {
        return applyMemberService.uploadFiles(file, clubId, memberId);
    }
}
