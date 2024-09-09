package com.is.invoicingsystem.dao;

import com.is.invoicingsystem.model.Invoice;
import com.is.invoicingsystem.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class InvoiceDao {
    public List<Invoice> getAllInvoices() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Invoice", Invoice.class).list();
        }
    }

    public void saveInvoice(Invoice invoice) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(invoice);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteInvoice(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Invoice invoice = session.get(Invoice.class, id);
            if (invoice != null) {
                session.delete(invoice);
                transaction.commit();
            }
        }
    }
}