package ufrrj.web2.sis_academico.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ufrrj.web2.sis_academico.model.Aluno;
import ufrrj.web2.sis_academico.util.HibernateUtil;

import java.util.List;

public class AlunoDAO {

    public void save(Aluno aluno) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(aluno);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e; // Rethrow exception for higher-level handling
        }
    }

    public List<Aluno> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Aluno", Aluno.class).list();
        }
    }
}