package bg.pazar.pazarbg.controller;

import bg.pazar.pazarbg.exception.ImageNotFoundException;
import bg.pazar.pazarbg.model.dto.offer.AddOfferBindingModel;
import bg.pazar.pazarbg.model.entity.Image;
import bg.pazar.pazarbg.model.enums.ImageType;
import bg.pazar.pazarbg.repo.ImageRepository;
import bg.pazar.pazarbg.service.CategoryService;
import bg.pazar.pazarbg.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
@RequestMapping("/offer")
public class OfferController {
    private static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult";
    private final CategoryService categoryService;
    private final OfferService offerService;
    private final ImageRepository imageRepository;

    public OfferController(CategoryService categoryService, OfferService offerService, ImageRepository imageRepository) {
        this.categoryService = categoryService;
        this.offerService = offerService;
        this.imageRepository = imageRepository;
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

    @GetMapping("/buy/{id}")
    public String buy(@PathVariable("id") Long id) {
        offerService.buyOffer(id);
        return "redirect:/home";
    }

    @PostMapping("/message/{id}")
    public String message(@PathVariable("id") Long id, String message) {
        offerService.sendMessage(id, message);
        return "redirect:/home";
    }

    @PostMapping("/reply/{id}")
    public String reply(@PathVariable("id") Long id, String message) {
        offerService.replyToMessage(id, message);
        return "redirect:/home/messages";
    }

    @GetMapping(value = "/image/{id}")
    public ResponseEntity<byte[]> getImageWithID(@PathVariable("id") Long id) {
        try {
            Image image = imageRepository.findById(id).orElseThrow();
            byte[] bytes = Files.readAllBytes(Path.of(image.getPath()));

            if (image.getType().name().equals(ImageType.PNG.name())) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytes);
            } else {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
            }
        } catch (Exception e) {
            throw new ImageNotFoundException();
        }
    }
}
