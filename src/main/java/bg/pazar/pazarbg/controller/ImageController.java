package bg.pazar.pazarbg.controller;

import bg.pazar.pazarbg.exception.ImageNotFoundException;
import bg.pazar.pazarbg.model.entity.Image;
import bg.pazar.pazarbg.model.enums.ImageType;
import bg.pazar.pazarbg.repo.ImageRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.Files;
import java.nio.file.Path;

@Controller
public class ImageController {
    private final ImageRepository imageRepository;

    public ImageController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    //Get mapping for image
    @GetMapping(value = "/image/{id}")
    public ResponseEntity<byte[]> getImageWithID(@PathVariable("id") Long id) {
        try {
            Image image = imageRepository.findById(id).orElseThrow();
            byte[] bytes = Files.readAllBytes(Path.of(image.getPath()));

            if (image.getType().name().equals(ImageType.PNG.name())) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytes);
            } else {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
            }
        } catch (Exception e) {
            throw new ImageNotFoundException();
        }
    }
}
