package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.exception.MessageNotFoundException;
import bg.pazar.pazarbg.exception.OfferNotFoundException;
import bg.pazar.pazarbg.model.entity.Message;
import bg.pazar.pazarbg.model.entity.Offer;
import bg.pazar.pazarbg.model.entity.UserEntity;
import bg.pazar.pazarbg.model.view.MessageViewModel;
import bg.pazar.pazarbg.repo.MessageRepository;
import bg.pazar.pazarbg.repo.OfferRepository;
import bg.pazar.pazarbg.repo.UserRepository;
import bg.pazar.pazarbg.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private final AuthenticationService authenticationService;
    private final MessageRepository messageRepository;
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;

    public MessageServiceImpl(AuthenticationService authenticationService, MessageRepository messageRepository, OfferRepository offerRepository, UserRepository userRepository) {
        this.authenticationService = authenticationService;
        this.messageRepository = messageRepository;
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
    }

    //Send a message to seller
    @Override
    public Message sendMessage(Long offerID, String content) {
        //Check if message is too short or too long
        if (content.length() < 10 || content.length() > 200) return null;

        //Check if offer exists
        Offer offer = offerRepository.findById(offerID).orElse(null);
        if (offer == null) throw new OfferNotFoundException();

        //Get the logged-in user and set him as author
        UserEntity from = userRepository.findByUsername(authenticationService.getCurrentUserName());
        UserEntity to = offer.getCreatedBy();

        //Check if user is sending message to himself
        if (from.getUsername().equals(to.getUsername())) return null;

        //Create message entity
        Message message = new Message();

        message.setFrom(from);
        message.setTo(to);
        message.setOffer(offer);
        message.setContent(content);
        message.setReply(false);

        //Save message entity to database
        messageRepository.save(message);

        //Return saved message entity for testing
        return message;
    }

    //Reply to a message from a buyer
    @Override
    public Message replyToMessage(Long messageID, String content) {
        //Check if message is too short or too long
        if (content.length() < 10 || content.length() > 200) return null;

        //Find original message
        Message originalMessage = messageRepository.findById(messageID).orElse(null);
        if (originalMessage == null) throw new MessageNotFoundException();
        if (originalMessage.isReply()) throw new MessageNotFoundException();

        //Set the logged-in user as author
        UserEntity from = userRepository.findByUsername(authenticationService.getCurrentUserName());

        //Set recipient to original message author
        UserEntity to = originalMessage.getFrom();

        //Check if user is replying to himself
        if (from.getUsername().equals(to.getUsername())) return null;

        //Create message entity
        Message message = new Message();

        message.setFrom(from);
        message.setTo(to);
        message.setOffer(originalMessage.getOffer());
        message.setContent(content);
        message.setReply(true);

        //Save message entity to database
        messageRepository.save(message);

        //Return saved message entity for testing
        return message;
    }

    //Get view models for the view messages page
    @Override
    public List<MessageViewModel> getAllMessagesViewModels() {
        List<MessageViewModel> models = new ArrayList<>();

        messageRepository.findAllByToId(authenticationService.getCurrentUserId()).forEach(message -> {
            MessageViewModel model = new MessageViewModel();

            model.setId(message.getId());
            model.setFrom(message.getFrom().getUsername());
            model.setOfferTitle(message.getOffer().getTitle());
            model.setContent(message.getContent());
            model.setReply(message.isReply());

            models.add(model);
        });

        return models;
    }
}
