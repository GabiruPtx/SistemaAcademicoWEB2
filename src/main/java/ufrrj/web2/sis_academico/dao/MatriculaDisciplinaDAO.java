package ufrrj.web2.sis_academico.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ufrrj.web2.sis_academico.model.MatriculaDisciplina;
import ufrrj.web2.sis_academico.model.Aluno;
import ufrrj.web2.sis_academico.model.DisciplinaOfertada;
import ufrrj.web2.sis_academico.util.HibernateUtil;

import java.time.LocalDateTime;
import java.util.List;

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
}