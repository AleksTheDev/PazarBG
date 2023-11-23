package bg.pazar.pazarbg.controller;

import bg.pazar.pazarbg.exception.ImageNotFoundException;
import bg.pazar.pazarbg.model.dto.offer.AddOfferBindingModel;
import bg.pazar.pazarbg.model.entity.Offer;
import bg.pazar.pazarbg.repo.OfferRepository;
import bg.pazar.pazarbg.service.CategoryService;
import bg.pazar.pazarbg.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
@RequestMapping("/offer")
public class OfferController {
    private static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult";
    private final CategoryService categoryService;
    private final OfferService offerService;
    private final OfferRepository offerRepository;

    public OfferController(CategoryService categoryService, OfferService offerService, OfferRepository offerRepository) {
        this.categoryService = categoryService;
        this.offerService = offerService;
        this.offerRepository = offerRepository;
    }

    @GetMapping("/add")
    public ModelAndView getAddOffer(Model model) {
        if (!model.containsAttribute("addOfferBindingModel")) {
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
            if (e instanceof IOException) errorMessage = "Cannot save image(s). Check filename and extension.";
            else errorMessage = e.getMessage();


            modelAndView.addObject("errorMessage", errorMessage);

            return modelAndView;
        }

        return new ModelAndView("redirect:/home");
    }

    @GetMapping(
            value = "/image/{offerid}/{imageid}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getImageWithID(@PathVariable("offerid") Long offerID, @PathVariable("imageid") int imageID) throws IOException {
        try {
            Offer offer = offerRepository.findById(offerID).orElseThrow();
            String imagePath = offer.getImagePaths().get(imageID);
            return Files.readAllBytes(Path.of(imagePath));
        } catch (Exception e) {
            throw new ImageNotFoundException();
        }
    }
}
