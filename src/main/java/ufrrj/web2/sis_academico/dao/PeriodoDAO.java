package ufrrj.web2.sis_academico.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ufrrj.web2.sis_academico.model.Periodo;
import ufrrj.web2.sis_academico.model.DisciplinaOfertada;
import ufrrj.web2.sis_academico.util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;

public class PeriodoDAO {

    public void save(Session session, Periodo periodo) {
        session.save(periodo);
    }

    public Periodo getPeriodoAtual() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Periodo p WHERE :currentDate BETWEEN p.dataInicio AND p.dataFim";
            Query<Periodo> query = session.createQuery(hql, Periodo.class);

            // Passando a data atual para a consulta
            query.setParameter("currentDate", LocalDate.now());

            return query.uniqueResult(); // Retorna o período atual ou null se não encontrar
        }
    }

    public List<Periodo> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Periodo", Periodo.class).list();
        }
    }

    public Periodo getById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Periodo.class, id);
        }
    }

    public List<Periodo> listarTodosComDisciplinas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Usando join fetch para evitar o problema N+1 de consultas
            String hql = "SELECT DISTINCT p FROM Periodo p " +
                    "LEFT JOIN FETCH p.disciplinas d " +
                    "LEFT JOIN FETCH d.disciplina";

            return session.createQuery(hql, Periodo.class).list();
        }
    }

    public Periodo getByIdComDisciplinas(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT DISTINCT p FROM Periodo p " +
                    "LEFT JOIN FETCH p.disciplinas d " +
                    "LEFT JOIN FETCH d.disciplina " +
                    "WHERE p.id = :id";

            Query<Periodo> query = session.createQuery(hql, Periodo.class);
            query.setParameter("id", id);
            return query.uniqueResult();
        }
    }

    public void update(Periodo periodo) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(periodo);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Periodo periodo = session.get(Periodo.class, id);
            if (periodo != null) {
                session.delete(periodo);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
}