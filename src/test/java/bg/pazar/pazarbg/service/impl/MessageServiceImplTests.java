package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.model.entity.Category;
import bg.pazar.pazarbg.model.entity.Message;
import bg.pazar.pazarbg.model.entity.Offer;
import bg.pazar.pazarbg.model.entity.UserEntity;
import bg.pazar.pazarbg.model.enums.UserRole;
import bg.pazar.pazarbg.model.view.MessageViewModel;
import bg.pazar.pazarbg.repo.MessageRepository;
import bg.pazar.pazarbg.repo.OfferRepository;
import bg.pazar.pazarbg.repo.UserRepository;
import bg.pazar.pazarbg.service.MessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageServiceImplTests {
    @Mock
    private AuthenticationService mockAuthenticationService;

    @Mock
    private MessageRepository mockMessageRepository;

    @Mock
    private OfferRepository mockOfferRepository;

    @Mock
    private UserRepository mockUserRepository;

    private MessageService messageService;

    private Message testMessage;
    private UserEntity testUser;
    private UserEntity testUser2;
    private Category testCategory;
    private Offer testOffer;

    @BeforeEach
    void setUp() {
        this.messageService = new MessageServiceImpl(mockAuthenticationService, mockMessageRepository, mockOfferRepository, mockUserRepository);

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

        this.testCategory = new Category();
        this.testCategory.setId(1L);
        this.testCategory.setName("Technology");
        this.testCategory.setDescription("A category for technology");

        this.testOffer = new Offer();
        this.testOffer.setId(1L);
        this.testOffer.setImages(new HashSet<>());
        this.testOffer.setPrice(20.0);
        this.testOffer.setCategory(this.testCategory);
        this.testOffer.setTitle("Mouse");
        this.testOffer.setDescription("Gaming mouse");
        this.testOffer.setCreatedBy(testUser);

        this.testMessage = new Message();
        this.testMessage.setId(1L);
        this.testMessage.setContent("Hello, is this product available?");
        this.testMessage.setFrom(testUser2);
        this.testMessage.setTo(testUser);
        this.testMessage.setOffer(testOffer);
    }

    @Test
    void sendMessageTest() {
        when(mockOfferRepository.findById(testOffer.getId())).thenReturn(Optional.ofNullable(testOffer));
        when(mockUserRepository.findByUsername(mockAuthenticationService.getCurrentUserName())).thenReturn(testUser2);

        Message actual = messageService.sendMessage(1L, testMessage.getContent());

        Assertions.assertEquals(testMessage.getContent(), actual.getContent(), "Message contents don't match");
        Assertions.assertEquals(testMessage.getFrom(), actual.getFrom(), "Message senders don't match");
        Assertions.assertEquals(testMessage.getTo(), actual.getTo(), "Message receivers don't match");
        Assertions.assertEquals(testMessage.getOffer(), actual.getOffer(), "Message offers don't match");
        Assertions.assertFalse(actual.isReply(), "Message should not be marked as a reply");
    }

    @Test
    void replyToMessageTest() {
        when(mockMessageRepository.findById(testMessage.getId())).thenReturn(Optional.ofNullable(testMessage));
        when(mockUserRepository.findByUsername(mockAuthenticationService.getCurrentUserName())).thenReturn(testUser);

        Message actual = messageService.replyToMessage(1L, testMessage.getContent());

        Assertions.assertEquals(testMessage.getContent(), actual.getContent(), "Message contents don't match");
        Assertions.assertEquals(testMessage.getTo(), actual.getFrom(), "Message senders don't match");
        Assertions.assertEquals(testMessage.getFrom(), actual.getTo(), "Message receivers don't match");
        Assertions.assertEquals(testMessage.getOffer(), actual.getOffer(), "Message offers don't match");
        Assertions.assertTrue(actual.isReply(), "Message should be marked as a reply");
    }

    @Test
    void getAllMessagesViewModelsTest() {
        when(mockMessageRepository.findAllByToId(mockAuthenticationService.getCurrentUserId())).thenReturn(List.of(this.testMessage));

        List<MessageViewModel> actualList = messageService.getAllMessagesViewModels();

        Assertions.assertEquals(1, actualList.size(), "There should be only one message view model");

        MessageViewModel actualMessage = actualList.get(0);

        Assertions.assertEquals(testMessage.getFrom().getUsername(), actualMessage.getFrom(), "From usernames don't match");
        Assertions.assertEquals(testMessage.getId(), actualMessage.getId(), "Message ids don't match");
        Assertions.assertEquals(testMessage.getOffer().getTitle(), actualMessage.getOfferTitle(), "Offer titles don't match");
        Assertions.assertEquals(testMessage.getContent(), actualMessage.getContent(), "Message contents don't match");
    }
}
