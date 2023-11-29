package bg.pazar.pazarbg.service;

import bg.pazar.pazarbg.model.view.MessageViewModel;

import java.util.List;

public interface MessageService {
    List<MessageViewModel> getAllMessagesViewModels();
}
