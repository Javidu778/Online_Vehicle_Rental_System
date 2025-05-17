package com.rental.servlet;

import com.rental.dao.BookingDAO;
import com.rental.dao.VehicleDAO;
import com.rental.model.Booking;
import com.rental.model.User;
import com.rental.model.Vehicle;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/customer/book-vehicle")
public class CustomerBookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookingDAO bookingDAO;
    private VehicleDAO vehicleDAO;

    public void init() {
        bookingDAO = new BookingDAO();
        vehicleDAO = new VehicleDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("../login.jsp");
            return;
        }

        List<Vehicle> availableVehicles = vehicleDAO.getAvailableVehicles();
        request.setAttribute("vehicles", availableVehicles);
        request.getRequestDispatcher("book_vehicle.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("../login.jsp");
            return;
        }

        try {
            int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
            Date pickupDate = Date.valueOf(request.getParameter("pickupDate"));
            Date returnDate = Date.valueOf(request.getParameter("returnDate"));

            Vehicle vehicle = vehicleDAO.getVehicleById(vehicleId);
            if (vehicle == null) {
                request.setAttribute("error", "Selected vehicle not found");
                doGet(request, response);
                return;
            }

            // Calculate total amount
            long days = (returnDate.getTime() - pickupDate.getTime()) / (1000 * 60 * 60 * 24);
            BigDecimal totalAmount = vehicle.getDailyRate().multiply(new BigDecimal(days));

            // Create booking
            Booking booking = new Booking();
            booking.setCustomerId(user.getUserId());
            booking.setVehicleId(vehicleId);
            booking.setPickupDate(pickupDate);
            booking.setReturnDate(returnDate);
            booking.setTotalAmount(totalAmount);
            booking.setStatus("PENDING");

            boolean success = bookingDAO.createBooking(booking);
            if (success) {
                request.setAttribute("message", "Booking request submitted successfully!");
                response.sendRedirect("my-bookings.jsp");
            } else {
                request.setAttribute("error", "Failed to create booking. Please try again.");
                doGet(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Invalid input. Please check your dates and try again.");
            doGet(request, response);
        }
    }
} 