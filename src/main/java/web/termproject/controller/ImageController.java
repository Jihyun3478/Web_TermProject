package web.termproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.termproject.service.ImageService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping(value = "/display", produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource getImage(String imagePath) throws MalformedURLException {
        return imageService.getImage(imagePath);
    }

    @PostMapping(value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> uploadImage(@RequestParam("imageFile") MultipartFile file, @RequestParam("imagePath") String imagePath) throws IOException {
        return imageService.uploadImage(file, imagePath);
    }
}
