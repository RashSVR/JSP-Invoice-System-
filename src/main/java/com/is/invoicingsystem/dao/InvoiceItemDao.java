package com.is.invoicingsystem.dao;

import com.is.invoicingsystem.model.Invoice;
import com.is.invoicingsystem.model.InvoiceItem;
import com.is.invoicingsystem.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class InvoiceItemDao {
    public List<InvoiceItem> getInvoiceById(Invoice invoice) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery
                    ("FROM InvoiceItem ii WHERE ii.invoice = :invoice", InvoiceItem.class)
                    .setParameter("invoice", invoice).list();
        }
    }

    public void saveInvoiceItame(InvoiceItem invoiceItem) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(invoiceItem);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}
