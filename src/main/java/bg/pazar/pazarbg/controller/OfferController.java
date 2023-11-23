package bg.pazar.pazarbg.controller;

import bg.pazar.pazarbg.model.dto.offer.AddOfferBindingModel;
import bg.pazar.pazarbg.service.CategoryService;
import bg.pazar.pazarbg.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Arrays;

@Controller
@RequestMapping("/offer")
public class OfferController {
    private static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult";
    private final CategoryService categoryService;
    private final OfferService offerService;

    public OfferController(CategoryService categoryService, OfferService offerService) {
        this.categoryService = categoryService;
        this.offerService = offerService;
    }

    @GetMapping("/add")
    public ModelAndView getAddOffer(Model model) {
        if(!model.containsAttribute("addOfferBindingModel")) {
            model.addAttribute("addOfferBindingModel", new AddOfferBindingModel());
        }

        ModelAndView modelAndView = new ModelAndView("offer-add");
        modelAndView.addObject("categories", categoryService.getAllCategoryNames());
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView postAddOffer(@Valid @ModelAttribute("addOfferBindingModel") AddOfferBindingModel addOfferBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("offer-add");
            modelAndView.addObject("bindingModel", addOfferBindingModel);
            modelAndView.addObject("categories", categoryService.getAllCategoryNames());

            redirectAttributes.addFlashAttribute("addOfferBindingModel", addOfferBindingModel);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH, bindingResult);

            return modelAndView;
        }

        try {
            offerService.addOffer(addOfferBindingModel);
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("offer-add");

            addOfferBindingModel.setImages(null);
            modelAndView.addObject("bindingModel", addOfferBindingModel);
            modelAndView.addObject("categories", categoryService.getAllCategoryNames());

            String errorMessage;
            if(e instanceof IOException) errorMessage = "Cannot save image(s). Check filename and extension.";
            else errorMessage = e.getMessage();


            modelAndView.addObject("errorMessage", errorMessage);

            return modelAndView;
        }

        return new ModelAndView("redirect:/home");
    }
    
    
}
