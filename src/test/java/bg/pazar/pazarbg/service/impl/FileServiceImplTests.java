package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.model.entity.Image;
import bg.pazar.pazarbg.model.entity.Offer;
import bg.pazar.pazarbg.model.entity.UserEntity;
import bg.pazar.pazarbg.model.enums.ImageType;
import bg.pazar.pazarbg.service.FileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FileServiceImplTests {
    private FileService fileService;

    @BeforeEach
    void setUp() {
        fileService = new FileServiceImpl();
    }

    @Test
    void testFormatImagePath() {
        String filename = "alex.jpg";

        UserEntity testUser = new UserEntity();
        testUser.setId(1L);

        Offer testOffer = new Offer();
        testOffer.setId(2L);

        Image testImage = new Image();
        testImage.setId(3L);
        testImage.setUploadedBy(testUser);
        testImage.setOffer(testOffer);

        String expected = FileServiceImpl.BASE_IMAGES_PATH + "1/2/3.jpg";
        String actual = fileService.formatImagePath(testImage, filename);

        Assertions.assertEquals(expected, actual, "Formatted image path is not correct");
    }

    @Test
    void testFormatImagePathException() {
        String filename = "alex.gif";

        UserEntity testUser = new UserEntity();
        testUser.setId(1L);

        Offer testOffer = new Offer();
        testOffer.setId(2L);

        Image testImage = new Image();
        testImage.setId(3L);
        testImage.setUploadedBy(testUser);
        testImage.setOffer(testOffer);

        Assertions.assertThrows(IllegalArgumentException.class, () -> fileService.formatImagePath(testImage, filename), "IllegalArgumentException should be thrown");
    }

    @Test
    void testGetImageTypeJPEG() {
        String filename = "alex.jpg";

        Assertions.assertEquals(ImageType.JPEG, fileService.getImageType(filename), "ImageType should be JPEG");
    }

    @Test
    void testGetImageTypePNG() {
        String filename = "alex.png";

        Assertions.assertEquals(ImageType.PNG, fileService.getImageType(filename), "ImageType should be PNG");
    }

    @Test
    void testGetImageTypeException() {
        String filename = "alex.gif";

        Assertions.assertThrows(IllegalArgumentException.class,() -> fileService.getImageType(filename), "IllegalArgumentException should be thrown");
    }

}
