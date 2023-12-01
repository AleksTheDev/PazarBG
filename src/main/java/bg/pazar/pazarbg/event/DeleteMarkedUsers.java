package bg.pazar.pazarbg.event;

import bg.pazar.pazarbg.model.entity.Offer;
import bg.pazar.pazarbg.model.entity.UserEntity;
import bg.pazar.pazarbg.repo.ImageRepository;
import bg.pazar.pazarbg.repo.MessageRepository;
import bg.pazar.pazarbg.repo.OfferRepository;
import bg.pazar.pazarbg.repo.UserRepository;
import bg.pazar.pazarbg.service.FileService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DeleteMarkedUsers {
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final ImageRepository imageRepository;
    private final MessageRepository messageRepository;
    private final FileService fileService;

    public DeleteMarkedUsers(UserRepository userRepository, OfferRepository offerRepository, ImageRepository imageRepository, MessageRepository messageRepository, FileService fileService) {
        this.userRepository = userRepository;
        this.offerRepository = offerRepository;
        this.imageRepository = imageRepository;
        this.messageRepository = messageRepository;
        this.fileService = fileService;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void deleteMarkedUsers() {
        List<UserEntity> users = userRepository.findAllByMarkedForDeletion(true);

        System.out.println("Deleting " + users.size() + " users.");

        users.forEach(user -> {
            Set<Offer> offersToDelete = user.getOffers();
            offersToDelete.addAll(user.getBoughtOffers());
            offersToDelete.forEach(offer -> {
                imageRepository.deleteAll(offer.getImages());
                offer.setImages(new HashSet<>());
            });

            messageRepository.deleteAll(user.getSentMessages());
            offerRepository.deleteAll(offersToDelete);

            user.setOffers(new HashSet<>());
            user.setBoughtOffers(new HashSet<>());
            user.setImages(new HashSet<>());
            user.setSentMessages(new HashSet<>());

            try {
                fileService.deleteUserImages(user);
            } catch (IOException e) {
                System.out.println("Failed to delete images for user " + user.getId());
            }

            userRepository.delete(user);
        });
    }
}
