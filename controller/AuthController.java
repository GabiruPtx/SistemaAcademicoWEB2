package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.example.service.UserService;
import com.example.model.User;

@WebServlet("/AuthController")
public class AuthController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService(); // Instancia o serviço de usuário
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if (email == null || password == null || role == null) {
            response.sendRedirect("error.jsp");
            return;
        }

        boolean authenticated = userService.authenticate(email, password, role);

        if (authenticated) {
            if ("professor".equals(role)) {
                response.sendRedirect("homeProfessor.jsp");
            } else if ("administrador".equals(role)) {
                response.sendRedirect("homeAdmin.jsp");
            }
        } else {
            response.sendRedirect("error.jsp");
        }
    }
}
