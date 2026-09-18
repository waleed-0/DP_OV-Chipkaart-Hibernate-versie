package main.java.DAO;

import main.java.POJO.OVChipkaart;
import main.java.POJO.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql
        implements OVChipkaartDAO {

    private Connection conn;

    public OVChipkaartDAOPsql(
            Connection conn) {

        this.conn = conn;
    }


    @Override
    public boolean save(
            OVChipkaart ovChipkaart)
            throws SQLException {

        if (ovChipkaart == null) {
            return false;
        }

        String query =
                "INSERT INTO ov_chipkaart " +
                        "(kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    ovChipkaart.getKaart_nummer()
            );

            statement.setDate(
                    2,
                    java.sql.Date.valueOf(
                            ovChipkaart.getGeldig_tot()
                    )
            );

            statement.setInt(
                    3,
                    ovChipkaart.getKlasse()
            );

            statement.setDouble(
                    4,
                    ovChipkaart.getSaldo()
            );

            if (ovChipkaart.getReiziger() != null) {

                statement.setInt(
                        5,
                        ovChipkaart
                                .getReiziger()
                                .getId()
                );

            } else {

                statement.setNull(
                        5,
                        java.sql.Types.INTEGER
                );
            }

            int result =
                    statement.executeUpdate();

            return result > 0;
        }
    }


    @Override
    public boolean update(
            OVChipkaart ovChipkaart)
            throws SQLException {

        if (ovChipkaart == null) {
            return false;
        }

        String query =
                "UPDATE ov_chipkaart " +
                        "SET geldig_tot = ?, " +
                        "klasse = ?, " +
                        "saldo = ?, " +
                        "reiziger_id = ? " +
                        "WHERE kaart_nummer = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setDate(
                    1,
                    java.sql.Date.valueOf(
                            ovChipkaart.getGeldig_tot()
                    )
            );

            statement.setInt(
                    2,
                    ovChipkaart.getKlasse()
            );

            statement.setDouble(
                    3,
                    ovChipkaart.getSaldo()
            );


            if (ovChipkaart.getReiziger() != null) {

                statement.setInt(
                        4,
                        ovChipkaart
                                .getReiziger()
                                .getId()
                );

            } else {

                statement.setNull(
                        4,
                        java.sql.Types.INTEGER
                );
            }

            statement.setInt(
                    5,
                    ovChipkaart.getKaart_nummer()
            );

            int result =
                    statement.executeUpdate();

            return result > 0;
        }
    }


    @Override
    public boolean delete(
            OVChipkaart ovChipkaart)
            throws SQLException {

        if (ovChipkaart == null) {
            return false;
        }

        String query =
                "DELETE FROM ov_chipkaart " +
                        "WHERE kaart_nummer = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    ovChipkaart.getKaart_nummer()
            );

            int result =
                    statement.executeUpdate();

            return result > 0;
        }
    }

    @Override
    public List<OVChipkaart> findByReiziger(
            Reiziger reiziger)
            throws SQLException {

        List<OVChipkaart> ovChipkaarten =
                new ArrayList<>();

        if (reiziger == null) {
            return ovChipkaarten;
        }

        String query =
                "SELECT kaart_nummer, " +
                        "geldig_tot, " +
                        "klasse, " +
                        "saldo, " +
                        "reiziger_id " +
                        "FROM ov_chipkaart " +
                        "WHERE reiziger_id = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    reiziger.getId()
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    int kaartNummer =
                            resultSet.getInt(
                                    "kaart_nummer"
                            );

                    java.sql.Date geldigTot =
                            resultSet.getDate(
                                    "geldig_tot"
                            );

                    int klasse =
                            resultSet.getInt(
                                    "klasse"
                            );

                    double saldo =
                            resultSet.getDouble(
                                    "saldo"
                            );

                    OVChipkaart ovChipkaart =
                            new OVChipkaart(
                                    kaartNummer,
                                    geldigTot.toLocalDate(),
                                    klasse,
                                    saldo,
                                    reiziger
                            );

                    ovChipkaarten.add(
                            ovChipkaart
                    );
                }
            }
        }

        return ovChipkaarten;
    }


    @Override
    public List<OVChipkaart> findAll()
            throws SQLException {

        List<OVChipkaart> ovChipkaarten =
                new ArrayList<>();

        String query =
                "SELECT " +
                        "o.kaart_nummer, " +
                        "o.geldig_tot, " +
                        "o.klasse, " +
                        "o.saldo, " +
                        "o.reiziger_id, " +
                        "r.voorletters, " +
                        "r.tussenvoegsel, " +
                        "r.achternaam, " +
                        "r.geboortedatum " +
                        "FROM ov_chipkaart o " +
                        "LEFT JOIN reiziger r " +
                        "ON o.reiziger_id = r.reiziger_id";

        try (PreparedStatement statement =
                     conn.prepareStatement(query);

             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                int kaartNummer =
                        resultSet.getInt(
                                "kaart_nummer"
                        );

                java.sql.Date geldigTot =
                        resultSet.getDate(
                                "geldig_tot"
                        );

                int klasse =
                        resultSet.getInt(
                                "klasse"
                        );

                double saldo =
                        resultSet.getDouble(
                                "saldo"
                        );

                int reizigerId =
                        resultSet.getInt(
                                "reiziger_id"
                        );

                boolean reizigerIdWasNull =
                        resultSet.wasNull();

                Reiziger reiziger =
                        null;

                if (!reizigerIdWasNull) {

                    reiziger =
                            new Reiziger(
                                    reizigerId,
                                    resultSet.getString(
                                            "voorletters"
                                    ),
                                    resultSet.getString(
                                            "tussenvoegsel"
                                    ),
                                    resultSet.getString(
                                            "achternaam"
                                    ),
                                    resultSet.getDate(
                                            "geboortedatum"
                                    )
                            );
                }

                OVChipkaart ovChipkaart =
                        new OVChipkaart(
                                kaartNummer,
                                geldigTot.toLocalDate(),
                                klasse,
                                saldo,
                                reiziger
                        );

                if (reiziger != null) {

                    reiziger.voegToeOVChipkaart(
                            ovChipkaart
                    );
                }

                ovChipkaarten.add(
                        ovChipkaart
                );
            }
        }

        return ovChipkaarten;
    }
}