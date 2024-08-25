package com.annapurna.service;

import java.sql.SQLException;
import java.util.List;

import com.annapurna.dao.AddressDAO;
import com.annapurna.model.Address;

public class AddressService {
    private AddressDAO addressDAO;

    public AddressService(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    public int addAddress(Address address) throws SQLException {
        return addressDAO.addAddress(address);
    }

    public List<Address> getAddressesByUserId(int userId) throws SQLException {
        return addressDAO.getAddressesByUserId(userId);
    }

    public List<Address> getAddresses() throws SQLException{
    	return addressDAO.getAddresses();
    }
    
    public int getMaxId() throws SQLException{
    	return addressDAO.getMaxId();
    }
}
