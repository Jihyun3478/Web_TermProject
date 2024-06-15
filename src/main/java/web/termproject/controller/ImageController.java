package web.termproject.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import web.termproject.service.ImageService;

@RestController
@RequestMapping({"/image"})
public class ImageController {
    private final ImageService imageService;

    @GetMapping(
            value = {"/display"},
            produces = {"image/jpeg"}
    )
    public Resource getImage(String imagePath) throws MalformedURLException {
        return this.imageService.getImage(imagePath);
    }

    @PostMapping(
            value = {"/upload"},
            consumes = {"multipart/form-data"},
            produces = {"application/json"}
    )
    public Map<String, String> uploadImage(@RequestParam("imageFile") MultipartFile file, @RequestParam("imagePath") String imagePath) throws IOException {
        return this.imageService.uploadImage(file, imagePath);
    }

    public ImageController(final ImageService imageService) {
        this.imageService = imageService;
    }
}