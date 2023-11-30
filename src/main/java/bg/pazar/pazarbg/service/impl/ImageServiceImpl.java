package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.model.entity.Image;
import bg.pazar.pazarbg.model.entity.Offer;
import bg.pazar.pazarbg.model.entity.UserEntity;
import bg.pazar.pazarbg.repo.ImageRepository;
import bg.pazar.pazarbg.service.FileService;
import bg.pazar.pazarbg.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final FileService fileService;

    public ImageServiceImpl(ImageRepository imageRepository, FileService fileService) {
        this.imageRepository = imageRepository;
        this.fileService = fileService;
    }

    @Override
    public void saveImages(List<MultipartFile> images, Offer offer, UserEntity user) throws IOException, IllegalArgumentException {
        if (images.isEmpty()) throw new IllegalArgumentException("No images selected");

        for (MultipartFile image : images) {
            Image imageEntity = new Image();

            imageEntity.setOffer(offer);
            imageEntity.setUploadedBy(user);
            imageEntity.setType(fileService.getImageType(Objects.requireNonNull(image.getOriginalFilename())));

            imageEntity = imageRepository.save(imageEntity);

            imageEntity.setPath(fileService.formatImagePath(imageEntity, image.getOriginalFilename()));

            fileService.saveImageToDisk(imageEntity, image.getBytes());

            imageRepository.save(imageEntity);
        }
    }
}
