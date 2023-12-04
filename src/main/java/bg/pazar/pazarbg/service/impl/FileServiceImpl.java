package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.model.entity.Image;
import bg.pazar.pazarbg.model.enums.ImageType;
import bg.pazar.pazarbg.service.FileService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class FileServiceImpl implements FileService {
    public static final String BASE_IMAGES_PATH = "./userImages/";

    //Save uploaded images to disk
    @Override
    public void saveImageToDisk(Image image, byte[] bytes) throws IOException {
        //Create file
        File file = new File(image.getPath());
        file.getParentFile().mkdirs();
        file.createNewFile();

        //Write to file
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
        outputStream.close();
    }

    //Delete an image's file
    @Override
    public boolean deleteImage(Image image) {
        File img = new File(image.getPath());
        return img.delete();
    }

    //Get formatted image path for saving to database
    @Override
    public String formatImagePath(Image image, String filename) throws IllegalArgumentException {
        //Get extension
        String[] filenameSplit = filename.split("\\.");
        String extension = filenameSplit[filenameSplit.length - 1];

        //Check if extension is supported
        if (!(extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png"))) {
            throw new IllegalArgumentException("Unsupported image type");
        }

        //Format path
        return BASE_IMAGES_PATH + image.getUploadedBy().getId() + "/" + image.getOffer().getId() + "/" + image.getId() + "." + extension;
    }

    //Get image type
    @Override
    public ImageType getImageType(String filename) throws IllegalArgumentException {
        //Get extension
        String[] filenameSplit = filename.split("\\.");
        String extension = filenameSplit[filenameSplit.length - 1];

        //Check if extension is supported
        if (extension.equals("jpg") || extension.equals("jpeg")) {
            return ImageType.JPEG;
        } else if (extension.equals("png")) {
            return ImageType.PNG;
        } else {
            throw new IllegalArgumentException("Unsupported image type");
        }
    }
}
