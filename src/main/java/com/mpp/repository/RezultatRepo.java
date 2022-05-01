package com.mpp.repository;

import com.mpp.domain.Rezultat;
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

public class RezultatRepo implements Repository<Long,Rezultat>{
    private final JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger(RezultatRepo.class);

    public RezultatRepo(Properties props) {
        logger.info("Initializing RezultatRepo with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Rezultat findOne(Long aLong) {
        logger.traceEntry("finding rezultat () ", aLong);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select from Rezultat where id=?")) {
            preStmt.setLong(1, aLong);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    Long Rezultatid = result.getLong("id");
                    Long participantid = result.getLong("id");
                    Long punctaj = result.getLong("id");
                    Rezultat rez=new Rezultat(Rezultatid,participantid,punctaj);
                    rez.setId(id);
                    return rez;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        return null;
    }

    @Override
    public Iterable<Rezultat> findAll() {
        Set<Rezultat> users = new HashSet<>();
        logger.traceEntry("finding rezultate () ");
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * Rezultat")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    Long Rezultatid = result.getLong("id");
                    Long participantid = result.getLong("id");
                    Long punctaj = result.getLong("id");
                    Rezultat rez=new Rezultat(Rezultatid,participantid,punctaj);
                    rez.setId(id);
                    users.add(rez);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        return users;
    }

    @Override
    public Rezultat save(Rezultat elem) {
        logger.traceEntry("saving Rezultat () ", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Rezultat (probaID,participantID,punctaj) values (?,?,?)")) {
            preStmt.setLong(1, elem.getProbaID());
            preStmt.setLong(2, elem.getParticipantID());
            preStmt.setLong(3, elem.getPuncte());
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
    public Rezultat delete(Long aLong) {
        return null;
    }

    @Override
    public Rezultat update(Rezultat entity) {
        return null;
    }
}
