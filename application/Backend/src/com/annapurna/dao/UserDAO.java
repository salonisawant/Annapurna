package com.annapurna.dao;

import java.sql.SQLException;

import com.annapurna.model.User;

public interface UserDAO {
	User getUserById(int id) throws SQLException;
    User getUserByEmail(String email) throws SQLException;
    void addUser(User user) throws SQLException;
    void updateUser(User user) throws SQLException;
    void deleteUser(int id) throws SQLException;
}
