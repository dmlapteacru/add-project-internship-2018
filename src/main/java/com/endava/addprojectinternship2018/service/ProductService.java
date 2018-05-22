package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.ProductDao;
import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Product;
import com.endava.addprojectinternship2018.model.dto.AdvancedFilter;
import com.endava.addprojectinternship2018.model.dto.ProductDto;
import com.endava.addprojectinternship2018.model.dto.ProductDtoTest;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
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

    private static final Logger LOGGER = Logger.getLogger(ProductService.class);

    public List<Product> getAllByCompanyId(int id) {
        return productDao.findAllByCompanyIdOrderByName(id);
    }

    public List<Product> getAllProducts() {
        return productDao.findAllByOrderByCompanyName();
    }

    public long countAll() {
        return productDao.count();
    }

    public long countByCompanyId(int companyId) {
        return productDao.countByCompanyId(companyId);
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
    public void saveProduct(ProductDto productDto) {
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
    public void save(ProductDtoTest product) {
        productDao.save(convertDTOToProduct(product));
    }

    public Product convertDTOToProduct(ProductDtoTest productDtoTest) {
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

    public List<Product> getAllByCompanyIdFiltered(int companyId, AdvancedFilter filter) {
        double priceFrom = (filter.getSumFrom() == 0 ? Double.MIN_VALUE : filter.getSumFrom());
        double priceTo = (filter.getSumTo() == 0 ? Double.MAX_VALUE : filter.getSumTo());
        return productDao.findAllByCompanyIdAndPriceBetween(companyId, priceFrom, priceTo);
    }

    public List<Product> getAllFiltered(AdvancedFilter filter) {
        double priceFrom = (filter.getSumFrom() == 0 ? Double.MIN_VALUE : filter.getSumFrom());
        double priceTo = (filter.getSumTo() == 0 ? Double.MAX_VALUE : filter.getSumTo());
        return productDao.findAllByPriceBetween(priceFrom, priceTo);
    }

    public ByteArrayInputStream getPriceList(int[] productIds) {

        List<Product> products = new LinkedList<>();
        if (productIds.length == 0) {
            products.addAll(getAllProducts());
        } else {
            for (int productId : productIds) {
                products.add(getById(productId));
            }
        }

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{4, 4, 3, 10});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("Company name", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Service name", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Price", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Description", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            for (Product product : products) {

                PdfPCell cell;

                cell = new PdfPCell(new Phrase(product.getCompany().getName()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(product.getName()));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(product.getPrice())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(product.getDescription()));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
            }

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(table);

            document.close();

        } catch (DocumentException ex) {

            LOGGER.error(ex.getMessage());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

}