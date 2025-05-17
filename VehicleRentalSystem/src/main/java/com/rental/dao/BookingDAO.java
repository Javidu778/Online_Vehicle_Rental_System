package com.rental.dao;

import com.rental.model.Booking;
import com.rental.model.User;
import com.rental.model.Vehicle;
import com.rental.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    
    public List<Booking> getPendingBookings() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.*, u.*, v.* FROM bookings b " +
                      "LEFT JOIN users u ON b.customer_id = u.user_id " +
                      "LEFT JOIN vehicles v ON b.vehicle_id = v.vehicle_id " +
                      "WHERE b.status = 'PENDING'";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    
    public Booking getBookingById(int bookingId) {
        String query = "SELECT b.*, u.*, v.* FROM bookings b " +
                      "LEFT JOIN users u ON b.customer_id = u.user_id " +
                      "LEFT JOIN vehicles v ON b.vehicle_id = v.vehicle_id " +
                      "WHERE b.booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, bookingId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractBookingFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean createBooking(Booking booking) {
        String query = "INSERT INTO bookings (customer_id, vehicle_id, pickup_date, return_date, total_amount, status) " +
                      "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, booking.getCustomerId());
            pstmt.setInt(2, booking.getVehicleId());
            pstmt.setDate(3, booking.getPickupDate());
            pstmt.setDate(4, booking.getReturnDate());
            pstmt.setBigDecimal(5, booking.getTotalAmount());
            pstmt.setString(6, booking.getStatus());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateBookingStatus(int bookingId, String status) {
        String query = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, bookingId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean assignVehicleToBooking(int bookingId, int vehicleId) {
        String query = "UPDATE bookings SET vehicle_id = ?, status = 'CONFIRMED' WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, vehicleId);
            pstmt.setInt(2, bookingId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Booking> getCustomerBookings(int customerId) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.*, u.*, v.* FROM bookings b " +
                      "LEFT JOIN users u ON b.customer_id = u.user_id " +
                      "LEFT JOIN vehicles v ON b.vehicle_id = v.vehicle_id " +
                      "WHERE b.customer_id = ? " +
                      "ORDER BY b.booking_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(extractBookingFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    
    private Booking extractBookingFromResultSet(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(rs.getInt("booking_id"));
        booking.setCustomerId(rs.getInt("customer_id"));
        booking.setVehicleId(rs.getInt("vehicle_id"));
        booking.setBookingDate(rs.getTimestamp("booking_date"));
        booking.setPickupDate(rs.getDate("pickup_date"));
        booking.setReturnDate(rs.getDate("return_date"));
        booking.setTotalAmount(rs.getBigDecimal("total_amount"));
        booking.setStatus(rs.getString("status"));
        
        // Set customer details if available
        if (rs.getString("username") != null) {
            User customer = new User();
            customer.setUserId(rs.getInt("user_id"));
            customer.setUsername(rs.getString("username"));
            customer.setFullName(rs.getString("full_name"));
            customer.setEmail(rs.getString("email"));
            booking.setCustomer(customer);
        }
        
        // Set vehicle details if available
        if (rs.getString("vehicle_type") != null) {
            Vehicle vehicle = new Vehicle();
            vehicle.setVehicleId(rs.getInt("vehicle_id"));
            vehicle.setVehicleType(rs.getString("vehicle_type"));
            vehicle.setBrand(rs.getString("brand"));
            vehicle.setModel(rs.getString("model"));
            booking.setVehicle(vehicle);
        }
        
        return booking;
    }
} 