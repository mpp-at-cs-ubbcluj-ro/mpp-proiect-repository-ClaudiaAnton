package com.mpp.repository;
import com.mpp.domain.Proba;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mpp.utils.JdbcUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class ProbaRepo implements ProbaRepoInterface{
    private final JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger(ArbitruRepo.class);

    public ProbaRepo(Properties props) {
        logger.info("Initializing ProbaRepo with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Proba findOne(Long aLong) {
        logger.traceEntry("finding Proba () ", aLong);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Proba where id=?")) {
            preStmt.setLong(1, aLong);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    String nume = result.getString("nume");
                    Proba pr=new Proba(nume);
                    pr.setId(id);
                    return pr;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        return null;
    }

    @Override
    public Iterable<Proba> findAll() {
        Set<Proba> users = new HashSet<>();
        logger.traceEntry("finding probe() ");
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * Proba")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    String nume = result.getString("nume");
                    Proba ar=new Proba(nume);
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
    public Proba save(Proba elem) {
        logger.traceEntry("saving Proba () ", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Proba (nume) values (?)")) {
            preStmt.setString(1, elem.getNume());
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

    @Override
    public Proba delete(Long aLong) {
        return null;
    }

    @Override
    public Proba update(Proba entity) {
        return null;
    }
}
