package com.mpp.repository;

import com.mpp.domain.NumPct;
import com.mpp.domain.Participant;
import com.mpp.domain.Rezultat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mpp.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ParticipantRepo implements ParticipantRepoInterface{
    private final JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger(ParticipantRepo.class);

    public ParticipantRepo(Properties props) {
        logger.info("Initializing CarsDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }


    @Override
    public Participant findOne(Long aLong) {
        logger.traceEntry("finding participant () ", aLong);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select from Participant where id=?")) {
            preStmt.setLong(1, aLong);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    String nume = result.getString("nume");
                    Long probaID = result.getLong("rezultatProbaID");
                    Participant ar=new Participant(nume);
                    ar.setId(id);
                    /**
                    de facut pt lista
                     */
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
    public Iterable<Participant> findAll() {
        Set<Participant> users = new HashSet<>();
        logger.traceEntry("finding participanti () ");
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Participant")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    String nume = result.getString("nume");
                    Participant ar=new Participant(nume);
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
    public Participant save(Participant elem) {
        logger.traceEntry("saving participant () ", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Participant (nume) values (?)")) {
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


//    public List<NumPct> get_NumPct_SortNume(Long proba)
//    {
//        List<NumPct> list=new ArrayList<>();
//        logger.traceEntry("lista participanti+puncte ");
//        Connection con = dbUtils.getConnection();
//        try (PreparedStatement preStmt = con.prepareStatement("select * from Participant ORDER BY nume")) {
//            try (ResultSet result = preStmt.executeQuery()) {
//                while (result.next()) {
//                    Long id = result.getLong("id");
//                    String nume = result.getString("nume");
//                    Participant ar=new Participant(nume);
//                    ar.setId(id);
//                    users.add(ar);
//                }
//            }
//        } catch (SQLException e) {
//            logger.error(e);
//            System.err.println("Error DB " + e);
//        }
//        return list;
//    }



    public Long get_participant_punctajTotal_proba(Long participant)
    {
        Long suma=0L;
        logger.traceEntry("nr total puncte pt participant () ", participant);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Rezultat where participantID=? ")) {
            preStmt.setLong(1, participant);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Long pct=result.getLong("punctaj");
                    suma+=pct;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        return suma;
    }




    @Override
    public Participant delete(Long aLong) {
        return null;
    }

    @Override
    public Participant update(Participant entity) {
        return null;
    }
}
