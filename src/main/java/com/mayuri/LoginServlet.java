package com.mayuri;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        
        System.out.println("Username: " + user + ", Password: " + pass);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/studentdbb", "root", "root");

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM users WHERE username=? AND password=?");
            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                response.sendRedirect("success.jsp");
            } else {
                response.getWriter().println("Login Failed. Try Again.");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();  // console मध्ये full error दिसेल
            response.getWriter().println("Error: " + e.getMessage());  // browser वर message दिसेल
        }
    }
}