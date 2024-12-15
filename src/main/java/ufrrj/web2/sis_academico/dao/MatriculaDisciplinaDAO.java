package ufrrj.web2.sis_academico.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ufrrj.web2.sis_academico.model.MatriculaDisciplina;
import ufrrj.web2.sis_academico.model.Aluno;
import ufrrj.web2.sis_academico.model.DisciplinaOfertada;
import ufrrj.web2.sis_academico.model.Periodo;
import ufrrj.web2.sis_academico.util.HibernateUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatriculaDisciplinaDAO {

    public void save(MatriculaDisciplina matricula) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(matricula);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public Double getMediaAnterior(Aluno aluno, Long disciplinaId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT m FROM MatriculaDisciplina m " +
                    "WHERE m.aluno = :aluno " +
                    "AND m.disciplinaOfertada.disciplina.id = :disciplinaId " +
                    "AND m.nota1 IS NOT NULL " +
                    "AND m.nota2 IS NOT NULL";

            Query<MatriculaDisciplina> query = session.createQuery(hql, MatriculaDisciplina.class);
            query.setParameter("aluno", aluno);
            query.setParameter("disciplinaId", disciplinaId);

            List<MatriculaDisciplina> matriculas = query.list();

            if (matriculas.isEmpty()) {
                return null;
            }

            // Retorna a média da última matrícula
            MatriculaDisciplina ultimaMatricula = matriculas.get(matriculas.size() - 1);
            return (ultimaMatricula.getNota1() + ultimaMatricula.getNota2()) / 2;
        }
    }

    public boolean jaMatriculado(Aluno aluno, DisciplinaOfertada disciplinaOfertada) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(m) FROM MatriculaDisciplina m " +
                    "WHERE m.aluno = :aluno " +
                    "AND m.disciplinaOfertada = :disciplinaOfertada";

            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("aluno", aluno);
            query.setParameter("disciplinaOfertada", disciplinaOfertada);

            return query.uniqueResult() > 0;
        }
    }

        public void updateNotas(Aluno aluno, DisciplinaOfertada disciplinaOfertada, Double nota1, Double nota2) {
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();

                // Find the specific enrollment
                String hql = "FROM MatriculaDisciplina WHERE aluno = :aluno AND disciplinaOfertada = :disciplina";
                MatriculaDisciplina matricula = session.createQuery(hql, MatriculaDisciplina.class)
                        .setParameter("aluno", aluno)
                        .setParameter("disciplina", disciplinaOfertada)
                        .uniqueResult();

                if (matricula == null) {
                    throw new IllegalArgumentException("Matrícula não encontrada");
                }

                // Validate notes
                if (nota1 < 0 || nota1 > 10 || nota2 < 0 || nota2 > 10) {
                    throw new IllegalArgumentException("Notas devem estar entre 0 e 10");
                }

                // Update notes
                matricula.setNota1(nota1);
                matricula.setNota2(nota2);

                session.update(matricula);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                throw e;
            }
        }

    public List<MatriculaDisciplina> buscarPorDisciplinaOfertada(DisciplinaOfertada disciplinaOfertada) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT m FROM MatriculaDisciplina m " +
                    "LEFT JOIN FETCH m.aluno " +
                    "WHERE m.disciplinaOfertada = :disciplinaOfertada " +
                    "ORDER BY m.aluno.nome";

            Query<MatriculaDisciplina> query = session.createQuery(hql, MatriculaDisciplina.class);
            query.setParameter("disciplinaOfertada", disciplinaOfertada);

            return query.list();
        }
    }

    public Map<Long, List<MatriculaDisciplina>> buscarMatriculasPorPeriodo(Periodo periodo) {
        Map<Long, List<MatriculaDisciplina>> matriculasPorDisciplina = new HashMap<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT m FROM MatriculaDisciplina m " +
                    "LEFT JOIN FETCH m.aluno " +
                    "LEFT JOIN FETCH m.disciplinaOfertada d " +
                    "WHERE d.periodo = :periodo " +
                    "ORDER BY d.disciplina.nome, m.aluno.nome";

            Query<MatriculaDisciplina> query = session.createQuery(hql, MatriculaDisciplina.class);
            query.setParameter("periodo", periodo);

            List<MatriculaDisciplina> matriculas = query.list();

            // Agrupa as matrículas por disciplina ofertada
            for (MatriculaDisciplina matricula : matriculas) {
                Long disciplinaOfertadaId = matricula.getDisciplinaOfertada().getId();
                matriculasPorDisciplina.computeIfAbsent(disciplinaOfertadaId, k -> new ArrayList<>())
                        .add(matricula);
            }
        }

        return matriculasPorDisciplina;
    }

    public double calcularMedia(MatriculaDisciplina matricula) {
        if (matricula.getNota1() == null || matricula.getNota2() == null) {
            return 0.0;
        }
        return (matricula.getNota1() + matricula.getNota2()) / 2.0;
    }

    public boolean isAprovado(MatriculaDisciplina matricula) {
        return calcularMedia(matricula) >= 6.0;
    }

}
