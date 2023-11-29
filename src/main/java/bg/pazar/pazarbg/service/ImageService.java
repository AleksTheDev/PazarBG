package bg.pazar.pazarbg.service;

import bg.pazar.pazarbg.model.entity.Offer;
import bg.pazar.pazarbg.model.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    void saveImages(List<MultipartFile> images, Offer offer, UserEntity user) throws IOException, IllegalArgumentException;
}
