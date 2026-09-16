package main.java.DAO;

import main.java.POJO.Adres;
import main.java.POJO.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {

    private Connection conn;
    private ReizigerDAO rdao;

    public AdresDAOPsql(Connection connection) {
        this.conn = connection;
    }

    public void setReizigerDAO(ReizigerDAO rdao) {
        this.rdao = rdao;
    }

    @Override
    public boolean save(Adres adres) throws SQLException {

        if (adres == null) {
            return false;
        }

        String query =
                "INSERT INTO adres " +
                        "(adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    adres.getAdres_id()
            );

            statement.setString(
                    2,
                    adres.getPostcode()
            );

            statement.setString(
                    3,
                    adres.getHuisnummer()
            );

            statement.setString(
                    4,
                    adres.getStraat()
            );

            statement.setString(
                    5,
                    adres.getWoonplaats()
            );

            if (adres.getReiziger() != null) {

                statement.setInt(
                        6,
                        adres.getReiziger().getId()
                );

            } else {

                statement.setNull(
                        6,
                        java.sql.Types.INTEGER
                );
            }

            int result =
                    statement.executeUpdate();

            return result > 0;
        }
    }

    @Override
    public boolean update(Adres adres) throws SQLException {

        if (adres == null) {
            return false;
        }

        String query =
                "UPDATE adres " +
                        "SET postcode = ?, " +
                        "huisnummer = ?, " +
                        "straat = ?, " +
                        "woonplaats = ?, " +
                        "reiziger_id = ? " +
                        "WHERE adres_id = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setString(
                    1,
                    adres.getPostcode()
            );

            statement.setString(
                    2,
                    adres.getHuisnummer()
            );

            statement.setString(
                    3,
                    adres.getStraat()
            );

            statement.setString(
                    4,
                    adres.getWoonplaats()
            );

            if (adres.getReiziger() != null) {

                statement.setInt(
                        5,
                        adres.getReiziger().getId()
                );

            } else {

                statement.setNull(
                        5,
                        java.sql.Types.INTEGER
                );
            }

            statement.setInt(
                    6,
                    adres.getAdres_id()
            );

            int result =
                    statement.executeUpdate();

            return result > 0;
        }
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {

        if (adres == null) {
            return false;
        }

        String query =
                "DELETE FROM adres " +
                        "WHERE adres_id = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    adres.getAdres_id()
            );

            int result =
                    statement.executeUpdate();

            return result > 0;
        }
    }




    @Override
    public Adres findByReiziger(Reiziger reiziger)
            throws SQLException {

        if (reiziger == null) {
            return null;
        }

        Adres adres = null;

        String query =
                "SELECT adres_id, postcode, huisnummer, straat, " +
                        "woonplaats, reiziger_id " +
                        "FROM adres " +
                        "WHERE reiziger_id = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    reiziger.getId()
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    int adresId =
                            resultSet.getInt(
                                    "adres_id"
                            );

                    String postcode =
                            resultSet.getString(
                                    "postcode"
                            );

                    String huisnummer =
                            resultSet.getString(
                                    "huisnummer"
                            );

                    String straat =
                            resultSet.getString(
                                    "straat"
                            );

                    String woonplaats =
                            resultSet.getString(
                                    "woonplaats"
                            );

                    adres =
                            new Adres(
                                    adresId,
                                    postcode,
                                    huisnummer,
                                    straat,
                                    woonplaats,
                                    reiziger
                            );

                    reiziger.setAdres(
                            adres
                    );
                }
            }
        }

        return adres;
    }

    @Override
    public List<Adres> findAll()
            throws SQLException {

        List<Adres> adressen =
                new ArrayList<>();

        if (rdao == null) {
            throw new IllegalStateException(
                    "ReizigerDAO is niet gekoppeld aan AdresDAOPsql."
            );
        }

        String query =
                "SELECT adres_id, postcode, huisnummer, " +
                        "straat, woonplaats, reiziger_id " +
                        "FROM adres";

        try (PreparedStatement statement =
                     conn.prepareStatement(query);

             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                int adresId =
                        resultSet.getInt(
                                "adres_id"
                        );

                String postcode =
                        resultSet.getString(
                                "postcode"
                        );

                String huisnummer =
                        resultSet.getString(
                                "huisnummer"
                        );

                String straat =
                        resultSet.getString(
                                "straat"
                        );

                String woonplaats =
                        resultSet.getString(
                                "woonplaats"
                        );

                int reizigerId =
                        resultSet.getInt(
                                "reiziger_id"
                        );

                Reiziger reiziger =
                        rdao.findById(
                                reizigerId
                        );

                Adres adres =
                        new Adres(
                                adresId,
                                postcode,
                                huisnummer,
                                straat,
                                woonplaats,
                                reiziger
                        );

                if (reiziger != null) {

                    reiziger.setAdres(
                            adres
                    );
                }

                adressen.add(
                        adres
                );
            }
        }

        return adressen;
    }


    @Override
    public Adres findById(int id)
            throws SQLException {

        Adres adres = null;

        String query =
                "SELECT adres_id, postcode, huisnummer, straat, " +
                        "woonplaats, reiziger_id " +
                        "FROM adres " +
                        "WHERE adres_id = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    id
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    int adresId =
                            resultSet.getInt(
                                    "adres_id"
                            );

                    String postcode =
                            resultSet.getString(
                                    "postcode"
                            );

                    String huisnummer =
                            resultSet.getString(
                                    "huisnummer"
                            );

                    String straat =
                            resultSet.getString(
                                    "straat"
                            );

                    String woonplaats =
                            resultSet.getString(
                                    "woonplaats"
                            );

                    int reizigerId =
                            resultSet.getInt(
                                    "reiziger_id"
                            );

                    Reiziger reiziger = null;

                    if (!resultSet.wasNull() &&
                            rdao != null) {

                        reiziger =
                                rdao.findById(
                                        reizigerId
                                );
                    }

                    adres =
                            new Adres(
                                    adresId,
                                    postcode,
                                    huisnummer,
                                    straat,
                                    woonplaats,
                                    reiziger
                            );

                    if (reiziger != null) {
                        reiziger.setAdres(
                                adres
                        );
                    }
                }
            }
        }

        return adres;
    }
}