package com.banking.api.resources;

import com.banking.api.domain.Category;
import com.banking.api.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryResource {

    @Autowired
    CategoryService categoryService;

    @GetMapping(" ")
    public ResponseEntity<List<Category>> getAllCategories(HttpServletRequest request){
        int user_id = (Integer) request.getAttribute("user_id");
        List<Category> categories = categoryService.fetchAllCategories(user_id);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{category_id}")
    public ResponseEntity<Category> getCategoryById(HttpServletRequest request,
                                                    @PathVariable("category_id") Integer categoryId){
        int userId = (Integer) request.getAttribute("user_id");
        Category category = categoryService.fetchCategoryById(userId, categoryId);
        return  new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping(" ")
    public ResponseEntity<Category> addCategory(HttpServletRequest request,
                                                @RequestBody Map<String, Object> categoryMap){
        int userId = (Integer) request.getAttribute("user_id");
        String title = (String) categoryMap.get("title");
        String description = (String) categoryMap.get("description");
        Category category = categoryService.addCategory(userId, title, description);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Map<String, Boolean>> updateCategory(HttpServletRequest request,
                                                               @PathVariable("categoryId") Integer categoryId,
                                                               @RequestBody Category category){
        int userId = (Integer) request.getAttribute("user_id");
        categoryService.updateCategory(userId, categoryId, category);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{category_id}")
    public ResponseEntity<Map<String, Boolean>> deleteCategory(HttpServletRequest request,
                                                               @PathVariable("category_id") Integer categoryId){
        int userId = (Integer) request.getAttribute("user_id");
        categoryService.removeCategoryWithAllTransactions(userId, categoryId);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
