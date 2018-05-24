package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.CategoryDao;
import com.endava.addprojectinternship2018.dao.CompanyDao;
import com.endava.addprojectinternship2018.dao.ContractDao;
import com.endava.addprojectinternship2018.dao.ProductDao;
import com.endava.addprojectinternship2018.model.Product;
import com.endava.addprojectinternship2018.model.dto.AdvancedFilter;
import com.endava.addprojectinternship2018.model.dto.ProductDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.security.Escape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.List;

@Service
public class ProductService {

    private final ProductDao productDao;
    private final CategoryDao categoryDao;
    private final CompanyDao companyDao;
    private final ContractDao contractDao;

    private static final Logger LOGGER = Logger.getLogger(ProductService.class);

    @Autowired
    public ProductService(ProductDao productDao, CategoryDao categoryDao,
                          CompanyDao companyDao, ContractDao contractDao) {
        this.productDao = productDao;
        this.categoryDao = categoryDao;
        this.companyDao = companyDao;
        this.contractDao = contractDao;
    }

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

    public Optional<Product> getByNameAndCategoryIdAndCompanyId(String name, int categoryId, int companyId) {
        return productDao.findByNameAndCategoryIdAndCompanyId(name, categoryId, companyId);
    }

    @Transactional
    public Set<String> deleteProduct(int productId) {

        Set<String> result = new HashSet<>();

        if (isInContracts(productId)) {
            result.add("product has active contracts");
            return result;
        }

        try {
            productDao.deleteById(productId);
            result.add("OK");
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        return result;
    }

    @Transactional
    public void save(ProductDto product) {
        productDao.save(convertDTOToProduct(product));
    }

    public Product convertDTOToProduct(ProductDto productDto) {
        Optional<Product> productOptional = productDao.findById(productDto.getProductId());
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setDescription(productDto.getDescription());
            return product;
        } else {
            Product product = new Product();
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setCategory(categoryDao.findById(productDto.getCategoryId()).get());
            product.setCompany(companyDao.findById(productDto.getCompanyId()).get());
            return product;
        }
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

    public boolean isInContracts(int productId) {
        return contractDao.countByProductId(productId) > 0;
    }

}