package bg.pazar.pazarbg.event;

import bg.pazar.pazarbg.model.entity.Offer;
import bg.pazar.pazarbg.model.entity.UserEntity;
import bg.pazar.pazarbg.repo.MessageRepository;
import bg.pazar.pazarbg.repo.OfferRepository;
import bg.pazar.pazarbg.repo.UserRepository;
import bg.pazar.pazarbg.service.ImageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DeleteMarkedUsers {
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final ImageService imageService;
    private final MessageRepository messageRepository;

    public DeleteMarkedUsers(UserRepository userRepository, OfferRepository offerRepository, ImageService imageService, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.offerRepository = offerRepository;
        this.imageService = imageService;
        this.messageRepository = messageRepository;
    }

    //Delete users marked for deletion at 3 am every day
    @Scheduled(cron = "0 0 3 * * *")
    public void deleteMarkedUsers() {
        //Get users marked for deletion
        List<UserEntity> users = userRepository.findAllByMarkedForDeletion(true);

        System.out.println("Deleting " + users.size() + " users.");

        users.forEach(user -> {
            //Get offers created by user
            Set<Offer> offersToDelete = user.getOffers();

            //Add offers bought by user
            offersToDelete.addAll(user.getBoughtOffers());

            //Delete the images from the offers
            offersToDelete.forEach(offer -> {
                imageService.deleteAll(offer.getImages());
                offer.setImages(new HashSet<>());
            });

            //Delete sent and received messages
            messageRepository.deleteAll(user.getSentMessages());
            messageRepository.deleteAll(user.getReceivedMessages());

            //Delete added and bought offers
            offerRepository.deleteAll(offersToDelete);

            //Remove references to deleted objects
            user.setOffers(new HashSet<>());
            user.setBoughtOffers(new HashSet<>());
            user.setImages(new HashSet<>());
            user.setSentMessages(new HashSet<>());
            user.setReceivedMessages(new HashSet<>());

            //Delete the user
            userRepository.delete(user);
        });
    }
}
