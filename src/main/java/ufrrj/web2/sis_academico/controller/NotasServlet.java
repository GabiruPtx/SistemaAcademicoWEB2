package ufrrj.web2.sis_academico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import ufrrj.web2.sis_academico.dao.MatriculaDisciplinaDAO;
import ufrrj.web2.sis_academico.dao.PeriodoDAO;
import ufrrj.web2.sis_academico.model.Aluno;
import ufrrj.web2.sis_academico.model.DisciplinaOfertada;
import ufrrj.web2.sis_academico.model.MatriculaDisciplina;
import ufrrj.web2.sis_academico.model.Periodo;
import ufrrj.web2.sis_academico.util.HibernateUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Notas.do")
public class NotasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Fetch all students
            List<Aluno> alunos = session.createQuery("from Aluno", Aluno.class).list();

            // Buscar o período atual usando o PeriodoDAO
            PeriodoDAO periodoDAO = new PeriodoDAO();
            Periodo periodoAtual = periodoDAO.getPeriodoAtual(); // Aqui você chama o método que sugeri

            if (periodoAtual != null) {
                // Fetch disciplinas ofertadas com o período atual
                List<DisciplinaOfertada> disciplinasOfertadas = session.createQuery(
                                "from DisciplinaOfertada where periodo = :periodo", DisciplinaOfertada.class
                        )
                        .setParameter("periodo", periodoAtual)
                        .list();

                request.setAttribute("disciplinasOfertadas", disciplinasOfertadas);
            } else {
                request.setAttribute("disciplinasOfertadas", new ArrayList<>()); // Caso não encontre o período
            }

            request.setAttribute("alunos", alunos);
            request.getRequestDispatcher("adicionarNota.jsp").forward(request, response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long alunoId = Long.parseLong(request.getParameter("aluno_id"));
            Long disciplinaId = Long.parseLong(request.getParameter("disciplina_id"));
            Double nota1 = Double.parseDouble(request.getParameter("nota1"));
            Double nota2 = Double.parseDouble(request.getParameter("nota2"));

            // Validate inputs
            if (nota1 < 0 || nota1 > 10 || nota2 < 0 || nota2 > 10) {
                throw new IllegalArgumentException("Notas devem estar entre 0 e 10");
            }

            // Fetch aluno and disciplina
            Aluno aluno = session.get(Aluno.class, alunoId);
            DisciplinaOfertada disciplinaOfertada = session.get(DisciplinaOfertada.class, disciplinaId);

            // Check if student is enrolled in the discipline
            MatriculaDisciplinaDAO matriculaDAO = new MatriculaDisciplinaDAO();
            if (!matriculaDAO.jaMatriculado(aluno, disciplinaOfertada)) {
                request.setAttribute("error", "Aluno não está matriculado nesta disciplina.");
                doGet(request, response);
                return;
            }

            // Find the specific enrollment to update
            String hql = "FROM MatriculaDisciplina WHERE aluno = :aluno AND disciplinaOfertada = :disciplina";
            MatriculaDisciplina matricula = session.createQuery(hql, MatriculaDisciplina.class)
                    .setParameter("aluno", aluno)
                    .setParameter("disciplina", disciplinaOfertada)
                    .uniqueResult();

            // Update grades
            session.beginTransaction();
            matricula.setNota1(nota1);
            matricula.setNota2(nota2);
            session.update(matricula);
            session.getTransaction().commit();

            response.sendRedirect("paginaPrincipal.jsp");
        } catch (Exception e) {
            request.setAttribute("error", "Erro ao adicionar notas: " + e.getMessage());
            doGet(request, response);
        }
    }
}