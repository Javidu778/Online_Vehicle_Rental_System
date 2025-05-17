package com.rental.servlet;

import com.rental.dao.BookingDAO;
import com.rental.model.Booking;
import com.rental.model.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/customer/my-bookings")
public class CustomerBookingsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookingDAO bookingDAO;

    public void init() {
        bookingDAO = new BookingDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("../login.jsp");
            return;
        }

        List<Booking> bookings = bookingDAO.getCustomerBookings(user.getUserId());
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("my-bookings.jsp").forward(request, response);
    }
} 