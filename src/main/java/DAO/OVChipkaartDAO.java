package main.java.DAO;

import main.java.POJO.OVChipkaart;
import main.java.POJO.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO {

    boolean save(
            OVChipkaart ovChipkaart)
            throws SQLException;

    boolean update(
            OVChipkaart ovChipkaart)
            throws SQLException;

    boolean delete(
            OVChipkaart ovChipkaart)
            throws SQLException;

    List<OVChipkaart> findByReiziger(
            Reiziger reiziger)
            throws SQLException;

    List<OVChipkaart> findAll()
            throws SQLException;
}