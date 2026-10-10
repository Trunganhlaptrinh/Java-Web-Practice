package dal;

import model.RentalRequest;
import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RentalRequestDAO {

    private static final String SELECT_JOIN = 
        "SELECT req.*, r.title AS room_title, u.full_name AS renter_name, u.phone AS renter_phone " +
        "FROM rental_requests req " +
        "JOIN rooms r ON req.room_id = r.id " +
        "JOIN users u ON req.renter_id = u.id ";

    public List<RentalRequest> getByLandlord(int landlordId) throws SQLException {
        String sql = SELECT_JOIN + "WHERE r.landlord_id = ? ORDER BY req.created_at DESC";
        List<RentalRequest> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, landlordId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<RentalRequest> getByRenter(int renterId) throws SQLException {
        String sql = SELECT_JOIN + "WHERE req.renter_id = ? ORDER BY req.created_at DESC";
        List<RentalRequest> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, renterId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public RentalRequest getById(int id) throws SQLException {
        String sql = SELECT_JOIN + "WHERE req.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public boolean exists(int roomId, int renterId) throws SQLException {
        String sql = "SELECT 1 FROM rental_requests WHERE room_id = ? AND renter_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setInt(2, renterId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int insert(RentalRequest req) throws SQLException {
        String sql = "INSERT INTO rental_requests (room_id, renter_id, message, status) VALUES (?, ?, ?, 'PENDING')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, req.getRoomId());
            ps.setInt(2, req.getRenterId());
            ps.setString(3, req.getMessage());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    public boolean updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE rental_requests SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    private RentalRequest mapRow(ResultSet rs) throws SQLException {
        RentalRequest req = new RentalRequest();
        req.setId(rs.getInt("id"));
        req.setRoomId(rs.getInt("room_id"));
        req.setRenterId(rs.getInt("renter_id"));
        req.setMessage(rs.getString("message"));
        req.setStatus(rs.getString("status"));
        req.setRoomTitle(rs.getString("room_title"));
        req.setRenterName(rs.getString("renter_name"));
        req.setRenterPhone(rs.getString("renter_phone"));
        if (rs.getTimestamp("created_at") != null) req.setCreatedAt(rs.getTimestamp("created_at").toString());
        if (rs.getTimestamp("updated_at") != null) req.setUpdatedAt(rs.getTimestamp("updated_at").toString());
        return req;
    }
}

