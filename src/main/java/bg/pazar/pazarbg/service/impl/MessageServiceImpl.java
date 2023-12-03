package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.model.view.MessageViewModel;
import bg.pazar.pazarbg.repo.MessageRepository;
import bg.pazar.pazarbg.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private final AuthenticationService authenticationService;
    private final MessageRepository messageRepository;

    public MessageServiceImpl(AuthenticationService authenticationService, MessageRepository messageRepository) {
        this.authenticationService = authenticationService;
        this.messageRepository = messageRepository;
    }

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
