package ufrrj.web2.sis_academico.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ufrrj.web2.sis_academico.model.Disciplina;
import ufrrj.web2.sis_academico.util.HibernateUtil;

import java.util.List;

public class DisciplinaDAO {

    public void save(Disciplina disciplina) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(disciplina);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public List<Disciplina> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Disciplina", Disciplina.class).list();
        }
    }

    public Disciplina findByCodigo(String codigo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Disciplina where codigo = :codigo", Disciplina.class)
                    .setParameter("codigo", codigo)
                    .uniqueResult();
        }
    }
}