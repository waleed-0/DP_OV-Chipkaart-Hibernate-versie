package main.java.DAO;

import main.java.POJO.Adres;
import main.java.POJO.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {

    private Connection conn;
    private AdresDAO adao;

    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
    }

    public void setAdresDAO(AdresDAO adao) {
        this.adao = adao;
    }

    @Override
    public boolean save(Reiziger reiziger) {

        if (reiziger == null) {
            return false;
        }

        String query =
                "INSERT INTO reiziger " +
                        "(reiziger_id, voorletters, tussenvoegsel, " +
                        "achternaam, geboortedatum) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    reiziger.getId()
            );

            statement.setString(
                    2,
                    reiziger.getVoorletters()
            );

            statement.setString(
                    3,
                    reiziger.getTussenvoegsel()
            );

            statement.setString(
                    4,
                    reiziger.getAchternaam()
            );

            statement.setDate(
                    5,
                    reiziger.getGeboortedatum()
            );

            int result =
                    statement.executeUpdate();

            if (result <= 0) {
                return false;
            }

            if (reiziger.getAdres() == null) {
                return true;
            }

            reiziger.getAdres()
                    .setReiziger(
                            reiziger
                    );

            if (adao == null) {
                return false;
            }

            return adao.save(
                    reiziger.getAdres()
            );

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger) {

        if (reiziger == null) {
            return false;
        }

        String query =
                "UPDATE reiziger " +
                        "SET voorletters = ?, " +
                        "tussenvoegsel = ?, " +
                        "achternaam = ?, " +
                        "geboortedatum = ? " +
                        "WHERE reiziger_id = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setString(
                    1,
                    reiziger.getVoorletters()
            );

            statement.setString(
                    2,
                    reiziger.getTussenvoegsel()
            );

            statement.setString(
                    3,
                    reiziger.getAchternaam()
            );

            statement.setDate(
                    4,
                    reiziger.getGeboortedatum()
            );

            statement.setInt(
                    5,
                    reiziger.getId()
            );

            int result =
                    statement.executeUpdate();

            if (result <= 0) {
                return false;
            }

            if (reiziger.getAdres() == null) {
                return true;
            }

            if (adao == null) {
                return false;
            }

            reiziger.getAdres()
                    .setReiziger(
                            reiziger
                    );

            return adao.update(
                    reiziger.getAdres()
            );

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) {

        if (reiziger == null) {
            return false;
        }

        try {

            if (reiziger.getAdres() != null) {

                if (adao == null) {
                    return false;
                }

                boolean adresVerwijderd =
                        adao.delete(
                                reiziger.getAdres()
                        );

                if (!adresVerwijderd) {
                    return false;
                }
            }


            String query =
                    "DELETE FROM reiziger " +
                            "WHERE reiziger_id = ?";

            try (PreparedStatement statement =
                         conn.prepareStatement(query)) {

                statement.setInt(
                        1,
                        reiziger.getId()
                );

                int result =
                        statement.executeUpdate();

                return result > 0;
            }

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }

    @Override
    public Reiziger findById(int id) {

        String query =
                "SELECT reiziger_id, voorletters, " +
                        "tussenvoegsel, achternaam, geboortedatum " +
                        "FROM reiziger " +
                        "WHERE reiziger_id = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setInt(
                    1,
                    id
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    Reiziger reiziger =
                            new Reiziger(
                                    resultSet.getInt(
                                            "reiziger_id"
                                    ),
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

                    if (adao != null) {

                        Adres adres =
                                adao.findByReiziger(
                                        reiziger
                                );

                        reiziger.setAdres(
                                adres
                        );
                    }

                    return reiziger;
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Reiziger> findByGbdatum(
            String datum) {

        List<Reiziger> reizigers =
                new ArrayList<>();

        String query =
                "SELECT reiziger_id, voorletters, " +
                        "tussenvoegsel, achternaam, geboortedatum " +
                        "FROM reiziger " +
                        "WHERE geboortedatum = ?";

        try (PreparedStatement statement =
                     conn.prepareStatement(query)) {

            statement.setDate(
                    1,
                    java.sql.Date.valueOf(
                            datum
                    )
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    Reiziger reiziger =
                            new Reiziger(
                                    resultSet.getInt(
                                            "reiziger_id"
                                    ),
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

                    if (adao != null) {

                        Adres adres =
                                adao.findByReiziger(
                                        reiziger
                                );

                        reiziger.setAdres(
                                adres
                        );
                    }

                    reizigers.add(
                            reiziger
                    );
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() {

        List<Reiziger> reizigers =
                new ArrayList<>();

        String query =
                "SELECT reiziger_id, voorletters, " +
                        "tussenvoegsel, achternaam, geboortedatum " +
                        "FROM reiziger";

        try (PreparedStatement statement =
                     conn.prepareStatement(query);

             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Reiziger reiziger =
                        new Reiziger(
                                resultSet.getInt(
                                        "reiziger_id"
                                ),
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

                if (adao != null) {

                    Adres adres =
                            adao.findByReiziger(
                                    reiziger
                            );

                    reiziger.setAdres(
                            adres
                    );
                }

                reizigers.add(
                        reiziger
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return reizigers;
    }
}