package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.CategoryDao;
import com.endava.addprojectinternship2018.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
//        Optional<Category> oldCategory = categoryDao.findById(category.getId());
//        if (oldCategory.isPresent()){
//            oldCategory.get().setName(category.getName());
//            categoryDao.save(oldCategory.get());
//        } else {
//            categoryDao.save(category);
//        }
        categoryDao.save(category);
    }

    public void deleteCategory(int id){
        categoryDao.deleteById(id);
    }
}
