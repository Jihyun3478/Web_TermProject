package web.termproject.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;

public interface ApplyMemberService {
    public ResponseEntity<Resource> downloadImage(String applyFilePath, Long clubId) throws MalformedURLException;
    public ResponseEntity<String> uploadFiles(MultipartFile file, Long clubId, Long memberId);
}
