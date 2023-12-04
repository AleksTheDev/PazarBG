package bg.pazar.pazarbg.controller;

import bg.pazar.pazarbg.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    //Get mapping for viewing messages
    @GetMapping("/view")
    public ModelAndView messages() {
        ModelAndView modelAndView = new ModelAndView("messages");

        modelAndView.addObject("messages", messageService.getAllMessagesViewModels());

        return modelAndView;
    }

    //Post mapping for sending messages
    @PostMapping("/send/{id}")
    public String message(@PathVariable("id") Long id, String message) {
        messageService.sendMessage(id, message);
        return "redirect:/home";
    }

    //Post mapping for replying to messages
    @PostMapping("/reply/{id}")
    public String reply(@PathVariable("id") Long id, String message) {
        messageService.replyToMessage(id, message);
        return "redirect:/messages/view";
    }
}
