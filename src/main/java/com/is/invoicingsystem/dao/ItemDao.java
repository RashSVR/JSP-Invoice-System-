package com.is.invoicingsystem.dao;

import com.is.invoicingsystem.model.Item;
import com.is.invoicingsystem.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ItemDao {
    public List<Item> getAllItems() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Item", Item.class).list();
        }
    }

    public Item getItemById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Item.class, id);
        }
    }

    public void saveItem(Item item) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(item);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public void updateItem(Item item) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(item);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public void deleteItem(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Item item = session.get(Item.class, id);
            if (item != null) {
                session.delete(item);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
}
