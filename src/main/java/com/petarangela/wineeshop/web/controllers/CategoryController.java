package com.petarangela.wineeshop.web.controllers;

import com.petarangela.wineeshop.model.Category;
import com.petarangela.wineeshop.model.Type;
import com.petarangela.wineeshop.service.CategoryService;
import com.petarangela.wineeshop.service.TypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final TypeService typeService;


    public CategoryController(CategoryService categoryService, TypeService typeService) {
        this.categoryService = categoryService;
        this.typeService = typeService;
    }

    @GetMapping
    public String getCategoriesPage(@RequestParam(required = false) String error, Model model){
        if(error!=null && !error.isEmpty()){
            model.addAttribute("hasError",true);
            model.addAttribute("error",error);
        }
        List<Category> categories = this.categoryService.listAll();
        List<Category> distinct = categories.stream().distinct().collect(Collectors.toList());
        for(Category cat:distinct) {
            List<String> types = this.typeService.listAllTypes().stream()
                    .filter(t -> t.getCategory().getName().equals(cat.getName()))
                    .map(Type::getName)
                    .collect(Collectors.toList());
            String sessionName = "typesIn" + cat.getName();
            model.addAttribute(sessionName, types);
        }
        model.addAttribute("categories", categories);
        return "categories";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        this.categoryService.deleteById(id);
        return "redirect:/categories";
    }

    @GetMapping("/edit-form/{id}")
    public String editCategoryPage(@PathVariable Long id, Model model) {
        if (this.categoryService.findById(id).isPresent()) {
            Category category = this.categoryService.findById(id).get();
            model.addAttribute("category", category);
            model.addAttribute("bodyContent", "add-category");
            return "add-category";
        }
        return "redirect:/wines?error=CategoryNotFound";
    }

    @GetMapping("/add-form")
    // TODO: @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addWinePage(Model model) {
        model.addAttribute("bodyContent", "add-category");
        return "add-category";
    }

    @PostMapping("/add")
    public String saveCategory(
            @RequestParam(required = false) Long id,
            @RequestParam String name) {
        if (id != null) {
            this.categoryService.update(id, name);
        } else {
            this.categoryService.create(name);
        }
        return "redirect:/categories";
    }
}
