package bg.pazar.pazarbg.service;

import bg.pazar.pazarbg.model.entity.Image;
import bg.pazar.pazarbg.model.entity.UserEntity;
import bg.pazar.pazarbg.model.enums.ImageType;

import java.io.IOException;

public interface FileService {
    void saveImageToDisk(Image image, byte[] bytes) throws IOException;


    boolean deleteImage(Image image) throws IOException;

    String formatImagePath(Image image, String filename) throws IllegalArgumentException;

    ImageType getImageType(String filename) throws IllegalArgumentException;
}
