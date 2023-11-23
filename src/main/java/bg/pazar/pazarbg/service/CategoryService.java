package bg.pazar.pazarbg.service;

import bg.pazar.pazarbg.model.dto.category.AddCategoryBindingModel;

import java.util.List;

public interface CategoryService {
    void addCategory(AddCategoryBindingModel addCategoryBindingModel);
    List<String> getAllCategoryNames();
}
