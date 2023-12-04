package bg.pazar.pazarbg.controller;

import bg.pazar.pazarbg.model.dto.offer.AddOfferBindingModel;
import bg.pazar.pazarbg.service.CategoryService;
import bg.pazar.pazarbg.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/offers")
public class OfferController {
    private static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult";
    private final CategoryService categoryService;
    private final OfferService offerService;

    public OfferController(CategoryService categoryService, OfferService offerService) {
        this.categoryService = categoryService;
        this.offerService = offerService;
    }

    //Get mapping for offer-add
    @GetMapping("/add")
    public ModelAndView getAddOffer(Model model) {
        //Add binding model
        if (!model.containsAttribute("addOfferBindingModel")) {
            model.addAttribute("addOfferBindingModel", new AddOfferBindingModel());
        }

        ModelAndView modelAndView = new ModelAndView("offer-add");
        modelAndView.addObject("categories", categoryService.getAllCategoryNames());
        return modelAndView;
    }

    //Post mapping for adding offer
    @PostMapping("/add")
    public ModelAndView postAddOffer(@Valid @ModelAttribute("addOfferBindingModel") AddOfferBindingModel addOfferBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //Validation
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("offer-add");
            modelAndView.addObject("bindingModel", addOfferBindingModel);
            modelAndView.addObject("categories", categoryService.getAllCategoryNames());

            //Adding error attributes
            redirectAttributes.addFlashAttribute("addOfferBindingModel", addOfferBindingModel);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH, bindingResult);

            return modelAndView;
        }

        try {
            //Add offer
            offerService.addOffer(addOfferBindingModel);
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("offer-add");

            //Return entered data to fields
            addOfferBindingModel.setImages(null);
            modelAndView.addObject("bindingModel", addOfferBindingModel);
            modelAndView.addObject("categories", categoryService.getAllCategoryNames());

            //Create user-friendly error message
            String errorMessage;
            if (e instanceof IOException) errorMessage = "Cannot save image(s). Check filename and extension.";
            else errorMessage = e.getMessage();


            modelAndView.addObject("errorMessage", errorMessage);

            return modelAndView;
        }

        return new ModelAndView("redirect:/home");
    }

    //Get mapping for buying offers
    @GetMapping("/buy/{id}")
    public String buy(@PathVariable("id") Long id) {
        offerService.buyOffer(id);
        return "redirect:/home";
    }
}
