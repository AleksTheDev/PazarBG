package bg.pazar.pazarbg.service;

import bg.pazar.pazarbg.model.dto.category.AddCategoryBindingModel;
import bg.pazar.pazarbg.model.entity.Category;
import bg.pazar.pazarbg.model.view.CategoryViewModel;

import java.util.List;

public interface CategoryService {
    void addCategory(AddCategoryBindingModel addCategoryBindingModel);
    List<String> getAllCategoryNames();

    List<CategoryViewModel> getAllCategoriesViewModels();

    CategoryViewModel getCategoryViewModel(Category category);
}
