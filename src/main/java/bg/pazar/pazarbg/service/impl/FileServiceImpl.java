package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.model.entity.Image;
import bg.pazar.pazarbg.model.entity.UserEntity;
import bg.pazar.pazarbg.model.enums.ImageType;
import bg.pazar.pazarbg.service.FileService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class FileServiceImpl implements FileService {
    public static final String BASE_IMAGES_PATH = "./userImages/";

    @Override
    public void saveImageToDisk(Image image, byte[] bytes) throws IOException {
        File file = new File(image.getPath());
        file.getParentFile().mkdirs();
        file.createNewFile();

        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
        outputStream.close();
    }

    @Override
    public void deleteUserImages(UserEntity user) throws IOException {
        File directory = new File(BASE_IMAGES_PATH + user.getId());
        FileUtils.deleteDirectory(directory);
    }

    @Override
    public String formatImagePath(Image image, String filename) throws IllegalArgumentException {
        String[] filenameSplit = filename.split("\\.");
        String extension = filenameSplit[filenameSplit.length - 1];

        if (!(extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png"))) {
            throw new IllegalArgumentException("Unsupported image type");
        }

        return BASE_IMAGES_PATH + image.getUploadedBy().getId() + "/" + image.getOffer().getId() + "/" + image.getId() + "." + extension;
    }

    @Override
    public ImageType getImageType(String filename) throws IllegalArgumentException {
        String[] filenameSplit = filename.split("\\.");
        String extension = filenameSplit[filenameSplit.length - 1];

        if (extension.equals("jpg") || extension.equals("jpeg")) {
            return ImageType.JPEG;
        } else if (extension.equals("png")) {
            return ImageType.PNG;
        } else {
            throw new IllegalArgumentException("Unsupported image type");
        }
    }
}
