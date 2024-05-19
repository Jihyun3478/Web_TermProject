package web.termproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import web.termproject.domain.entity.ApplyMember;
import web.termproject.domain.status.ApplyMemberStatus;
import web.termproject.repository.ApplyMemberRepository;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class ApplyMemberServiceImpl implements ApplyMemberService {
    private final ApplyMemberRepository applyMemberRepository;
    private static String uploadDirectory = System.getProperty("user.dir") + "/applyMember";

    @Override
    public ResponseEntity<Resource> downloadImage(String applyFilePath, Long clubId) throws MalformedURLException {
        Path filePath = Paths.get(uploadDirectory + "/club/" + clubId).resolve(applyFilePath).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        // 파일이 존재하는지 확인
        if (!resource.exists()) {
            throw new RuntimeException("File not found " + applyFilePath);
        }

        // 다운로드할 파일의 MIME 타입 설정
        String contentType;
        try {
            contentType = Files.probeContentType(filePath);
        } catch (IOException e) {
            contentType = "application/octet-stream";
        }

        // 다운로드할 파일의 응답 헤더 설정
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @Override
    public ResponseEntity<String> uploadFiles(MultipartFile file, Long clubId, Long memberId) {
        try {
            // 업로드할 디렉토리에 파일 저장
            String fileName = file.getOriginalFilename();
            String directoryPath = uploadDirectory + "/request/club/" + clubId + "/" + memberId;
            File directory = new File(directoryPath);
            if(!directory.exists())
                directory.mkdirs();
            File destFile = new File(directoryPath + "/" + fileName);
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to upload files.");
        }
        if(!applyMemberRepository.existsApplyMemberByMemberId(memberId)) {
            ApplyMember applyMember = ApplyMember.builder()
                    .member(applyMemberRepository.findMemberByMemberId(memberId))
                    .applyMemberStatus(ApplyMemberStatus.NOT_CLUB_MEMBER)
                    .club(applyMemberRepository.findClubByClubId(clubId))
                    .build();
            applyMemberRepository.save(applyMember);
        }
        return ResponseEntity.ok("Files uploaded successfully.");
    }
}
