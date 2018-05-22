package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.CategoryDao;
import com.endava.addprojectinternship2018.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    public List<Category> getAllCategory(){
        return categoryDao.findAll();
    }

    public void saveCategory(Category category){
        categoryDao.save(category);
    }

    public void deleteCategory(int id){
        categoryDao.deleteById(id);
    }

    public Category getCategoryById(int id){
        return categoryDao.getOne(id);
    }

    public Optional<Category> getCategoryByName(String name) {
       return categoryDao.findByName(name);
    }
}
