package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.model.dto.offer.AddOfferBindingModel;
import bg.pazar.pazarbg.model.entity.*;
import bg.pazar.pazarbg.model.enums.ImageType;
import bg.pazar.pazarbg.model.enums.UserRole;
import bg.pazar.pazarbg.model.view.OfferViewModel;
import bg.pazar.pazarbg.repo.CategoryRepository;
import bg.pazar.pazarbg.repo.MessageRepository;
import bg.pazar.pazarbg.repo.OfferRepository;
import bg.pazar.pazarbg.repo.UserRepository;
import bg.pazar.pazarbg.service.ImageService;
import bg.pazar.pazarbg.service.OfferService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OfferServiceImplTests {
    @Mock
    private ImageService mockImageService;
    @Mock
    private OfferRepository mockOfferRepository;
    @Mock
    private CategoryRepository mockCategoryRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private MessageRepository mockMessageRepository;
    @Mock
    private AuthenticationService mockAuthenticationService;
    private ModelMapper modelMapper;
    private OfferService offerService;

    private Category testCategory;
    private UserEntity testUser;
    private UserEntity testUser2;
    private Image testImage;
    private Offer testOffer;
    private Offer testOffer2;
    private Message testMessage;

    @BeforeEach
    void setUp() {
        this.modelMapper = new ModelMapper();
        this.offerService = new OfferServiceImpl(mockImageService, mockOfferRepository, mockCategoryRepository, mockUserRepository, mockMessageRepository, mockAuthenticationService, modelMapper);

        this.testCategory = new Category();
        this.testCategory.setId(1L);
        this.testCategory.setName("Technology");
        this.testCategory.setDescription("A category for technology");

        this.testUser = new UserEntity();
        this.testUser.setUsername("Alex");
        this.testUser.setEmail("alex@gmail.com");
        this.testUser.setId(1L);
        this.testUser.setRole(UserRole.ADMIN);
        this.testUser.setMarkedForDeletion(false);

        this.testUser2 = new UserEntity();
        this.testUser2.setUsername("Alex2");
        this.testUser2.setEmail("alex2@gmail.com");
        this.testUser2.setId(2L);
        this.testUser2.setRole(UserRole.USER);
        this.testUser2.setMarkedForDeletion(false);

        this.testImage = new Image();
        this.testImage.setId(1L);
        this.testImage.setType(ImageType.JPEG);
        this.testImage.setUploadedBy(testUser);
        this.testImage.setPath("./userImages/1.jpg");

        this.testOffer = new Offer();
        this.testOffer.setId(1L);
        this.testOffer.setImages(Set.of(testImage));
        this.testOffer.setPrice(20.0);
        this.testOffer.setCategory(this.testCategory);
        this.testOffer.setTitle("Mouse");
        this.testOffer.setDescription("Gaming mouse");
        this.testOffer.setCreatedBy(testUser);

        this.testOffer2 = new Offer();
        this.testOffer2.setId(1L);
        this.testOffer2.setImages(Set.of(testImage));
        this.testOffer2.setPrice(20.0);
        this.testOffer2.setCategory(this.testCategory);
        this.testOffer2.setTitle("Mouse2");
        this.testOffer2.setDescription("Gaming mouse2");
        this.testOffer2.setCreatedBy(testUser);
        this.testOffer2.setBoughtBy(testUser2);

        this.testMessage = new Message();
        this.testMessage.setId(1L);
        this.testMessage.setContent("Hello, is this product available?");
        this.testMessage.setFrom(testUser2);
        this.testMessage.setTo(testUser);
        this.testMessage.setOffer(testOffer);
    }

    @Test
    void addOfferTest() {
        when(mockUserRepository.findByUsername(mockAuthenticationService.getCurrentUserName())).thenReturn(testUser);
        when(mockCategoryRepository.findByName(testCategory.getName())).thenReturn(testCategory);
        when(mockOfferRepository.save(testOffer)).thenReturn(testOffer);

        AddOfferBindingModel model = new AddOfferBindingModel();

        model.setTitle(testOffer.getTitle());
        model.setDescription(testOffer.getDescription());
        model.setPrice(testOffer.getPrice());
        model.setCategory(testCategory.getName());
        model.setImages(new ArrayList<>());

        try {
            Offer actual = offerService.addOffer(model);
            Assertions.assertEquals(testOffer, actual, "Offers do not match");
        } catch (Exception e) {
            //Do nothing
        }
    }

    @Test
    void buyOfferTest() {
        when(mockUserRepository.findByUsername(mockAuthenticationService.getCurrentUserName())).thenReturn(testUser2);
        when(mockOfferRepository.findById(1L)).thenReturn(Optional.ofNullable(testOffer));

        Offer actual = offerService.buyOffer(1L);

        Assertions.assertEquals(testUser2, actual.getBoughtBy(), "BoughtBy doesn't match");
    }

    @Test
    void sendMessageTest() {
        when(mockOfferRepository.findById(1L)).thenReturn(Optional.ofNullable(testOffer));
        when(mockUserRepository.findByUsername(mockAuthenticationService.getCurrentUserName())).thenReturn(testUser2);

        Message actual = offerService.sendMessage(1L, testMessage.getContent());

        Assertions.assertEquals(testMessage.getContent(), actual.getContent(), "Message contents don't match");
        Assertions.assertEquals(testMessage.getFrom(), actual.getFrom(), "Message senders don't match");
        Assertions.assertEquals(testMessage.getTo(), actual.getTo(), "Message receivers don't match");
        Assertions.assertEquals(testMessage.getOffer(), actual.getOffer(), "Message offers don't match");
    }

    @Test
    void generateViewModelForOfferTest() {
        OfferViewModel actual = offerService.generateViewModelForOffer(testOffer);

        Assertions.assertEquals(testOffer.getId(), actual.getId(), "Ids don't match");
        Assertions.assertEquals(testOffer.getTitle(), actual.getTitle(), "Titles don't match");
        Assertions.assertEquals(testOffer.getDescription(), actual.getDescription(), "Descriptions don't match");
        Assertions.assertEquals(testOffer.getPrice(), actual.getPrice(), "Prices don't match");
        Assertions.assertEquals(testOffer.getCreatedBy().getUsername(), actual.getCreatedBy(), "CreatedBy Usernames don't match");
        Assertions.assertEquals(testOffer.getCategory().getName(), actual.getCategory(), "Categories don't match");
        Assertions.assertEquals(testImage.getId(), actual.getFirstImageId(), "First images ids don't match");
        Assertions.assertFalse(actual.isUserOfferCreator(), "IsUserOfferCreator should be false");
    }

    @Test
    void getAllOffersViewModelsTest() {
        when(mockOfferRepository.findAllByBoughtByIsNull()).thenReturn(List.of(testOffer));

        List<OfferViewModel> models = offerService.getAllOffersViewModels();
        OfferViewModel actual = models.get(0);

        Assertions.assertEquals(1, models.size(), "Only one offer view model should be returned");

        Assertions.assertEquals(testOffer.getId(), actual.getId(), "Ids don't match");
        Assertions.assertEquals(testOffer.getTitle(), actual.getTitle(), "Titles don't match");
        Assertions.assertEquals(testOffer.getDescription(), actual.getDescription(), "Descriptions don't match");
        Assertions.assertEquals(testOffer.getPrice(), actual.getPrice(), "Prices don't match");
        Assertions.assertEquals(testOffer.getCreatedBy().getUsername(), actual.getCreatedBy(), "CreatedBy Usernames don't match");
        Assertions.assertEquals(testOffer.getCategory().getName(), actual.getCategory(), "Categories don't match");
        Assertions.assertEquals(testImage.getId(), actual.getFirstImageId(), "First images ids don't match");
        Assertions.assertFalse(actual.isUserOfferCreator(), "IsUserOfferCreator should be false");
    }

    @Test
    void getAllOffersViewModelsByCategoryTest() {
        testCategory.setOffers(Set.of(testOffer));
        List<OfferViewModel> models = offerService.getAllOffersViewModelsByCategory(testCategory);
        OfferViewModel actual = models.get(0);

        Assertions.assertEquals(1, models.size(), "Only one offer view model should be returned");

        Assertions.assertEquals(testOffer.getId(), actual.getId(), "Ids don't match");
        Assertions.assertEquals(testOffer.getTitle(), actual.getTitle(), "Titles don't match");
        Assertions.assertEquals(testOffer.getDescription(), actual.getDescription(), "Descriptions don't match");
        Assertions.assertEquals(testOffer.getPrice(), actual.getPrice(), "Prices don't match");
        Assertions.assertEquals(testOffer.getCreatedBy().getUsername(), actual.getCreatedBy(), "CreatedBy Usernames don't match");
        Assertions.assertEquals(testOffer.getCategory().getName(), actual.getCategory(), "Categories don't match");
        Assertions.assertEquals(testImage.getId(), actual.getFirstImageId(), "First images ids don't match");
        Assertions.assertFalse(actual.isUserOfferCreator(), "IsUserOfferCreator should be false");
    }

    @Test
    void getBoughtOffersViewModelsTest() {
        when(mockUserRepository.findByUsername(mockAuthenticationService.getCurrentUserName())).thenReturn(testUser2);
        when(mockOfferRepository.findAllByBoughtBy(testUser2)).thenReturn(List.of(testOffer2));

        List<OfferViewModel> models = offerService.getBoughtOffersViewModels();
        OfferViewModel actual = models.get(0);

        Assertions.assertEquals(1, models.size(), "Only one offer view model should be returned");

        Assertions.assertEquals(testOffer2.getId(), actual.getId(), "Ids don't match");
        Assertions.assertEquals(testOffer2.getTitle(), actual.getTitle(), "Titles don't match");
        Assertions.assertEquals(testOffer2.getDescription(), actual.getDescription(), "Descriptions don't match");
        Assertions.assertEquals(testOffer2.getPrice(), actual.getPrice(), "Prices don't match");
        Assertions.assertEquals(testOffer2.getCreatedBy().getUsername(), actual.getCreatedBy(), "CreatedBy Usernames don't match");
        Assertions.assertEquals(testOffer2.getCategory().getName(), actual.getCategory(), "Categories don't match");
        Assertions.assertEquals(testImage.getId(), actual.getFirstImageId(), "First images ids don't match");
        Assertions.assertFalse(actual.isUserOfferCreator(), "IsUserOfferCreator should be false");
    }
}
