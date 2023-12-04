package bg.pazar.pazarbg.service;

import bg.pazar.pazarbg.model.entity.Image;
import bg.pazar.pazarbg.model.entity.Offer;
import bg.pazar.pazarbg.model.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ImageService {
    void deleteAll(Set<Image> images);

    void saveImages(List<MultipartFile> images, Offer offer, UserEntity user) throws IOException, IllegalArgumentException;
}
