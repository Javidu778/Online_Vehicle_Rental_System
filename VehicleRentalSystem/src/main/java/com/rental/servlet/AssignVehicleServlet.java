package com.rental.servlet;

import com.rental.dao.BookingDAO;
import com.rental.dao.VehicleDAO;
import com.rental.model.Booking;
import com.rental.model.Vehicle;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/agent/assign-vehicle")
public class AssignVehicleServlet extends HttpServlet {
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
        if (session.getAttribute("role") == null || !"AGENT".equals(session.getAttribute("role"))) {
            response.sendRedirect("../login.jsp");
            return;
        }

        String bookingId = request.getParameter("bookingId");
        if (bookingId != null) {
            Booking booking = bookingDAO.getBookingById(Integer.parseInt(bookingId));
            List<Vehicle> availableVehicles = vehicleDAO.getAvailableVehicles();
            
            request.setAttribute("booking", booking);
            request.setAttribute("vehicles", availableVehicles);
            request.getRequestDispatcher("assign_vehicle.jsp").forward(request, response);
        } else {
            response.sendRedirect("dashboard.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("role") == null || !"AGENT".equals(session.getAttribute("role"))) {
            response.sendRedirect("../login.jsp");
            return;
        }

        String bookingId = request.getParameter("bookingId");
        String vehicleId = request.getParameter("vehicleId");
        String action = request.getParameter("action");

        if ("assign".equals(action) && bookingId != null && vehicleId != null) {
            boolean success = bookingDAO.assignVehicleToBooking(
                Integer.parseInt(bookingId), 
                Integer.parseInt(vehicleId)
            );

            if (success) {
                vehicleDAO.updateVehicleStatus(Integer.parseInt(vehicleId), "RESERVED");
                request.setAttribute("message", "Vehicle assigned successfully!");
            } else {
                request.setAttribute("error", "Failed to assign vehicle. Please try again.");
            }
        } else if ("mark-pending".equals(action) && bookingId != null) {
            boolean success = bookingDAO.updateBookingStatus(
                Integer.parseInt(bookingId), 
                "PENDING"
            );

            if (success) {
                request.setAttribute("message", "Booking marked as pending!");
            } else {
                request.setAttribute("error", "Failed to update booking status. Please try again.");
            }
        }

        response.sendRedirect("dashboard.jsp");
    }
} 