<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.rental.model.Vehicle" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Book Vehicle - Vehicle Rental System</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h2>Book a Vehicle</h2>
            <div class="user-info">
                Welcome, <%= session.getAttribute("user") != null ? ((com.rental.model.User)session.getAttribute("user")).getFullName() : "Customer" %>
                <a href="../logout" class="logout-btn">Logout</a>
            </div>
        </header>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error-message">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <section class="booking-form">
            <h3>Select Vehicle and Dates</h3>
            <form action="book-vehicle" method="post">
                <div class="form-group">
                    <label for="vehicleId">Select Vehicle:</label>
                    <select name="vehicleId" id="vehicleId" required>
                        <option value="">Choose a vehicle</option>
                        <% 
                        List<Vehicle> vehicles = (List<Vehicle>)request.getAttribute("vehicles");
                        if (vehicles != null) {
                            for (Vehicle vehicle : vehicles) {
                        %>
                            <option value="<%= vehicle.getVehicleId() %>" 
                                    data-rate="<%= vehicle.getDailyRate() %>">
                                <%= vehicle.getBrand() %> <%= vehicle.getModel() %> 
                                (<%= vehicle.getVehicleType() %>) - 
                                $<%= vehicle.getDailyRate() %>/day
                            </option>
                        <% 
                            }
                        }
                        %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="pickupDate">Pickup Date:</label>
                    <input type="date" id="pickupDate" name="pickupDate" required 
                           min="<%= java.time.LocalDate.now() %>">
                </div>

                <div class="form-group">
                    <label for="returnDate">Return Date:</label>
                    <input type="date" id="returnDate" name="returnDate" required>
                </div>

                <div class="form-group">
                    <label>Estimated Total:</label>
                    <div id="totalAmount">$0.00</div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Submit Booking</button>
                    <a href="my-bookings.jsp" class="btn btn-secondary">View My Bookings</a>
                </div>
            </form>
        </section>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const vehicleSelect = document.getElementById('vehicleId');
            const pickupDate = document.getElementById('pickupDate');
            const returnDate = document.getElementById('returnDate');
            const totalAmount = document.getElementById('totalAmount');

            function calculateTotal() {
                const selectedOption = vehicleSelect.options[vehicleSelect.selectedIndex];
                if (selectedOption.value && pickupDate.value && returnDate.value) {
                    const dailyRate = parseFloat(selectedOption.dataset.rate);
                    const start = new Date(pickupDate.value);
                    const end = new Date(returnDate.value);
                    const days = Math.ceil((end - start) / (1000 * 60 * 60 * 24));
                    const total = dailyRate * days;
                    totalAmount.textContent = '$' + total.toFixed(2);
                } else {
                    totalAmount.textContent = '$0.00';
                }
            }

            vehicleSelect.addEventListener('change', calculateTotal);
            pickupDate.addEventListener('change', calculateTotal);
            returnDate.addEventListener('change', calculateTotal);

            // Set minimum return date based on pickup date
            pickupDate.addEventListener('change', function() {
                returnDate.min = this.value;
                if (returnDate.value && returnDate.value < this.value) {
                    returnDate.value = this.value;
                }
                calculateTotal();
            });
        });
    </script>
</body>
</html> 