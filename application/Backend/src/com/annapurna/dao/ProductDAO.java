package com.annapurna.dao;

import java.sql.SQLException;
import java.util.List;

import com.annapurna.model.Product;

public interface ProductDAO {
	Product getProductById(int id) throws SQLException;
    List<Product> getAllProducts() throws SQLException;
    void addProduct(Product product) throws SQLException;
    void updateProduct(Product product) throws SQLException;
    void deleteProduct(int id) throws SQLException;
}
