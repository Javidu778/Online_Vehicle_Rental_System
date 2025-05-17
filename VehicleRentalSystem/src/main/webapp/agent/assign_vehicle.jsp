<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.rental.model.Booking" %>
<%@ page import="com.rental.model.Vehicle" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Assign Vehicle - Vehicle Rental System</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h2>Assign Vehicle</h2>
            <a href="dashboard.jsp" class="back-btn">Back to Dashboard</a>
        </header>

        <% 
        Booking booking = (Booking)request.getAttribute("booking");
        List<Vehicle> vehicles = (List<Vehicle>)request.getAttribute("vehicles");
        
        if (booking != null) {
        %>
            <section class="booking-details">
                <h3>Booking Details</h3>
                <div class="details-grid">
                    <div class="detail-item">
                        <label>Booking ID:</label>
                        <span><%= booking.getBookingId() %></span>
                    </div>
                    <div class="detail-item">
                        <label>Customer:</label>
                        <span><%= booking.getCustomer().getFullName() %></span>
                    </div>
                    <div class="detail-item">
                        <label>Pickup Date:</label>
                        <span><%= booking.getPickupDate() %></span>
                    </div>
                    <div class="detail-item">
                        <label>Return Date:</label>
                        <span><%= booking.getReturnDate() %></span>
                    </div>
                    <div class="detail-item">
                        <label>Total Amount:</label>
                        <span>$<%= booking.getTotalAmount() %></span>
                    </div>
                </div>
            </section>

            <section class="assign-vehicle">
                <h3>Select Vehicle</h3>
                <% if (vehicles != null && !vehicles.isEmpty()) { %>
                    <form action="assign-vehicle" method="post">
                        <input type="hidden" name="bookingId" value="<%= booking.getBookingId() %>">
                        <input type="hidden" name="action" value="assign">
                        
                        <div class="form-group">
                            <label for="vehicleId">Available Vehicles:</label>
                            <select name="vehicleId" id="vehicleId" required>
                                <option value="">Select a vehicle</option>
                                <% for (Vehicle vehicle : vehicles) { %>
                                    <option value="<%= vehicle.getVehicleId() %>">
                                        <%= vehicle.getBrand() %> <%= vehicle.getModel() %> 
                                        (<%= vehicle.getVehicleType() %>) - 
                                        $<%= vehicle.getDailyRate() %>/day
                                    </option>
                                <% } %>
                            </select>
                        </div>
                        
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">Assign Vehicle</button>
                            <button type="submit" name="action" value="mark-pending" class="btn btn-secondary">
                                Mark as Pending
                            </button>
                        </div>
                    </form>
                <% } else { %>
                    <div class="no-vehicles">
                        <p>No vehicles available for the selected dates.</p>
                        <form action="assign-vehicle" method="post">
                            <input type="hidden" name="bookingId" value="<%= booking.getBookingId() %>">
                            <input type="hidden" name="action" value="mark-pending">
                            <button type="submit" class="btn btn-secondary">Mark as Pending</button>
                        </form>
                    </div>
                <% } %>
            </section>
        <% } else { %>
            <div class="error-message">
                Booking not found or no longer available.
            </div>
        <% } %>
    </div>
</body>
</html> 