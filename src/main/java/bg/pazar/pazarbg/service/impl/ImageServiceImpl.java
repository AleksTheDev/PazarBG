package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.model.entity.Image;
import bg.pazar.pazarbg.model.entity.Offer;
import bg.pazar.pazarbg.model.entity.UserEntity;
import bg.pazar.pazarbg.model.enums.ImageType;
import bg.pazar.pazarbg.repo.ImageRepository;
import bg.pazar.pazarbg.repo.OfferRepository;
import bg.pazar.pazarbg.repo.UserRepository;
import bg.pazar.pazarbg.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

@Service
public class ImageServiceImpl implements ImageService {
    public static final String BASE_IMAGES_PATH = "./userImages/";
    private final ImageRepository imageRepository;

    public ImageServiceImpl(AuthenticationService authenticationService, UserRepository userRepository, OfferRepository offerRepository, ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public void saveImages(List<MultipartFile> images, Offer offer, UserEntity user) throws IOException, IllegalArgumentException {
        if (images.isEmpty()) throw new IllegalArgumentException("No images selected");

        for (MultipartFile image : images) {
            Image imageEntity = new Image();

            imageEntity.setOffer(offer);
            imageEntity.setUploadedBy(user);
            imageEntity.setType(getImageType(Objects.requireNonNull(image.getOriginalFilename())));

            imageEntity = imageRepository.save(imageEntity);

            String pathToSave = formatImagePath(user.getId(), offer.getId(), imageEntity.getId(), Objects.requireNonNull(image.getOriginalFilename()));

            imageEntity.setPath(pathToSave);

            File file = new File(pathToSave);
            file.getParentFile().mkdirs();
            file.createNewFile();

            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(image.getBytes());

            imageRepository.save(imageEntity);
        }
    }

    private String formatImagePath(Long userID, Long offerID, Long imageID, String filename) throws IllegalArgumentException {
        String[] filenameSplit = filename.split("\\.");
        String extension = filenameSplit[filenameSplit.length - 1];

        if (!(extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png"))) {
            throw new IllegalArgumentException("Unsupported image type");
        }

        return BASE_IMAGES_PATH + userID + "/" + offerID + "/" + imageID + "." + extension;
    }

    private ImageType getImageType(String filename) throws IllegalArgumentException {
        String[] filenameSplit = filename.split("\\.");
        String extension = filenameSplit[filenameSplit.length - 1];

        if (extension.equals("jpg") || extension.equals("jpeg")) {
            return ImageType.JPEG;
        } else if(extension.equals("png")) {
            return ImageType.PNG;
        } else {
            throw new IllegalArgumentException("Unsupported image type");
        }
    }
}
