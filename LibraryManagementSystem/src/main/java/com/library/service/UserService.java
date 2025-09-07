package com.library.service;

import com.library.dao.impl.UserDAOImpl;
import com.library.dao.interfaces.UserDAO;
import com.library.exception.LibraryException;
import com.library.exception.UserNotFoundException;
import com.library.model.User;
import com.library.util.ValidationUtil;

import java.sql.SQLException;
import java.util.List;

public class UserService  {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAOImpl();
    }

    public int registerUser(User user) throws LibraryException {
        try {
            // Validate user data
            validateUser(user);

            // Check if email already exists
            User existingUser = userDAO.findByEmail(user.getEmail());
            if (existingUser != null) {
                throw new LibraryException("User with email " + user.getEmail() + " already exists");
            }

            return userDAO.create(user);
        } catch (SQLException e) {
            throw new LibraryException("Error registering user: " + e.getMessage(), e);
        }
    }

    public User findUser(int userId) throws UserNotFoundException, LibraryException {
        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                throw new UserNotFoundException("User with ID " + userId + " not found");
            }
            return user;
        } catch (SQLException e) {
            throw new LibraryException("Error finding user: " + e.getMessage(), e);
        }
    }

    public User findUserByEmail(String email) throws UserNotFoundException, LibraryException {
        try {
            User user = userDAO.findByEmail(email);
            if (user == null) {
                throw new UserNotFoundException("User with email " + email + " not found");
            }
            return user;
        } catch (SQLException e) {
            throw new LibraryException("Error finding user: " + e.getMessage(), e);
        }
    }

    public List<User> searchUsers(String keyword) throws LibraryException {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return userDAO.findAll();
            }
            return userDAO.searchUsers(keyword.trim());
        } catch (SQLException e) {
            throw new LibraryException("Error searching users: " + e.getMessage(), e);
        }
    }

    public List<User> getAllUsers() throws LibraryException {
        try {
            return userDAO.findAll();
        } catch (SQLException e) {
            throw new LibraryException("Error retrieving users: " + e.getMessage(), e);
        }
    }

    public boolean updateUser(User user) throws LibraryException {
        try {
            validateUser(user);

            // Check if user exists
            User existingUser = userDAO.findById(user.getUserId());
            if (existingUser == null) {
                throw new UserNotFoundException("User with ID " + user.getUserId() + " not found");
            }

            return userDAO.update(user);
        } catch (SQLException e) {
            throw new LibraryException("Error updating user: " + e.getMessage(), e);
        }
    }

    public boolean deleteUser(int userId) throws LibraryException {
        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                throw new UserNotFoundException("User with ID " + userId + " not found");
            }

            return userDAO.delete(userId);
        } catch (SQLException e) {
            throw new LibraryException("Error deleting user: " + e.getMessage(), e);
        }
    }

    private void validateUser(User user) throws LibraryException {
        if (!ValidationUtil.isNotEmpty(user.getFirstName())) {
            throw new LibraryException("First name is required");
        }
        if (!ValidationUtil.isNotEmpty(user.getLastName())) {
            throw new LibraryException("Last name is required");
        }
        if (!ValidationUtil.isValidEmail(user.getEmail())) {
            throw new LibraryException("Invalid email format");
        }
        if (user.getPhoneNumber() != null && !ValidationUtil.isValidPhone(user.getPhoneNumber())) {
            throw new LibraryException("Invalid phone number format");
        }
    }

}
