package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.ProductDao;
import com.endava.addprojectinternship2018.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public List<Product> getAllByCompanyId(int id) {
        return productDao.findAllByCompanyId(id);
    }

    public List<Product> getAllProducts(){
        return productDao.findAll();
    }

    public void saveProduct(Product product){
        Optional<Product> oldProduct = productDao.findById(product.getId());
        if (oldProduct.isPresent()){
            oldProduct.get().setName(product.getName());
            oldProduct.get().setDescription(product.getDescription());
            productDao.save(oldProduct.get());
        } else {
            productDao.save(product);
        }
    }

    public void deleteProduct(int id) {
        productDao.deleteById(id);
    }
}