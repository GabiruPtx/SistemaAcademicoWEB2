package ufrrj.web2.sis_academico.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ufrrj.web2.sis_academico.model.Periodo;
import ufrrj.web2.sis_academico.model.DisciplinaOfertada;
import ufrrj.web2.sis_academico.util.HibernateUtil;

import java.util.List;

public class PeriodoDAO {

    public void save(Periodo periodo) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(periodo);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
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