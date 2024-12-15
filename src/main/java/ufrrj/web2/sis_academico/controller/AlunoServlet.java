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

                request.setAttribute("alunos.do", alunos);
                request.setAttribute("disciplinas", disciplinas);

                request.getRequestDispatcher("/webapp/matricularAluno.jsp").forward(request, response);
            } else {
                // Listar alunos para cadastro simples
                List<Aluno> alunos = session.createQuery("from Aluno", Aluno.class).list();
                request.setAttribute("alunos.do", alunos);

                request.getRequestDispatcher("/webapp/cadastrarAluno.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String matricula = request.getParameter("matricula");

        if (nome == null || matricula == null || nome.isBlank() || matricula.isBlank()) {
            request.setAttribute("error", "Nome e matrícula são obrigatórios.");
            request.getRequestDispatcher("/WEB-INF/cadastrarAluno.jsp").forward(request, response);
            return;
        }

        Aluno aluno = new Aluno();
        aluno.setNome(nome);
        aluno.setMatricula(matricula);

        try {
            AlunoDAO alunoDAO = new AlunoDAO();
            alunoDAO.save(aluno);
            response.sendRedirect("Alunos.do");
        } catch (Exception e) {
            request.setAttribute("error", "Erro ao cadastrar aluno: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/cadastrarAluno.jsp").forward(request, response);
        }
    }
}
