package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.model.dto.category.AddCategoryBindingModel;
import bg.pazar.pazarbg.model.entity.Category;
import bg.pazar.pazarbg.model.view.CategoryViewModel;
import bg.pazar.pazarbg.repo.CategoryRepository;
import bg.pazar.pazarbg.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addCategory(AddCategoryBindingModel addCategoryBindingModel) {
        Category category = modelMapper.map(addCategoryBindingModel, Category.class);
        categoryRepository.save(category);
    }

    @Override
    public List<String> getAllCategoryNames() {
        List<String> categoryNames = new ArrayList<>();

        categoryRepository.findAll().forEach(category -> {
            categoryNames.add(category.getName());
        });

        return categoryNames;
    }

    @Override
    public List<CategoryViewModel> getAllCategoriesViewModels() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryViewModel> models = new ArrayList<>();

        categories.forEach(category -> {
            models.add(getCategoryViewModel(category));
        });

        return models;
    }

    @Override
    public CategoryViewModel getCategoryViewModel(Category category) {
        return modelMapper.map(category, CategoryViewModel.class);
    }
}
