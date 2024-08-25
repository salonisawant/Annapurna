package com.annapurna.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.annapurna.model.Address;

public class AddressDAOImpl implements AddressDAO {
    private Connection connection;

    public AddressDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Address getAddressById(int id) throws SQLException {
        String query = "SELECT * FROM addresses WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractAddressFromResultSet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Address> getAddressesByUserId(int userId) throws SQLException {
        List<Address> addresses = new ArrayList<>();
        String query = "SELECT * FROM addresses WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    addresses.add(extractAddressFromResultSet(rs));
                }
            }
        }
        return addresses;
    }

    @Override
    public int addAddress(Address address) throws SQLException {
        String query = "INSERT INTO addresses (user_id, address_line1, address_line2, city, state, postal_code, country) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, address.getUserId());
            pstmt.setString(2, address.getAddressLine1());
            pstmt.setString(3, address.getAddressLine2());
            pstmt.setString(4, address.getCity());
            pstmt.setString(5, address.getState());
            pstmt.setString(6, address.getPostalCode());
            pstmt.setString(7, address.getCountry());
            
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating address failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newId = generatedKeys.getInt(1);
                    address.setId(newId);
                    return newId;
                } else {
                    throw new SQLException("Creating address failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public void updateAddress(Address address) throws SQLException {
        String query = "UPDATE addresses SET address_line1 = ?, address_line2 = ?, city = ?, state = ?, postal_code = ?, country = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, address.getAddressLine1());
            pstmt.setString(2, address.getAddressLine2());
            pstmt.setString(3, address.getCity());
            pstmt.setString(4, address.getState());
            pstmt.setString(5, address.getPostalCode());
            pstmt.setString(6, address.getCountry());
            pstmt.setInt(7, address.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteAddress(int id) throws SQLException {
        String query = "DELETE FROM addresses WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
    
    public List<Address> getAddresses() throws SQLException{
    	List<Address>address = new ArrayList<>();
    	String query = "Select * from addresses";
    	try(PreparedStatement pstmt = connection.prepareStatement(query)){
    		ResultSet rs = pstmt.executeQuery();
    		while(rs.next()) {
    			address.add(extractAddressFromResultSet(rs));
    		}
    	}
    	return address;
    }
    
    public int getMaxId() throws SQLException{
    	String query = "Select max(id) from addresses";
    	int address_id = 0;
    	try(PreparedStatement pstmt = connection.prepareStatement(query)){
    		ResultSet rs = pstmt.executeQuery();
    		while(rs.next()) {
    			address_id = rs.getInt("id");
    		}
    	}
    	return address_id;
    }
    private Address extractAddressFromResultSet(ResultSet rs) throws SQLException {
        Address address = new Address();
        address.setId(rs.getInt("id"));
        address.setUserId(rs.getInt("user_id"));
        address.setAddressLine1(rs.getString("address_line1"));
        address.setAddressLine2(rs.getString("address_line2"));
        address.setCity(rs.getString("city"));
        address.setState(rs.getString("state"));
        address.setPostalCode(rs.getString("postal_code"));
        address.setCountry(rs.getString("country"));
        return address;
    }
}