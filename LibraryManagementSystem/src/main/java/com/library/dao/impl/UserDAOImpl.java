package com.library.dao.impl;

import com.library.dao.interfaces.UserDAO;
import com.library.model.User;
import com.library.util.DatabaseConnection;

import java.sql.*;
import java.util.List;
import com.library.model.enums.UserType;
import com.library.model.enums.UserStatus;

public class UserDAOImpl implements UserDAO {


    @Override
    public User findById(int id) throws SQLException {
        return null;
    }

    @Override
    public User findByEmail(String email) throws SQLException {
        return null;
    }

    @Override
    public List<User> findAll() throws SQLException {
        return List.of();
    }

    @Override
    public List<User> searchUsers(String keyword) throws SQLException {
        return List.of();
    }

    @Override
    public boolean update(User user) throws SQLException {
        return false;
    }



    @Override
    public int create(User user) throws SQLException {
        String sql = """
            INSERT INTO users (first_name, last_name, email, phone_number, 
                             user_type, status, registration_date) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPhoneNumber());
            pstmt.setString(5, user.getUserType().name());
            pstmt.setString(6, user.getStatus().name());
            pstmt.setInt(7, user.getUserId());

            return pstmt.executeUpdate() ;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "UPDATE users SET status = 'DELETED', updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setUserType(UserType.valueOf(rs.getString("user_type")));
        user.setStatus(UserStatus.valueOf(rs.getString("status")));
        user.setRegistrationDate(rs.getDate("registration_date").toLocalDate());
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime().toLocalDate());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime().toLocalDate());
        return user;
    }
}
