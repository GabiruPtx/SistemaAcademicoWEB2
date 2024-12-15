package ufrrj.web2.sis_academico.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import ufrrj.web2.sis_academico.dao.PeriodoDAO;
import ufrrj.web2.sis_academico.model.Periodo;
import ufrrj.web2.sis_academico.model.DisciplinaOfertada;
import ufrrj.web2.sis_academico.model.Disciplina;
import ufrrj.web2.sis_academico.util.HibernateUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/Periodos.do")
public class PeriodoServlet extends HttpServlet {

    private final PeriodoDAO periodoDAO = new PeriodoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Carregar todas as disciplinas disponíveis
            List<Disciplina> disciplinas = session.createQuery("from Disciplina", Disciplina.class).list();
            request.setAttribute("disciplinas", disciplinas);

            // Carregar períodos existentes
            List<Periodo> periodos = periodoDAO.getAll();
            request.setAttribute("periodos", periodos);

            request.getRequestDispatcher("/webapp/adicionarPeriodo.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Erro ao carregar dados: " + e.getMessage());
            request.getRequestDispatcher("/webapp/adicionarPeriodo.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String dataInicioStr = request.getParameter("dataInicio");
        String dataFimStr = request.getParameter("dataFim");
        String[] disciplinasIds = request.getParameterValues("disciplinasOfertadas");

        // Validação dos campos obrigatórios
        if (nome == null || dataInicioStr == null || dataFimStr == null ||
                nome.isBlank() || dataInicioStr.isBlank() || dataFimStr.isBlank()) {
            request.setAttribute("error", "Todos os campos são obrigatórios.");
            doGet(request, response);
            return;
        }

        try {
            LocalDate dataInicio = LocalDate.parse(dataInicioStr);
            LocalDate dataFim = LocalDate.parse(dataFimStr);

            // Validação das datas
            if (dataFim.isBefore(dataInicio)) {
                request.setAttribute("error", "A data de fim não pode ser anterior à data de início.");
                doGet(request, response);
                return;
            }

            Periodo periodo = new Periodo(nome, dataInicio, dataFim);

            // Adicionar disciplinas ofertadas se houver seleção
            if (disciplinasIds != null && disciplinasIds.length > 0) {
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
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
            }

            periodoDAO.save(periodo);
            response.sendRedirect("Periodos.do");

        } catch (Exception e) {
            request.setAttribute("error", "Erro ao salvar período: " + e.getMessage());
            doGet(request, response);
        }
    }
}