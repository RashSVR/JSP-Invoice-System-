package com.is.invoicingsystem.dao;

import com.is.invoicingsystem.model.InvoiceItem;
import com.is.invoicingsystem.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class InvoiceItemDao {

    public void saveInvoiceItame(InvoiceItem invoiceItem) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(invoiceItem);
            transaction.commit();
        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
            e.printStackTrace();
        }
    }
}
