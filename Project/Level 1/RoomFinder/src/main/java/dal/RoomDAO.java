package dal;

import model.Room;
import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    private static final String SELECT_JOIN = 
        "SELECT r.*, u.full_name AS landlord_name, u.phone AS landlord_phone " +
        "FROM rooms r JOIN users u ON r.landlord_id = u.id ";

    public List<Room> getAll(String search, Integer landlordId, String status) throws SQLException {
        StringBuilder sql = new StringBuilder(SELECT_JOIN).append("WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (r.title LIKE ? OR r.address LIKE ?) ");
            params.add("%" + search.trim() + "%");
            params.add("%" + search.trim() + "%");
        }
        if (landlordId != null) {
            sql.append("AND r.landlord_id = ? ");
            params.add(landlordId);
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND r.status = ? ");
            params.add(status);
        }
        sql.append("ORDER BY r.created_at DESC");

        List<Room> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Room getById(int id) throws SQLException {
        String sql = SELECT_JOIN + "WHERE r.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public int insert(Room r) throws SQLException {
        String sql = "INSERT INTO rooms (landlord_id, title, description, price, area, address, image_url, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, 'AVAILABLE')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getLandlordId());
            ps.setString(2, r.getTitle());
            ps.setString(3, r.getDescription());
            ps.setLong(4, r.getPrice());
            ps.setFloat(5, r.getArea());
            ps.setString(6, r.getAddress());
            ps.setString(7, r.getImageUrl());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    public boolean update(Room r) throws SQLException {
        String sql = "UPDATE rooms SET title = ?, description = ?, price = ?, area = ?, address = ?, image_url = ? WHERE id = ? AND landlord_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getTitle());
            ps.setString(2, r.getDescription());
            ps.setLong(3, r.getPrice());
            ps.setFloat(4, r.getArea());
            ps.setString(5, r.getAddress());
            ps.setString(6, r.getImageUrl());
            ps.setInt(7, r.getId());
            ps.setInt(8, r.getLandlordId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateStatus(int id, int landlordId, String status) throws SQLException {
        String sql = "UPDATE rooms SET status = ? WHERE id = ? AND landlord_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.setInt(3, landlordId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id, int landlordId) throws SQLException {
        String sql = "DELETE FROM rooms WHERE id = ? AND landlord_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, landlordId);
            return ps.executeUpdate() > 0;
        }
    }

    private Room mapRow(ResultSet rs) throws SQLException {
        Room r = new Room();
        r.setId(rs.getInt("id"));
        r.setLandlordId(rs.getInt("landlord_id"));
        r.setTitle(rs.getString("title"));
        r.setDescription(rs.getString("description"));
        r.setPrice(rs.getLong("price"));
        r.setArea(rs.getFloat("area"));
        r.setAddress(rs.getString("address"));
        r.setImageUrl(rs.getString("image_url"));
        r.setStatus(rs.getString("status"));
        r.setLandlordName(rs.getString("landlord_name"));
        r.setLandlordPhone(rs.getString("landlord_phone"));
        if (rs.getTimestamp("created_at") != null) r.setCreatedAt(rs.getTimestamp("created_at").toString());
        if (rs.getTimestamp("updated_at") != null) r.setUpdatedAt(rs.getTimestamp("updated_at").toString());
        return r;
    }
}

