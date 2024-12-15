package ufrrj.web2.sis_academico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Login.do")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Pega o email e a senha do formulário e remove espaços em branco
        String email = request.getParameter("email").trim();
        String password = request.getParameter("password").trim();

        // Validação fictícia de e-mail e senha (substituir por lógica real)
        if ("admin@email.com".equals(email) && "1234".equals(password)) {
            // Configura uma mensagem de sucesso na sessão
            request.getSession().setAttribute("mensagemSucesso", "Login realizado com sucesso! Bem-vindo, " + email + "!");

            // Redireciona para a página principal
            response.sendRedirect("paginaPrincipal.jsp");
        } else {
            // Configura uma mensagem de erro na sessão
            request.getSession().setAttribute("mensagemErro", "Credenciais inválidas. Tente novamente.");

            // Redireciona de volta para a página de login
            response.sendRedirect("login.jsp");
        }
    }
}
