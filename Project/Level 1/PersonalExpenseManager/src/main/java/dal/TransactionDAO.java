package dal;

import model.Transaction;
import util.DBConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TransactionDAO {
    private static final String SELECT_COLUMNS = ""
            + "t.id, t.amount, t.type, t.description, t.transaction_date, "
            + "t.category_id, t.payment_method_id, t.note, t.created_at, t.updated_at, "
            + "c.name AS category_name, pm.name AS payment_method_name ";

    public List<Transaction> search(String search, String type, Integer categoryId,
                                    Integer paymentMethodId, LocalDate fromDate, LocalDate toDate)
            throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT ")
                .append(SELECT_COLUMNS)
                .append("FROM transactions t ")
                .append("LEFT JOIN categories c ON c.id = t.category_id ")
                .append("LEFT JOIN payment_methods pm ON pm.id = t.payment_method_id ")
                .append("WHERE 1 = 1");
        List<Object> parameters = new ArrayList<>();

        if (search != null && !search.isBlank()) {
            sql.append(" AND t.description LIKE ?");
            parameters.add("%" + search.trim() + "%");
        }
        if (type != null && !type.isBlank()) {
            sql.append(" AND t.type = ?");
            parameters.add(type);
        }
        if (categoryId != null) {
            sql.append(" AND t.category_id = ?");
            parameters.add(categoryId);
        }
        if (paymentMethodId != null) {
            sql.append(" AND t.payment_method_id = ?");
            parameters.add(paymentMethodId);
        }
        if (fromDate != null) {
            sql.append(" AND t.transaction_date >= ?");
            parameters.add(Date.valueOf(fromDate));
        }
        if (toDate != null) {
            sql.append(" AND t.transaction_date <= ?");
            parameters.add(Date.valueOf(toDate));
        }
        sql.append(" ORDER BY t.transaction_date DESC, t.id DESC");

        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            setParameters(statement, parameters);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(mapRow(resultSet));
                }
            }
        }
        return transactions;
    }

    public Transaction getById(long id) throws SQLException {
        String sql = "SELECT " + SELECT_COLUMNS
                + "FROM transactions t "
                + "LEFT JOIN categories c ON c.id = t.category_id "
                + "LEFT JOIN payment_methods pm ON pm.id = t.payment_method_id "
                + "WHERE t.id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }
        return null;
    }

    public long insert(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions "
                + "(amount, type, description, transaction_date, category_id, payment_method_id, note) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setTransactionParameters(statement, transaction, false);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        }
        return -1;
    }

    public boolean update(Transaction transaction) throws SQLException {
        String sql = "UPDATE transactions SET amount = ?, type = ?, description = ?, "
                + "transaction_date = ?, category_id = ?, payment_method_id = ?, note = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setTransactionParameters(statement, transaction, true);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public Map<String, Long> getTotals(LocalDate fromDate, LocalDate toDate) throws SQLException {
        String sql = "SELECT type, COALESCE(SUM(amount), 0) AS total FROM transactions "
                + "WHERE transaction_date BETWEEN ? AND ? GROUP BY type";
        Map<String, Long> totals = new LinkedHashMap<>();
        totals.put("income", 0L);
        totals.put("expense", 0L);
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, Date.valueOf(fromDate));
            statement.setDate(2, Date.valueOf(toDate));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String type = resultSet.getString("type");
                    totals.put(type.equals("INCOME") ? "income" : "expense",
                            resultSet.getLong("total"));
                }
            }
        }
        totals.put("balance", totals.get("income") - totals.get("expense"));
        return totals;
    }

    public List<Map<String, Object>> getTotalsByCategory(LocalDate fromDate, LocalDate toDate)
            throws SQLException {
        String sql = "SELECT COALESCE(c.name, 'Không phân loại') AS category_name, "
                + "t.type, SUM(t.amount) AS total "
                + "FROM transactions t LEFT JOIN categories c ON c.id = t.category_id "
                + "WHERE t.transaction_date BETWEEN ? AND ? "
                + "GROUP BY c.name, t.type ORDER BY total DESC";
        List<Map<String, Object>> result = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, Date.valueOf(fromDate));
            statement.setDate(2, Date.valueOf(toDate));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("categoryName", resultSet.getString("category_name"));
                    row.put("type", resultSet.getString("type"));
                    row.put("total", resultSet.getLong("total"));
                    result.add(row);
                }
            }
        }
        return result;
    }

    private void setTransactionParameters(PreparedStatement statement, Transaction transaction,
                                          boolean includeId) throws SQLException {
        statement.setLong(1, transaction.getAmount());
        statement.setString(2, transaction.getType());
        statement.setString(3, transaction.getDescription());
        statement.setDate(4, Date.valueOf(transaction.getTransactionDate()));
        setNullableInt(statement, 5, transaction.getCategoryId());
        setNullableInt(statement, 6, transaction.getPaymentMethodId());
        statement.setString(7, transaction.getNote());
        if (includeId) {
            statement.setLong(8, transaction.getId());
        }
    }

    private void setNullableInt(PreparedStatement statement, int index, Integer value)
            throws SQLException {
        if (value == null) {
            statement.setNull(index, java.sql.Types.INTEGER);
        } else {
            statement.setInt(index, value);
        }
    }

    private void setParameters(PreparedStatement statement, List<Object> parameters)
            throws SQLException {
        for (int index = 0; index < parameters.size(); index++) {
            statement.setObject(index + 1, parameters.get(index));
        }
    }

    private Transaction mapRow(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(resultSet.getLong("id"));
        transaction.setAmount(resultSet.getLong("amount"));
        transaction.setType(resultSet.getString("type"));
        transaction.setDescription(resultSet.getString("description"));
        transaction.setTransactionDate(resultSet.getDate("transaction_date").toString());
        transaction.setCategoryId((Integer) resultSet.getObject("category_id"));
        transaction.setPaymentMethodId((Integer) resultSet.getObject("payment_method_id"));
        transaction.setNote(resultSet.getString("note"));
        transaction.setCategoryName(resultSet.getString("category_name"));
        transaction.setPaymentMethodName(resultSet.getString("payment_method_name"));
        transaction.setCreatedAt(resultSet.getTimestamp("created_at").toString());
        transaction.setUpdatedAt(resultSet.getTimestamp("updated_at").toString());
        return transaction;
    }
}
