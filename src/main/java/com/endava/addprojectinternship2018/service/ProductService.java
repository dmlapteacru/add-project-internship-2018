package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.ProductDao;
import com.endava.addprojectinternship2018.model.Product;
import com.endava.addprojectinternship2018.model.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Product> getAllByCategoryId(int categoryId) {
        return productDao.findAllByCategoryId(categoryId);
    }

    public List<Product> getAllByCompanyIdAndCategoryId(int companyId, int categoryId) {
        return productDao.findAllByCompanyIdAndCategoryId(companyId, categoryId);
    }

    public Optional<Product> getByNameAndCompanyId(String name, int companyId) {
        return productDao.findByNameAndCompanyId(name, companyId);
    }

    public Optional<Product> getByNameAndCategoryId(String name, int categoryId) {
        return productDao.findByNameAndCategoryId(name, categoryId);
    }

    @Transactional
    public void saveProduct(ProductDto productDto){
        productDao.save(convertProductDtoToProduct(productDto));
    }

    @Transactional
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