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
import java.util.Set;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final FileService fileService;

    public ImageServiceImpl(ImageRepository imageRepository, FileService fileService) {
        this.imageRepository = imageRepository;
        this.fileService = fileService;
    }

    //Delete all images from both database and disk
    @Override
    public void deleteAll(Set<Image> images) {
        images.forEach(image -> {
            //Delete image file from disk
            try {
                fileService.deleteImage(image);
            } catch (IOException e) {
                System.out.println("Could not delete image: " + image.getPath());
            }

            //Delete image entity from database
            imageRepository.delete(image);
        });
    }

    //Save uploaded images for offer
    @Override
    public void saveImages(List<MultipartFile> images, Offer offer, UserEntity user) throws IOException, IllegalArgumentException {
        //Check if there are images
        if (images.isEmpty()) throw new IllegalArgumentException("No images selected");

        for (MultipartFile image : images) {
            Image imageEntity = new Image();

            //Create image entity
            imageEntity.setOffer(offer);
            imageEntity.setUploadedBy(user);
            imageEntity.setType(fileService.getImageType(Objects.requireNonNull(image.getOriginalFilename())));

            //Save to database to generate id, because the id is needed by the file service for path formatting
            imageEntity = imageRepository.save(imageEntity);

            //Get formatted path from the file service
            imageEntity.setPath(fileService.formatImagePath(imageEntity, image.getOriginalFilename()));

            //Save image to disk
            fileService.saveImageToDisk(imageEntity, image.getBytes());

            //Save image entity to database
            imageRepository.save(imageEntity);
        }
    }
}
