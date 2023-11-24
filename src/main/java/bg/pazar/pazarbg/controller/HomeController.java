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

    @GetMapping
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("home");

        HomeViewModel viewModel = homeService.getAllOffers();

        modelAndView.addObject("viewModel", viewModel);

        return modelAndView;
    }

    @GetMapping("/category/{id}")
    public ModelAndView home(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("home");

        HomeViewModel viewModel = homeService.getOffersByCategory(id);

        modelAndView.addObject("viewModel", viewModel);

        return modelAndView;
    }
}
