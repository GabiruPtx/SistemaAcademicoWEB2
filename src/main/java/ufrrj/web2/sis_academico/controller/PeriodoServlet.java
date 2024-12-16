package ufrrj.web2.sis_academico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ufrrj.web2.sis_academico.dao.PeriodoDAO;
import ufrrj.web2.sis_academico.dao.MatriculaDisciplinaDAO;
import ufrrj.web2.sis_academico.model.*;
import ufrrj.web2.sis_academico.util.HibernateUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/Periodos.do", "/painelPeriodos.do"})
public class PeriodoServlet extends HttpServlet {

    private final PeriodoDAO periodoDAO = new PeriodoDAO();
    private final MatriculaDisciplinaDAO matriculaDAO = new MatriculaDisciplinaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/painelPeriodos.do".equals(path)) {
            mostrarPainelPeriodos(request, response);
        } else {
            mostrarFormularioPeriodo(request, response);
        }
    }

    private void mostrarPainelPeriodos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Carregar períodos com disciplinas usando o novo método
            List<Periodo> periodos = periodoDAO.listarTodosComDisciplinas();

            // Para cada período, carregar as matrículas das disciplinas
            Map<Long, List<MatriculaDisciplina>> matriculasPorDisciplina = null;
            if (!periodos.isEmpty()) {
                matriculasPorDisciplina = matriculaDAO.buscarMatriculasPorPeriodo(periodos.get(0));
            }

            // Configurar atributos para o JSP
            request.setAttribute("periodos", periodos);
            request.setAttribute("matriculasPorDisciplina", matriculasPorDisciplina);
            request.setAttribute("matriculaDAO", matriculaDAO); // Para usar os métodos de cálculo

            // Encaminhar para o JSP do painel
            request.getRequestDispatcher("painelPeriodo.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Erro ao carregar painel de períodos: " + e.getMessage());
            request.getRequestDispatcher("painelPeriodo.jsp").forward(request, response);
        }
    }

    private void mostrarFormularioPeriodo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Carregar todas as disciplinas disponíveis
            List<Disciplina> disciplinas = session.createQuery("from Disciplina", Disciplina.class).list();
            request.setAttribute("disciplinas", disciplinas);

            // Carregar períodos existentes
            List<Periodo> periodos = periodoDAO.getAll();
            request.setAttribute("periodos", periodos);

            request.getRequestDispatcher("adicionarPeriodo.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Erro ao carregar dados: " + e.getMessage());
            request.getRequestDispatcher("adicionarPeriodo.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("updateNotas".equals(action)) {
            atualizarNotas(request, response);
        } else {
            criarNovoPeriodo(request, response);
        }
    }

    private void atualizarNotas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long alunoId = Long.parseLong(request.getParameter("alunoId"));
            Long disciplinaOfertadaId = Long.parseLong(request.getParameter("disciplinaOfertadaId"));
            Double nota1 = Double.parseDouble(request.getParameter("nota1"));
            Double nota2 = Double.parseDouble(request.getParameter("nota2"));

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                DisciplinaOfertada disciplinaOfertada = session.get(DisciplinaOfertada.class, disciplinaOfertadaId);
                Aluno aluno = session.get(Aluno.class, alunoId);

                if (disciplinaOfertada != null && aluno != null) {
                    matriculaDAO.updateNotas(aluno, disciplinaOfertada, nota1, nota2);
                    response.sendRedirect("painelPeriodos");
                } else {
                    throw new ServletException("Aluno ou disciplina não encontrados");
                }
            }
        } catch (Exception e) {
            request.setAttribute("error", "Erro ao atualizar notas: " + e.getMessage());
            mostrarPainelPeriodos(request, response);
        }
    }

    private void criarNovoPeriodo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("### Parâmetros recebidos ###");
        Map<String, String[]> parametros = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parametros.entrySet()) {
            System.out.println("Chave: " + entry.getKey() + ", Valores: " + Arrays.toString(entry.getValue()));
        }

        String nome = request.getParameter("nome");
        String dataInicioStr = request.getParameter("dataInicio");
        String dataFimStr = request.getParameter("dataFim");
        String[] disciplinasIds = request.getParameterValues("disciplinasOfertadas");

        // Validação dos campos obrigatórios
        if (nome == null || dataInicioStr == null || dataFimStr == null ||
                nome.isBlank() || dataInicioStr.isBlank() || dataFimStr.isBlank()) {
            request.setAttribute("error", "Todos os campos são obrigatórios.");
            mostrarFormularioPeriodo(request, response);
            return;
        }

        try {
            LocalDate dataInicio = LocalDate.parse(dataInicioStr);
            LocalDate dataFim = LocalDate.parse(dataFimStr);

            if (dataFim.isBefore(dataInicio)) {
                request.setAttribute("error", "A data de fim não pode ser anterior à data de início.");
                mostrarFormularioPeriodo(request, response);
                return;
            }

            Periodo periodo = new Periodo(nome, dataInicio, dataFim);

            // Abrir sessão e transação para todo o processo
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction tx = session.beginTransaction();

                if (disciplinasIds != null && disciplinasIds.length > 0) {
                    for (String disciplinaId : disciplinasIds) {
                        Disciplina disciplina = session.get(Disciplina.class, Long.parseLong(disciplinaId));
                        if (disciplina != null) {
                            DisciplinaOfertada ofertada = new DisciplinaOfertada();
                            ofertada.setDisciplina(disciplina);
                            ofertada.setPeriodo(periodo);
                            periodo.getDisciplinas().add(ofertada);
                        }
                    }
                }

                // Salvar o período usando o DAO com a sessão ativa
                periodoDAO.save(session, periodo);

                tx.commit();
            }

            // Redirecionar após sucesso
            response.sendRedirect("paginaPrincipal.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Erro ao salvar período: " + e.getMessage());
            mostrarFormularioPeriodo(request, response);
        }
    }
}