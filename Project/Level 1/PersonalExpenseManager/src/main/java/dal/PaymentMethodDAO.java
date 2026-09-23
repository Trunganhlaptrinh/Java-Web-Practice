package dal;

import model.PaymentMethod;
import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethodDAO {
    public List<PaymentMethod> getAll() throws SQLException {
        String sql = "SELECT id, name, created_at, updated_at FROM payment_methods ORDER BY name";
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                paymentMethods.add(mapRow(resultSet));
            }
        }
        return paymentMethods;
    }

    public PaymentMethod getById(int id) throws SQLException {
        String sql = "SELECT id, name, created_at, updated_at FROM payment_methods WHERE id = ?";
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

    public int insert(PaymentMethod paymentMethod) throws SQLException {
        String sql = "INSERT INTO payment_methods (name) VALUES (?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, paymentMethod.getName());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public boolean update(PaymentMethod paymentMethod) throws SQLException {
        String sql = "UPDATE payment_methods SET name = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, paymentMethod.getName());
            statement.setInt(2, paymentMethod.getId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM payment_methods WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean existsByName(String name, Integer excludeId) throws SQLException {
        String sql = excludeId == null
                ? "SELECT 1 FROM payment_methods WHERE name = ?"
                : "SELECT 1 FROM payment_methods WHERE name = ? AND id <> ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            if (excludeId != null) {
                statement.setInt(2, excludeId);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private PaymentMethod mapRow(ResultSet resultSet) throws SQLException {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(resultSet.getInt("id"));
        paymentMethod.setName(resultSet.getString("name"));
        paymentMethod.setCreatedAt(resultSet.getTimestamp("created_at").toString());
        paymentMethod.setUpdatedAt(resultSet.getTimestamp("updated_at").toString());
        return paymentMethod;
    }
}
