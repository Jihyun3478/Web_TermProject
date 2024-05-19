package web.termproject.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    private static String uploadDirectory = System.getProperty("user.dir") + "/img";
    @Override
    public Resource getImage(String imagePath) throws MalformedURLException {
        Path filePath = Paths.get(uploadDirectory).resolve(imagePath).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
            return resource;
        } else {
            throw new RuntimeException("File not found " + imagePath);
        }
    }

    @Override
    public Map<String, String> uploadImage(MultipartFile file, String imagePath) throws IOException {
        // 파일이 비어있는지 확인
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // 업로드 디렉토리 생성
        Path uploadPath = Paths.get(uploadDirectory + "/" + imagePath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 파일 저장
        String originalFileName = file.getOriginalFilename();
        String fileName = generateUniqueFileName(uploadPath, originalFileName);
        Path targetLocation = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation);

        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("url", "http://localhost:8080/image/display?imagePath=" + imagePath + "/" + fileName);
        return jsonMap;
    }

    private String generateUniqueFileName(Path uploadPath, String originalFileName) {
        String fileName = originalFileName;
        while (Files.exists(uploadPath.resolve(fileName)))
            fileName = UUID.randomUUID() + "-" + originalFileName;
        return fileName;
    }
}
