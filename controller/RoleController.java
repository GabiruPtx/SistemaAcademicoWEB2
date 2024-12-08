package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/RoleController")
public class RoleController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String role = request.getParameter("role");

        if (role == null) {
            response.sendRedirect("error.jsp");
            return;
        }

        switch (role) {
            case "professor":
                response.sendRedirect("loginProfessor.jsp");
                break;
            case "administrador":
                response.sendRedirect("loginAdministrador.jsp");
                break;
            default:
                response.sendRedirect("error.jsp");
        }
    }
}
