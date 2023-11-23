package bg.pazar.pazarbg.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    List<String> saveImages(List<MultipartFile> images, Long id) throws IOException, IllegalArgumentException;
}
