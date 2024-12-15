package ufrrj.web2.sis_academico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import ufrrj.web2.sis_academico.dao.DisciplinaDAO;
import ufrrj.web2.sis_academico.model.Disciplina;
import ufrrj.web2.sis_academico.util.HibernateUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/Disciplinas.do")
public class DisciplinaServlet extends HttpServlet {

    private DisciplinaDAO disciplinaDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        disciplinaDAO = new DisciplinaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Disciplina> disciplinas = disciplinaDAO.getAll();
            request.setAttribute("disciplinas", disciplinas);
            request.getRequestDispatcher("cadastrarDisciplina.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Erro ao listar disciplinas: " + e.getMessage());
            request.getRequestDispatcher("cadastrarDisciplina.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String codigo = request.getParameter("codigo");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (nome == null || codigo == null || nome.isBlank() || codigo.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\": \"Nome e código são obrigatórios.\"}");
            return;
        }

        try {
            // Verifica se já existe disciplina com o mesmo código
            Disciplina existente = disciplinaDAO.findByCodigo(codigo);
            if (existente != null) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write("{\"message\": \"Já existe uma disciplina com este código.\"}");
                return;
            }

            Disciplina disciplina = new Disciplina(nome, codigo);
            disciplinaDAO.save(disciplina);

            // Retorna sucesso e a lista atualizada de disciplinas
            List<Disciplina> disciplinas = disciplinaDAO.getAll();
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Disciplina cadastrada com sucesso!\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"Erro ao cadastrar disciplina: " + e.getMessage() + "\"}");
        }
    }
}