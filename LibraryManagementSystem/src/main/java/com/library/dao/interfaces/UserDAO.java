package com.library.dao.interfaces;

import com.library.model.User;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    int create(User user) throws SQLException;
    User findById(int id) throws SQLException;
    User findByEmail(String email) throws SQLException;
    List<User> findAll() throws SQLException;
    List<User> searchUsers(String keyword) throws SQLException;
    boolean update(User user) throws SQLException;
    boolean delete(int id) throws SQLException;
}
