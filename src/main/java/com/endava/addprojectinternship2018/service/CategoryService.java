package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.CategoryDao;
import com.endava.addprojectinternship2018.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryDao categoryDao;

    @Autowired
    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public List<Category> getAllCategory(){
        return categoryDao.findAll();
    }

    @Transactional
    public void saveCategory(Category category){
        categoryDao.save(category);
    }

    @Transactional
    public void deleteCategory(int id){
        categoryDao.deleteById(id);
    }

    public Category getCategoryById(int id){
        return categoryDao.findById(id).get();
    }

    public Optional<Category> getCategoryByName(String name) {
       return categoryDao.findByName(name);
    }
}
