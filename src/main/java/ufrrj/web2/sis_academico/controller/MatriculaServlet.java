package ufrrj.web2.sis_academico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import ufrrj.web2.sis_academico.dao.MatriculaDisciplinaDAO;
import ufrrj.web2.sis_academico.model.*;
import ufrrj.web2.sis_academico.util.HibernateUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/MatricularAluno.do")
public class MatriculaServlet extends HttpServlet {

    private final MatriculaDisciplinaDAO matriculaDAO = new MatriculaDisciplinaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Carregar período atual
            String hql = "FROM Periodo p WHERE :dataAtual BETWEEN p.dataInicio AND p.dataFim";
            List<Periodo> periodos = session.createQuery(hql, Periodo.class)
                    .setParameter("dataAtual", LocalDate.now()) // Alterado para LocalDate.now()
                    .list();

            if (!periodos.isEmpty()) {
                Periodo periodoAtual = periodos.get(0);
                request.setAttribute("periodoAtual", periodoAtual);

                // Carregar disciplinas ofertadas no período atual
                List<DisciplinaOfertada> disciplinasOfertadas = periodoAtual.getDisciplinas();
                request.setAttribute("disciplinasOfertadas", disciplinasOfertadas);
            }

            request.getRequestDispatcher("matricularAluno.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Erro ao carregar dados: " + e.getMessage());
            request.getRequestDispatcher("matricularAluno.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String matriculaAluno = request.getParameter("matriculaAluno");
        Long disciplinaOfertadaId = Long.parseLong(request.getParameter("disciplinaOfertadaId"));

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Buscar aluno e disciplina
            Aluno aluno = session.createQuery("FROM Aluno WHERE matricula = :matricula", Aluno.class)
                    .setParameter("matricula", matriculaAluno)
                    .uniqueResult();

            DisciplinaOfertada disciplinaOfertada = session.get(DisciplinaOfertada.class, disciplinaOfertadaId);

            if (aluno == null || disciplinaOfertada == null) {
                request.setAttribute("error", "Aluno ou disciplina não encontrados.");
                doGet(request, response);
                return;
            }

            // Verificar se já está matriculado
            if (matriculaDAO.jaMatriculado(aluno, disciplinaOfertada)) {
                request.setAttribute("error", "Aluno já matriculado nesta disciplina neste período.");
                doGet(request, response);
                return;
            }

            // Verificar média anterior
            Double mediaAnterior = matriculaDAO.getMediaAnterior(aluno, disciplinaOfertada.getDisciplina().getId());
            if (mediaAnterior != null && mediaAnterior >= 5.0) {
                request.setAttribute("error", "Aluno já foi aprovado nesta disciplina anteriormente.");
                doGet(request, response);
                return;
            }

            // Criar nova matrícula
            MatriculaDisciplina novaMatricula = new MatriculaDisciplina();
            novaMatricula.setAluno(aluno);
            novaMatricula.setDisciplinaOfertada(disciplinaOfertada);
            novaMatricula.setDataMatricula(LocalDateTime.now());

            matriculaDAO.save(novaMatricula);

            request.setAttribute("success", "Matrícula realizada com sucesso!");
            doGet(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Erro ao realizar matrícula: " + e.getMessage());
            doGet(request, response);
        }
    }
}