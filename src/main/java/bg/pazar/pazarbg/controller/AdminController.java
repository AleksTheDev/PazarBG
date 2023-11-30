package bg.pazar.pazarbg.controller;

import bg.pazar.pazarbg.model.dto.category.AddCategoryBindingModel;
import bg.pazar.pazarbg.service.CategoryService;
import bg.pazar.pazarbg.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult";

    private final CategoryService categoryService;
    private final UserService userService;

    public AdminController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/category-add")
    public String getAddCategory(Model model) {
        if (!model.containsAttribute("addCategoryBindingModel")) {
            model.addAttribute("addCategoryBindingModel", new AddCategoryBindingModel());
        }

        return "category-add";
    }

    @PostMapping("/category-add")
    public ModelAndView postAddCategory(@Valid @ModelAttribute("addCategoryBindingModel") AddCategoryBindingModel addCategoryBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("category-add");
            modelAndView.addObject("bindingModel", addCategoryBindingModel);

            redirectAttributes.addFlashAttribute("addCategoryBindingModel", addCategoryBindingModel);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH, bindingResult);

            return modelAndView;
        }

        categoryService.addCategory(addCategoryBindingModel);

        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/manage-users")
    public ModelAndView manageUsers() {
        ModelAndView modelAndView = new ModelAndView("manage-users");

        modelAndView.addObject("users", userService.getAllUsersViewModels());

        return modelAndView;
    }

    @GetMapping("/toggle-role/{id}")
    public String toggleRole(@PathVariable Long id) {
        userService.toggleRole(id);

        return "redirect:/admin/manage-users";
    }

    @GetMapping("/toggle-deletion/{id}")
    public String toggleDeletion(@PathVariable Long id) {
        userService.toggleDeletion(id);

        return "redirect:/admin/manage-users";
    }
}
