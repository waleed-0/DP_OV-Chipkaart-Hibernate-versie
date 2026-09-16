package main.java.DAO;

import main.java.POJO.Adres;
import main.java.POJO.Reiziger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {

    private static final String Url =
            "jdbc:postgresql://localhost:5432/ovchip";

    private static final String User =
            "postgres";

    private static final String Password =
            "0000";

    public static void main(String[] args)
            throws SQLException {

        Connection conn = getConnection();

        if (conn != null) {

            AdresDAOPsql adresDAO =
                    new AdresDAOPsql(conn);

            ReizigerDAOPsql reizigerDAO =
                    new ReizigerDAOPsql(conn);

            adresDAO.setReizigerDAO(reizigerDAO);
            reizigerDAO.setAdresDAO(adresDAO);

            testAdresDAO(
                    adresDAO,
                    reizigerDAO
            );

            closeConnection(conn);

        } else {

            System.out.println(
                    "Er is een fout opgetreden tijdens " +
                            "het maken van de databaseverbinding."
            );
        }
    }

    private static Connection getConnection() {

        Connection connection = null;

        try {

            connection = DriverManager.getConnection(
                    Url,
                    User,
                    Password
            );

            System.out.println(
                    "Databaseverbinding is ok."
            );

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return connection;
    }

    private static void closeConnection(
            Connection connection) {

        if (connection != null) {

            try {

                connection.close();

                System.out.println(
                        "Databaseverbinding gesloten."
                );

            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }

    public static void testAdresDAO(
            AdresDAO adresDAO,
            ReizigerDAO reizigerDAO)
            throws SQLException {

        System.out.println(
                "\n---------- Test AdresDAO -------------"
        );


        Reiziger reiziger1 =
                new Reiziger(
                        100,
                        "G.",
                        null,
                        "van Rijn",
                        Date.valueOf("2002-09-17")
                );


        System.out.println(
                "\n--- Reiziger opslaan ---"
        );

        boolean reizigerOpgeslagen =
                reizigerDAO.save(reiziger1);

        System.out.println(
                "Reiziger opgeslagen: "
                        + reizigerOpgeslagen
        );



        Adres adres1 =
                new Adres(
                        100,
                        "3511 LX",
                        "37",
                        "Straatnaam 1",
                        "Utrecht",
                        reiziger1
                );



        reiziger1.setAdres(adres1);
        adres1.setReiziger(reiziger1);

        System.out.println(
                "\n--- Adres opslaan ---"
        );

        boolean adresOpgeslagen =
                adresDAO.save(adres1);

        System.out.println(
                "Adres opgeslagen: "
                        + adresOpgeslagen
        );


        System.out.println(
                "\n--- Adres ophalen op ID ---"
        );

        Adres adresOpId =
                adresDAO.findById(100);

        System.out.println(
                "Opgehaald adres: "
                        + adresOpId
        );


        System.out.println(
                "\n--- Adres wijzigen ---"
        );

        adres1.setPostcode("3521 AL");
        adres1.setHuisnummer("6A");

        boolean adresGewijzigd =
                adresDAO.update(adres1);

        System.out.println(
                "Adres gewijzigd: "
                        + adresGewijzigd
        );

        System.out.println(
                "Adres: "
                        + adresDAO.findById(100)
        );


        System.out.println(
                "\n--- Adres ophalen via Reiziger ---"
        );

        Adres gevondenAdres =
                adresDAO.findByReiziger(
                        reiziger1
                );

        System.out.println(
                "Opgehaald adres: "
                        + gevondenAdres
        );


        System.out.println(
                "\n--- Alle adressen ---"
        );

        List<Adres> alleAdressen =
                adresDAO.findAll();

        for (Adres adres : alleAdressen) {

            System.out.println(adres);
        }


        System.out.println(
                "\n--- Alle reizigers ---"
        );

        List<Reiziger> alleReizigers =
                reizigerDAO.findAll();

        for (Reiziger reiziger : alleReizigers) {

            System.out.println(reiziger);
        }


        System.out.println(
                "\n--- Reiziger wijzigen ---"
        );

        reiziger1.setVoorletters("G.A.");

        boolean reizigerGewijzigd =
                reizigerDAO.update(reiziger1);

        System.out.println(
                "Reiziger gewijzigd: "
                        + reizigerGewijzigd
        );


        System.out.println(
                "\n--- Reiziger ophalen op ID ---"
        );

        Reiziger opgehaaldeReiziger =
                reizigerDAO.findById(100);

        System.out.println(
                "Opgehaalde reiziger: "
                        + opgehaaldeReiziger
        );


        System.out.println(
                "\n--- Adres verwijderen ---"
        );

        boolean adresVerwijderd =
                adresDAO.delete(adres1);

        System.out.println(
                "Adres verwijderd: "
                        + adresVerwijderd
        );

        if (adresVerwijderd) {

            reiziger1.setAdres(null);
            adres1.setReiziger(null);
        }


        System.out.println(
                "\n--- Reiziger verwijderen ---"
        );

        boolean reizigerVerwijderd =
                reizigerDAO.delete(reiziger1);

        System.out.println(
                "Reiziger verwijderd: "
                        + reizigerVerwijderd
        );

        System.out.println(
                "\n---------- Einde Test AdresDAO -------------"
        );
    }
}