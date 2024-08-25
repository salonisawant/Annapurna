package com.annapurna.dao;

import java.sql.SQLException;
import java.util.List;

import com.annapurna.model.Address;

public interface AddressDAO {
	Address getAddressById(int id) throws SQLException;
    List<Address> getAddressesByUserId(int userId) throws SQLException;
    int addAddress(Address address) throws SQLException;
    void updateAddress(Address address) throws SQLException;
    void deleteAddress(int id) throws SQLException;
    List<Address> getAddresses() throws SQLException;
    int getMaxId() throws SQLException;
}
