package com.annapurna.service;

import java.sql.SQLException;
import java.util.List;

import com.annapurna.dao.ProductDAO;
import com.annapurna.model.Product;

public class ProductService {
    private ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> getAllProducts() throws SQLException {
        return productDAO.getAllProducts();
    }

    public void addProduct(Product product) throws SQLException {
        productDAO.addProduct(product);
    }

    public void updateProduct(Product product) throws SQLException {
        productDAO.updateProduct(product);
    }

    public void deleteProduct(int productId) throws SQLException {
        productDAO.deleteProduct(productId);
    }

	public Product getProductById(int productId) throws SQLException {
		// TODO Auto-generated method stub
		return productDAO.getProductById(productId);
	}

    
}
