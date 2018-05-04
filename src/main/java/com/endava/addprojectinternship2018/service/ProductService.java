package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.ProductDao;
import com.endava.addprojectinternship2018.model.Product;
import com.endava.addprojectinternship2018.model.dto.ProductDto;
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
            productDao.save(oldProduct.get());
        } else {
            productDao.save(product);
        }
    }

    public void deleteProduct(int id) {
        productDao.deleteById(id);
    }

    public Product convertProductDtoToProduct(ProductDto productDto) {
        Product product = productDao.findById(productDto.getProductId())
                .orElseGet(Product::new);
        product.setName(productDto.getName());
        product.setCategory(productDto.getCategory());
        product.setCompany(productDto.getCompany());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        return product;
    }

    public ProductDto convertProductToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setProductId(product.getId());
        productDto.setCategory(product.getCategory());
        productDto.setCompany(product.getCompany());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        return productDto;
    }
}