package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.exception.CategoryNotFoundException;
import bg.pazar.pazarbg.model.entity.Category;
import bg.pazar.pazarbg.model.view.HomeViewModel;
import bg.pazar.pazarbg.repo.CategoryRepository;
import bg.pazar.pazarbg.service.CategoryService;
import bg.pazar.pazarbg.service.HomeService;
import bg.pazar.pazarbg.service.OfferService;
import org.springframework.stereotype.Service;

@Service
public class HomeServiceImpl implements HomeService {
    private final OfferService offerService;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;


    public HomeServiceImpl(OfferService offerService, CategoryService categoryService, CategoryRepository categoryRepository) {
        this.offerService = offerService;
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public HomeViewModel getAllOffers() {
        HomeViewModel model = new HomeViewModel();

        model.setCurrentCategory(null);
        model.setCategories(categoryService.getAllCategoriesViewModels());
        model.setOffers(offerService.getAllOffersViewModels());

        return model;
    }

    @Override
    public HomeViewModel getBoughtOffers() {
        HomeViewModel model = new HomeViewModel();

        model.setCurrentCategory(null);
        model.setCategories(null);
        model.setOffers(offerService.getBoughtOffersViewModels());

        return model;
    }

    @Override
    public HomeViewModel getOffersByCategory(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) throw new CategoryNotFoundException();

        HomeViewModel model = new HomeViewModel();

        model.setCurrentCategory(categoryService.getCategoryViewModel(category));
        model.setCategories(categoryService.getAllCategoriesViewModels());
        model.setOffers(offerService.getAllOffersViewModelsByCategory(category));

        return model;
    }
}
