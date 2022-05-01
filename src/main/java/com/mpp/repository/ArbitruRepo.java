package com.mpp.repository;
import com.mpp.domain.Arbitru;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mpp.utils.JdbcUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;


public class ArbitruRepo implements ArbitruRepoInterface{
    private final JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();
//    private static final Logger logger = LogManager.getLogger(ArbitruRepo.class);

    public ArbitruRepo(Properties props) {
        initialize();
        logger.info("Initializing ArbitruRepo with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Arbitru findOne(Long aLong) {
        logger.traceEntry("finding arbitru () ", aLong);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select* from Arbitru where id=?")) {
            preStmt.setLong(1, aLong);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    String nume = result.getString("nume");
                    String username = result.getString("username");
                    String parola = result.getString("parola");
                    Long proba = result.getLong("proba");
                    Arbitru ar=new Arbitru(nume,username,parola,proba);
                    ar.setId(id);
                    return ar;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        return null;
    }

    @Override
    public Iterable<Arbitru> findAll() {
        Set<Arbitru> users = new HashSet<>();
        logger.traceEntry("finding toti arbitrii () ");
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Arbitru")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    String nume = result.getString("nume");
                    String username = result.getString("username");
                    String parola = result.getString("parola");
                    Long proba = result.getLong("proba");
                    Arbitru ar=new Arbitru(nume,username,parola,proba);
                    ar.setId(id);
                    users.add(ar);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        return users;
    }

    @Override
    public Arbitru save(Arbitru elem) {
        logger.traceEntry("saving arbitru () ", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Arbitru (nume,username,parola,proba) values (?,?,?,?)")) {
            preStmt.setString(1, elem.getNume());
            preStmt.setString(2, elem.getUsername());
            preStmt.setString(3, elem.getParola());
            preStmt.setLong(4, elem.getProba());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
            return null;
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
        return elem;
    }

    static SessionFactory sessionFactory;
    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exception "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }

    }

    @Override
    public Arbitru getArbitruUsername(String st) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Query<Arbitru> query = session.createQuery("select m from Arbitru as m where m.username=?1", Arbitru.class);
                query.setParameter(1, st);
                Arbitru messages = query.uniqueResult();
//                Arbitru messages = session.createQuery("from Arbitru as m  order by m.text asc", Arbitru.class).uniqueResult();
                                //        session.createQuery("select m from Message as m join fetch m.nextMessage order by m.text asc", Message.class). //initializarea obiectelor asociate

                System.out.println(messages.getUsername());
                tx.commit();
                return messages;
            } catch (RuntimeException ex) {
                System.err.println("Eroare la select "+ex);
                if (tx != null)
                    tx.rollback();
            }
            return null;

        }
//        logger.traceEntry("finding arbitru by username () ", st);
//        Connection con = dbUtils.getConnection();
//
//        try (PreparedStatement preStmt = con.prepareStatement("select* from Arbitru where username=?")) {
//            preStmt.setString(1,st);
//            try (ResultSet result = preStmt.executeQuery()) {
//                while (result.next()) {
//                    Long id = result.getLong("id");
//                    String nume = result.getString("nume");
//                    String username = result.getString("username");
//                    String parola = result.getString("parola");
//                    Long proba = result.getLong("proba");
//                    Arbitru ar=new Arbitru(nume,username,parola,proba);
//                    ar.setId(id);
//                    return ar;
//                }
//            }
//        } catch (SQLException e) {
//            logger.error(e);
//            System.err.println("Error DB " + e);
//        }
//        return null;
    }

    @Override
    public Arbitru delete(Long aLong) {
        return null;
    }

    @Override
    public Arbitru update(Arbitru entity) {
        return null;
    }


}
