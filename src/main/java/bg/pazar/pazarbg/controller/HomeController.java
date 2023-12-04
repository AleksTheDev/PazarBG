package bg.pazar.pazarbg.controller;

import bg.pazar.pazarbg.model.view.HomeViewModel;
import bg.pazar.pazarbg.service.HomeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    //Get mapping for homepage
    @GetMapping
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("home");

        HomeViewModel viewModel = homeService.getAllOffers();

        modelAndView.addObject("viewModel", viewModel);

        return modelAndView;
    }

    //Get mapping for bought offers
    @GetMapping("/bought")
    public ModelAndView bought() {
        ModelAndView modelAndView = new ModelAndView("home");

        HomeViewModel viewModel = homeService.getBoughtOffers();

        modelAndView.addObject("viewModel", viewModel);
        modelAndView.addObject("boughtOffersMode", true);

        return modelAndView;
    }

    //Get mapping for showing offers of a specific category
    @GetMapping("/category/{id}")
    public ModelAndView home(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("home");

        HomeViewModel viewModel = homeService.getOffersByCategory(id);

        modelAndView.addObject("viewModel", viewModel);

        return modelAndView;
    }
}
