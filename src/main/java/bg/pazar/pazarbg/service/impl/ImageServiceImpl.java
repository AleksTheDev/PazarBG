package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ImageServiceImpl implements ImageService {
    public static final String BASE_IMAGES_PATH = "./userImages/";
    private final AuthenticationService authenticationService;

    public ImageServiceImpl(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public List<String> saveImages(List<MultipartFile> images, Long offerID) throws IOException, IllegalArgumentException {
        if (images.isEmpty()) throw new IllegalArgumentException("No images selected");

        Long userID = authenticationService.getCurrentUserId();
        List<String> imagePaths = new ArrayList<>();

        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);

            String pathToSave = formatImagePath(userID, offerID, i, Objects.requireNonNull(image.getOriginalFilename()));

            File file = new File(pathToSave);
            file.getParentFile().mkdirs();
            file.createNewFile();

            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(image.getBytes());

            imagePaths.add(pathToSave);
        }

        return imagePaths;
    }

    private String formatImagePath(Long userID, Long offerID, int index, String filename) throws IllegalArgumentException {
        String[] filenameSplit = filename.split("\\.");
        String extension = filenameSplit[filenameSplit.length - 1];

        if (!(extension.equals("jpg") || extension.equals("jpeg"))) {
            throw new IllegalArgumentException("Unsupported image type");
        }

        return BASE_IMAGES_PATH + userID + "/" + offerID + "/" + index + "." + extension;
    }
}
