package bg.pazar.pazarbg.service;

import bg.pazar.pazarbg.model.entity.Message;
import bg.pazar.pazarbg.model.view.MessageViewModel;

import java.util.List;

public interface MessageService {
    Message sendMessage(Long offerID, String content);

    Message replyToMessage(Long messageID, String content);

    List<MessageViewModel> getAllMessagesViewModels();
}
