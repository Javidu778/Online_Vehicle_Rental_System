<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.rental.model.Booking" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Bookings - Vehicle Rental System</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h2>My Bookings</h2>
            <div class="user-info">
                Welcome, <%= session.getAttribute("user") != null ? ((com.rental.model.User)session.getAttribute("user")).getFullName() : "Customer" %>
                <a href="../logout" class="logout-btn">Logout</a>
            </div>
        </header>

        <% if (request.getAttribute("message") != null) { %>
            <div class="success-message">
                <%= request.getAttribute("message") %>
            </div>
        <% } %>

        <section class="bookings-list">
            <div class="section-header">
                <h3>Booking History</h3>
                <a href="book-vehicle" class="btn btn-primary">New Booking</a>
            </div>

            <table>
                <thead>
                    <tr>
                        <th>Booking ID</th>
                        <th>Vehicle</th>
                        <th>Pickup Date</th>
                        <th>Return Date</th>
                        <th>Total Amount</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                    List<Booking> bookings = (List<Booking>)request.getAttribute("bookings");
                    if (bookings != null && !bookings.isEmpty()) {
                        for (Booking booking : bookings) {
                    %>
                        <tr>
                            <td><%= booking.getBookingId() %></td>
                            <td>
                                <% if (booking.getVehicle() != null) { %>
                                    <%= booking.getVehicle().getBrand() %> 
                                    <%= booking.getVehicle().getModel() %>
                                <% } else { %>
                                    Pending Assignment
                                <% } %>
                            </td>
                            <td><%= booking.getPickupDate() %></td>
                            <td><%= booking.getReturnDate() %></td>
                            <td>$<%= booking.getTotalAmount() %></td>
                            <td>
                                <span class="status-badge status-<%= booking.getStatus().toLowerCase() %>">
                                    <%= booking.getStatus() %>
                                </span>
                            </td>
                        </tr>
                    <% 
                        }
                    } else {
                    %>
                        <tr>
                            <td colspan="6" class="no-data">No bookings found</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </section>
    </div>
</body>
</html> 