package bg.pazar.pazarbg.controller;

import bg.pazar.pazarbg.model.dto.offer.AddOfferBindingModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/offer")
public class OfferController {
    @GetMapping("/add")
    public String addOfferGet(Model model) {
        if(!model.containsAttribute("addOfferBindingModel")) {
            model.addAttribute("addOfferBindingModel", new AddOfferBindingModel());
        }

        return "offer-add";
    }
}
