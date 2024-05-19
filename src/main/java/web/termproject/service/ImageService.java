package web.termproject.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

public interface ImageService {
    Resource getImage(String imagePath) throws MalformedURLException;
    Map<String, String> uploadImage(MultipartFile file, String imagePath) throws IOException;
}
