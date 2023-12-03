package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.exception.CategoryNotFoundException;
import bg.pazar.pazarbg.model.entity.Category;
import bg.pazar.pazarbg.model.view.CategoryViewModel;
import bg.pazar.pazarbg.model.view.HomeViewModel;
import bg.pazar.pazarbg.model.view.OfferViewModel;
import bg.pazar.pazarbg.repo.CategoryRepository;
import bg.pazar.pazarbg.service.CategoryService;
import bg.pazar.pazarbg.service.HomeService;
import bg.pazar.pazarbg.service.OfferService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HomeServiceImplTests {
    @Mock
    private OfferService mockOfferService;
    private CategoryService categoryService;
    @Mock
    private CategoryRepository mockCategoryRepository;

    private HomeService homeService;

    private Category testCategory;
    private OfferViewModel testOfferViewModel;

    @BeforeEach
    void setUp() {
        this.categoryService = new CategoryServiceImpl(mockCategoryRepository, new ModelMapper());

        this.homeService = new HomeServiceImpl(mockOfferService, categoryService, mockCategoryRepository);

        this.testCategory = new Category();
        this.testCategory.setId(1L);
        this.testCategory.setName("Technology");
        this.testCategory.setDescription("A category for technology");

        this.testOfferViewModel = new OfferViewModel();
        this.testOfferViewModel.setId(1L);
        this.testOfferViewModel.setUserOfferCreator(true);
        this.testOfferViewModel.setCategory(testCategory.getName());
        this.testOfferViewModel.setCreatedBy("Alex");
        this.testOfferViewModel.setPrice(100.0);
        this.testOfferViewModel.setTitle("Mouse");
        this.testOfferViewModel.setDescription("Gaming mouse");
    }

    @Test
    void getAllOffersTest() {
        when(mockCategoryRepository.findAll()).thenReturn(List.of(this.testCategory));
        when(mockOfferService.getAllOffersViewModels()).thenReturn(List.of(testOfferViewModel));

        HomeViewModel actual = homeService.getAllOffers();

        Assertions.assertNull(actual.getCurrentCategory(), "Current category should be null");
        Assertions.assertEquals(1, actual.getCategories().size(), "There should be only one category view model");
        Assertions.assertEquals(1, actual.getOffers().size(), "There should be only one offer view model");
    }

    @Test
    void getBoughtOffersTest() {
        when(mockOfferService.getBoughtOffersViewModels()).thenReturn(List.of(testOfferViewModel));

        HomeViewModel actual = homeService.getBoughtOffers();

        Assertions.assertNull(actual.getCurrentCategory(), "Current category should be null");
        Assertions.assertNull(actual.getCategories(), "There should be no categories");
        Assertions.assertEquals(1, actual.getOffers().size(), "There should be only one offer view model");
    }

    @Test
    void getOffersByCategoryTest() {
        when(mockCategoryRepository.findAll()).thenReturn(List.of(this.testCategory));
        when(mockCategoryRepository.findById(testCategory.getId())).thenReturn(Optional.ofNullable(this.testCategory));
        when(mockOfferService.getAllOffersViewModelsByCategory(testCategory)).thenReturn(List.of(testOfferViewModel));

        HomeViewModel actual = homeService.getOffersByCategory(1L);
        CategoryViewModel expectedCategory = categoryService.getCategoryViewModel(testCategory);

        Assertions.assertEquals(expectedCategory.getId(), actual.getCurrentCategory().getId(), "Current category ids don't match");
        Assertions.assertEquals(expectedCategory.getName(), actual.getCurrentCategory().getName(), "Current category names don't match");
        Assertions.assertEquals(expectedCategory.getDescription(), actual.getCurrentCategory().getDescription(), "Current category descriptions don't match");
        Assertions.assertEquals(1, actual.getCategories().size(), "There should be only one category");
        Assertions.assertEquals(1, actual.getOffers().size(), "There should be only one offer view model");
    }

    @Test
    void getOffersByCategoryShouldThrowExceptionTest() {
        when(mockCategoryRepository.findById(testCategory.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(CategoryNotFoundException.class, () -> homeService.getOffersByCategory(1L));
    }
}
