package ufrrj.web2.sis_academico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import ufrrj.web2.sis_academico.dao.AlunoDAO;
import ufrrj.web2.sis_academico.model.Aluno;
import ufrrj.web2.sis_academico.model.DisciplinaOfertada;
import ufrrj.web2.sis_academico.util.HibernateUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/Alunos.do")
public class AlunoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            if ("disciplinas".equals(action)) {
                // Listar alunos e disciplinas
                List<Aluno> alunos = session.createQuery("from Aluno", Aluno.class).list();
                List<DisciplinaOfertada> disciplinas = session.createQuery("from DisciplinaOfertada", DisciplinaOfertada.class).list();

                request.setAttribute("alunos", alunos);
                request.setAttribute("disciplinas", disciplinas);

                request.getRequestDispatcher("matricularAluno.jsp").forward(request, response);
            } else {
                // Listar alunos para cadastro simples
                List<Aluno> alunos = session.createQuery("from Aluno", Aluno.class).list();
                request.setAttribute("alunos.do", alunos);

                request.getRequestDispatcher("cadastrarAluno.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String matricula = request.getParameter("matricula");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (nome == null || matricula == null || nome.isBlank() || matricula.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\": \"Nome e matrícula são obrigatórios.\"}");
            return;
        }

        Aluno aluno = new Aluno();
        aluno.setNome(nome);
        aluno.setMatricula(matricula);

        try {
            AlunoDAO alunoDAO = new AlunoDAO();
            alunoDAO.save(aluno);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Aluno cadastrado com sucesso!\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"Erro ao cadastrar aluno: " + e.getMessage() + "\"}");
        }
    }
}
