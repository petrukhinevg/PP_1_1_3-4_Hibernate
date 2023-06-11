package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;


public class UserDaoHibernateImpl implements UserDao {
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS user (id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(45), lastName VARCHAR(45), age TINYINT)";
    private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS user";
    private static final String CLEAR_TABLE_SQL = "DELETE from user";
    private static final String INSERT_SQL = "INSERT INTO users(name, lastName, age) VALUES (?, ?, ?)";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM user WHERE id=?";
    private static final String GET_ALL_SQL = "SELECT * FROM user";

    private final static SessionFactory sessionFactory = Util.getSessionFactory();

    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(CREATE_TABLE_SQL).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(DROP_TABLE_SQL).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery(CLEAR_TABLE_SQL).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(DELETE_BY_ID_SQL);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = null;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("FROM User");
            users = (List<User>) query.list();
        }
        return users;
    }



    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
