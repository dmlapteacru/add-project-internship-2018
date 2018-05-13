package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.ProductDao;
import com.endava.addprojectinternship2018.model.Product;
import com.endava.addprojectinternship2018.model.dto.ProductDto;
import com.endava.addprojectinternship2018.model.dto.ProductDtoTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CompanyService companyService;

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

    public Optional<Product> getByNameAndCategoryIdAndCompanyId(String name, int categoryId, int companyId) {
        return productDao.findByNameAndCategoryIdAndCompanyId(name, categoryId, companyId);
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

    @Transactional
    public void save(ProductDtoTest product){
        productDao.save(convertDTOToProduct(product));
    }

    public Product convertDTOToProduct(ProductDtoTest productDtoTest){
        Product product = new Product();
        product.setName(productDtoTest.getName());
        product.setDescription(productDtoTest.getDescription());
        product.setPrice(productDtoTest.getPrice());
        product.setCategory(categoryService.getCategoryById(productDtoTest.getCategoryId()));
        product.setCompany(companyService.getCompanyById(productDtoTest.getCompanyId()).get());
        return product;
    }

    public Product getById(int productId) {
        return productDao.findById(productId).get();
    }
}