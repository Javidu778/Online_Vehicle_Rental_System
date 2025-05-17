<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.rental.model.Booking" %>
<%@ page import="com.rental.model.Vehicle" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Agent Dashboard - Vehicle Rental System</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h2>Agent Dashboard</h2>
            <div class="user-info">
                Welcome, <%= session.getAttribute("user") != null ? ((com.rental.model.User)session.getAttribute("user")).getFullName() : "Agent" %>
                <a href="../logout" class="logout-btn">Logout</a>
            </div>
        </header>

        <% if (request.getAttribute("message") != null) { %>
            <div class="success-message">
                <%= request.getAttribute("message") %>
            </div>
        <% } %>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="error-message">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <section class="pending-bookings">
            <h3>Pending Bookings</h3>
            <table>
                <thead>
                    <tr>
                        <th>Booking ID</th>
                        <th>Customer</th>
                        <th>Pickup Date</th>
                        <th>Return Date</th>
                        <th>Total Amount</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                    List<Booking> pendingBookings = (List<Booking>)request.getAttribute("pendingBookings");
                    if (pendingBookings != null) {
                        for (Booking booking : pendingBookings) {
                    %>
                        <tr>
                            <td><%= booking.getBookingId() %></td>
                            <td><%= booking.getCustomer().getFullName() %></td>
                            <td><%= booking.getPickupDate() %></td>
                            <td><%= booking.getReturnDate() %></td>
                            <td>$<%= booking.getTotalAmount() %></td>
                            <td>
                                <a href="assign-vehicle?bookingId=<%= booking.getBookingId() %>" 
                                   class="btn btn-primary">Assign Vehicle</a>
                            </td>
                        </tr>
                    <% 
                        }
                    }
                    %>
                </tbody>
            </table>
        </section>

        <section class="available-vehicles">
            <h3>Available Vehicles</h3>
            <table>
                <thead>
                    <tr>
                        <th>Vehicle ID</th>
                        <th>Type</th>
                        <th>Brand</th>
                        <th>Model</th>
                        <th>Year</th>
                        <th>Daily Rate</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                    List<Vehicle> availableVehicles = (List<Vehicle>)request.getAttribute("availableVehicles");
                    if (availableVehicles != null) {
                        for (Vehicle vehicle : availableVehicles) {
                    %>
                        <tr>
                            <td><%= vehicle.getVehicleId() %></td>
                            <td><%= vehicle.getVehicleType() %></td>
                            <td><%= vehicle.getBrand() %></td>
                            <td><%= vehicle.getModel() %></td>
                            <td><%= vehicle.getYear() %></td>
                            <td>$<%= vehicle.getDailyRate() %></td>
                        </tr>
                    <% 
                        }
                    }
                    %>
                </tbody>
            </table>
        </section>
    </div>
</body>
</html> 