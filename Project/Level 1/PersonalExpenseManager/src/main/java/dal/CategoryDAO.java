package dal;

import model.Category;
import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    
    public List<Category> getAll(String type) throws SQLException {
        String sql = type == null || type.isBlank()
                ? "SELECT id, name, type, created_at, updated_at FROM categories ORDER BY type, name"
                : "SELECT id, name, type, created_at, updated_at FROM categories WHERE type = ? ORDER BY name";
        List<Category> categories = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (type != null && !type.isBlank()) {
                statement.setString(1, type);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    categories.add(mapRow(resultSet));
                }
            }
        }
        return categories;
    }

    public Category getById(int id) throws SQLException {
        String sql = "SELECT id, name, type, created_at, updated_at FROM categories WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }
        return null;
    }

    public int insert(Category category) throws SQLException {
        String sql = "INSERT INTO categories (name, type) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getType());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public boolean update(Category category) throws SQLException {
        String sql = "UPDATE categories SET name = ?, type = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getType());
            statement.setInt(3, category.getId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean existsByNameAndType(String name, String type, Integer excludeId) throws SQLException {
        String sql = excludeId == null
                ? "SELECT 1 FROM categories WHERE name = ? AND type = ?"
                : "SELECT 1 FROM categories WHERE name = ? AND type = ? AND id <> ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, type);
            if (excludeId != null) {
                statement.setInt(3, excludeId);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private Category mapRow(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        category.setId(resultSet.getInt("id"));
        category.setName(resultSet.getString("name"));
        category.setType(resultSet.getString("type"));
        category.setCreatedAt(resultSet.getTimestamp("created_at").toString());
        category.setUpdatedAt(resultSet.getTimestamp("updated_at").toString());
        return category;
    }
}
