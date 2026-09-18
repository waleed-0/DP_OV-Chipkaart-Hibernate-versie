package main.java.DAO;

import main.java.POJO.Adres;
import main.java.POJO.OVChipkaart;
import main.java.POJO.Reiziger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main {

    private static final String Url =
            "jdbc:postgresql://localhost:5432/ovchip";

    private static final String User =
            "postgres";

    private static final String Password =
            "0000";


    private static final int TEST_REIZIGER_ID =
            100;

    private static final int TEST_ADRES_ID =
            100;

    private static final int TEST_KAART_ID_1 =
            123456;

    private static final int TEST_KAART_ID_2 =
            654321;


    public static void main(String[] args) {

        Connection conn =
                getConnection();

        if (conn == null) {

            System.out.println(
                    "Er is een fout opgetreden tijdens " +
                            "het maken van de databaseverbinding."
            );

            return;
        }

        try {


            AdresDAOPsql adresDAO =
                    new AdresDAOPsql(
                            conn
                    );

            ReizigerDAOPsql reizigerDAO =
                    new ReizigerDAOPsql(
                            conn
                    );

            OVChipkaartDAOPsql ovChipkaartDAO =
                    new OVChipkaartDAOPsql(
                            conn
                    );



            adresDAO.setReizigerDAO(
                    reizigerDAO
            );

            reizigerDAO.setAdresDAO(
                    adresDAO
            );



            reizigerDAO.setOVChipkaartDAO(
                    ovChipkaartDAO
            );



            verwijderOudeTestData(
                    conn
            );



            testAdresDAO(
                    adresDAO,
                    reizigerDAO
            );



            verwijderOudeTestData(
                    conn
            );



            testOVChipkaartDAO(
                    reizigerDAO,
                    ovChipkaartDAO
            );


        } catch (SQLException e) {

            System.out.println(
                    "Er is een databasefout opgetreden."
            );

            e.printStackTrace();

        } finally {

            closeConnection(
                    conn
            );
        }
    }



    private static Connection getConnection() {

        Connection connection =
                null;

        try {

            connection =
                    DriverManager.getConnection(
                            Url,
                            User,
                            Password
                    );

            System.out.println(
                    "Databaseverbinding is ok."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Databaseverbinding kon niet worden gemaakt."
            );

            e.printStackTrace();
        }

        return connection;
    }



    private static void closeConnection(
            Connection connection) {

        if (connection != null) {

            try {

                if (!connection.isClosed()) {

                    connection.close();

                    System.out.println(
                            "Databaseverbinding gesloten."
                    );
                }

            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }


    private static void verwijderOudeTestData(
            Connection conn)
            throws SQLException {

        System.out.println(
                "\n--- Oude testdata controleren ---"
        );



        String deleteKaarten =
                "DELETE FROM ov_chipkaart " +
                        "WHERE kaart_nummer = ? " +
                        "OR kaart_nummer = ?";


        try (PreparedStatement statement =
                     conn.prepareStatement(
                             deleteKaarten
                     )) {

            statement.setInt(
                    1,
                    TEST_KAART_ID_1
            );

            statement.setInt(
                    2,
                    TEST_KAART_ID_2
            );

            int aantalVerwijderd =
                    statement.executeUpdate();

            if (aantalVerwijderd > 0) {

                System.out.println(
                        aantalVerwijderd +
                                " oude test-OVChipkaart(en) verwijderd."
                );
            }
        }



        String deleteKaartenVanReiziger =
                "DELETE FROM ov_chipkaart " +
                        "WHERE reiziger_id = ?";


        try (PreparedStatement statement =
                     conn.prepareStatement(
                             deleteKaartenVanReiziger
                     )) {

            statement.setInt(
                    1,
                    TEST_REIZIGER_ID
            );

            statement.executeUpdate();
        }



        String deleteAdres =
                "DELETE FROM adres " +
                        "WHERE adres_id = ?";


        try (PreparedStatement statement =
                     conn.prepareStatement(
                             deleteAdres
                     )) {

            statement.setInt(
                    1,
                    TEST_ADRES_ID
            );

            int aantalVerwijderd =
                    statement.executeUpdate();

            if (aantalVerwijderd > 0) {

                System.out.println(
                        "Oud testadres met ID " +
                                TEST_ADRES_ID +
                                " verwijderd."
                );
            }
        }



        String deleteReiziger =
                "DELETE FROM reiziger " +
                        "WHERE reiziger_id = ?";


        try (PreparedStatement statement =
                     conn.prepareStatement(
                             deleteReiziger
                     )) {

            statement.setInt(
                    1,
                    TEST_REIZIGER_ID
            );

            int aantalVerwijderd =
                    statement.executeUpdate();

            if (aantalVerwijderd > 0) {

                System.out.println(
                        "Oude testreiziger met ID " +
                                TEST_REIZIGER_ID +
                                " verwijderd."
                );
            }
        }


        System.out.println(
                "Testdatabase is klaar voor de nieuwe test."
        );
    }



    public static void testAdresDAO(
            AdresDAO adresDAO,
            ReizigerDAO reizigerDAO)
            throws SQLException {

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "          TEST ADRESDAO"
        );

        System.out.println(
                "=========================================="
        );



        Reiziger reiziger1 =
                new Reiziger(
                        TEST_REIZIGER_ID,
                        "G.",
                        null,
                        "van Rijn",
                        Date.valueOf(
                                "2002-09-17"
                        )
                );



        System.out.println(
                "\n--- Reiziger opslaan ---"
        );

        boolean reizigerOpgeslagen =
                reizigerDAO.save(
                        reiziger1
                );

        System.out.println(
                "Reiziger opgeslagen: " +
                        reizigerOpgeslagen
        );

        if (!reizigerOpgeslagen) {

            System.out.println(
                    "Test gestopt: Reiziger kon niet worden opgeslagen."
            );

            return;
        }



        Adres adres1 =
                new Adres(
                        TEST_ADRES_ID,
                        "3511 LX",
                        "37",
                        "Straatnaam 1",
                        "Utrecht",
                        reiziger1
                );



        reiziger1.setAdres(
                adres1
        );

        adres1.setReiziger(
                reiziger1
        );



        System.out.println(
                "\n--- Adres opslaan ---"
        );

        boolean adresOpgeslagen =
                adresDAO.save(
                        adres1
                );

        System.out.println(
                "Adres opgeslagen: " +
                        adresOpgeslagen
        );

        if (!adresOpgeslagen) {

            reiziger1.setAdres(
                    null
            );

            reizigerDAO.delete(
                    reiziger1
            );

            return;
        }



        System.out.println(
                "\n--- Adres ophalen op ID ---"
        );

        Adres adresOpId =
                adresDAO.findById(
                        TEST_ADRES_ID
                );

        System.out.println(
                "Opgehaald adres: " +
                        adresOpId
        );



        System.out.println(
                "\n--- Adres wijzigen ---"
        );

        adres1.setPostcode(
                "3521 AL"
        );

        adres1.setHuisnummer(
                "6A"
        );

        boolean adresGewijzigd =
                adresDAO.update(
                        adres1
                );

        System.out.println(
                "Adres gewijzigd: " +
                        adresGewijzigd
        );

        System.out.println(
                "Gewijzigd adres: " +
                        adresDAO.findById(
                                TEST_ADRES_ID
                        )
        );



        System.out.println(
                "\n--- Adres ophalen via Reiziger ---"
        );

        Adres gevondenAdres =
                adresDAO.findByReiziger(
                        reiziger1
                );

        System.out.println(
                "Opgehaald adres: " +
                        gevondenAdres
        );



        System.out.println(
                "\n--- Alle adressen ---"
        );

        List<Adres> alleAdressen =
                adresDAO.findAll();

        for (Adres adres :
                alleAdressen) {

            System.out.println(
                    adres
            );
        }



        System.out.println(
                "\n--- Alle reizigers ---"
        );

        List<Reiziger> alleReizigers =
                reizigerDAO.findAll();

        for (Reiziger reiziger :
                alleReizigers) {

            System.out.println(
                    reiziger
            );
        }



        System.out.println(
                "\n--- Reiziger wijzigen ---"
        );

        reiziger1.setVoorletters(
                "G.A."
        );

        boolean reizigerGewijzigd =
                reizigerDAO.update(
                        reiziger1
                );

        System.out.println(
                "Reiziger gewijzigd: " +
                        reizigerGewijzigd
        );



        System.out.println(
                "\n--- Reiziger ophalen op ID ---"
        );

        Reiziger opgehaaldeReiziger =
                reizigerDAO.findById(
                        TEST_REIZIGER_ID
                );

        System.out.println(
                "Opgehaalde reiziger: " +
                        opgehaaldeReiziger
        );



        System.out.println(
                "\n--- Adres verwijderen ---"
        );

        boolean adresVerwijderd =
                adresDAO.delete(
                        adres1
                );

        System.out.println(
                "Adres verwijderd: " +
                        adresVerwijderd
        );

        if (adresVerwijderd) {

            reiziger1.setAdres(
                    null
            );

            adres1.setReiziger(
                    null
            );
        }



        System.out.println(
                "\n--- Controleren of adres verwijderd is ---"
        );

        System.out.println(
                "Adres na verwijderen: " +
                        adresDAO.findById(
                                TEST_ADRES_ID
                        )
        );



        System.out.println(
                "\n--- Reiziger verwijderen ---"
        );

        boolean reizigerVerwijderd =
                reizigerDAO.delete(
                        reiziger1
                );

        System.out.println(
                "Reiziger verwijderd: " +
                        reizigerVerwijderd
        );



        System.out.println(
                "\n--- Controleren of reiziger verwijderd is ---"
        );

        System.out.println(
                "Reiziger na verwijderen: " +
                        reizigerDAO.findById(
                                TEST_REIZIGER_ID
                        )
        );


        System.out.println(
                "\n---------- Einde Test AdresDAO ----------"
        );
    }



    public static void testOVChipkaartDAO(
            ReizigerDAO reizigerDAO,
            OVChipkaartDAO ovChipkaartDAO)
            throws SQLException {

        System.out.println(
                "\n\n=========================================="
        );

        System.out.println(
                "        P4 - TEST OVCHIPKAARTDAO"
        );

        System.out.println(
                "=========================================="
        );



        Reiziger reiziger =
                new Reiziger(
                        TEST_REIZIGER_ID,
                        "W.",
                        null,
                        "Test",
                        Date.valueOf(
                                "2000-01-01"
                        )
                );



        System.out.println(
                "\n--- Reiziger opslaan ---"
        );

        boolean reizigerOpgeslagen =
                reizigerDAO.save(
                        reiziger
                );

        System.out.println(
                "Reiziger opgeslagen: " +
                        reizigerOpgeslagen
        );

        if (!reizigerOpgeslagen) {

            System.out.println(
                    "P4-test gestopt."
            );

            return;
        }



        OVChipkaart kaart1 =
                new OVChipkaart(
                        TEST_KAART_ID_1,
                        LocalDate.of(
                                2028,
                                12,
                                31
                        ),
                        2,
                        25.50,
                        reiziger
                );

        OVChipkaart kaart2 =
                new OVChipkaart(
                        TEST_KAART_ID_2,
                        LocalDate.of(
                                2029,
                                6,
                                30
                        ),
                        1,
                        50.00,
                        reiziger
                );



        System.out.println(
                "\n--- Bidirectionele relatie maken ---"
        );

        boolean kaart1Toegevoegd =
                reiziger.voegToeOVChipkaart(
                        kaart1
                );

        boolean kaart2Toegevoegd =
                reiziger.voegToeOVChipkaart(
                        kaart2
                );

        System.out.println(
                "Kaart 1 toegevoegd aan Reiziger: " +
                        kaart1Toegevoegd
        );

        System.out.println(
                "Kaart 2 toegevoegd aan Reiziger: " +
                        kaart2Toegevoegd
        );

        System.out.println(
                "Aantal kaarten in Reiziger-object: " +
                        reiziger.getOvChipkaarten().size()
        );

        System.out.println(
                "Reiziger van kaart 1: #" +
                        kaart1.getReiziger().getId()
        );

        System.out.println(
                "Reiziger van kaart 2: #" +
                        kaart2.getReiziger().getId()
        );



        System.out.println(
                "\n--- OVChipkaarten opslaan ---"
        );

        boolean kaart1Opgeslagen =
                ovChipkaartDAO.save(
                        kaart1
                );

        boolean kaart2Opgeslagen =
                ovChipkaartDAO.save(
                        kaart2
                );

        System.out.println(
                "Kaart 1 opgeslagen: " +
                        kaart1Opgeslagen
        );

        System.out.println(
                "Kaart 2 opgeslagen: " +
                        kaart2Opgeslagen
        );



        System.out.println(
                "\n--- OVChipkaarten ophalen via Reiziger ---"
        );

        List<OVChipkaart> kaartenVanReiziger =
                ovChipkaartDAO.findByReiziger(
                        reiziger
                );

        for (OVChipkaart kaart :
                kaartenVanReiziger) {

            System.out.println(
                    kaart
            );
        }



        System.out.println(
                "\n--- Alle OVChipkaarten ---"
        );

        List<OVChipkaart> alleKaarten =
                ovChipkaartDAO.findAll();

        for (OVChipkaart kaart :
                alleKaarten) {

            System.out.println(
                    kaart
            );
        }



        System.out.println(
                "\n--- OVChipkaart wijzigen ---"
        );

        kaart1.setSaldo(
                75.75
        );

        kaart1.setKlasse(
                1
        );

        kaart1.setGeldig_tot(
                LocalDate.of(
                        2030,
                        12,
                        31
                )
        );

        boolean kaartGewijzigd =
                ovChipkaartDAO.update(
                        kaart1
                );

        System.out.println(
                "OVChipkaart gewijzigd: " +
                        kaartGewijzigd
        );



        System.out.println(
                "\n--- Wijziging controleren ---"
        );

        List<OVChipkaart> kaartenNaUpdate =
                ovChipkaartDAO.findByReiziger(
                        reiziger
                );

        for (OVChipkaart kaart :
                kaartenNaUpdate) {

            System.out.println(
                    kaart
            );
        }


        System.out.println(
                "\n--- Reiziger ophalen inclusief OVChipkaarten ---"
        );

        Reiziger reizigerUitDatabase =
                reizigerDAO.findById(
                        TEST_REIZIGER_ID
                );

        System.out.println(
                reizigerUitDatabase
        );

        if (reizigerUitDatabase != null) {

            System.out.println(
                    "Aantal OVChipkaarten: " +
                            reizigerUitDatabase
                                    .getOvChipkaarten()
                                    .size()
            );
        }



        System.out.println(
                "\n--- Alle Reizigers inclusief OVChipkaarten ---"
        );

        List<Reiziger> alleReizigers =
                reizigerDAO.findAll();

        for (Reiziger r :
                alleReizigers) {

            System.out.println(
                    r
            );
        }



        System.out.println(
                "\n--- OVChipkaart 1 verwijderen ---"
        );

        boolean kaart1Verwijderd =
                ovChipkaartDAO.delete(
                        kaart1
                );

        System.out.println(
                "Kaart 1 verwijderd uit database: " +
                        kaart1Verwijderd
        );

        if (kaart1Verwijderd) {

            boolean verwijderdUitObject =
                    reiziger.verwijderOVChipkaart(
                            kaart1
                    );

            System.out.println(
                    "Kaart 1 verwijderd uit Reiziger-object: " +
                            verwijderdUitObject
            );
        }



        System.out.println(
                "\n--- OVChipkaart 2 verwijderen ---"
        );

        boolean kaart2Verwijderd =
                ovChipkaartDAO.delete(
                        kaart2
                );

        System.out.println(
                "Kaart 2 verwijderd uit database: " +
                        kaart2Verwijderd
        );

        if (kaart2Verwijderd) {

            boolean verwijderdUitObject =
                    reiziger.verwijderOVChipkaart(
                            kaart2
                    );

            System.out.println(
                    "Kaart 2 verwijderd uit Reiziger-object: " +
                            verwijderdUitObject
            );
        }


        System.out.println(
                "\n--- Controleren of OVChipkaarten verwijderd zijn ---"
        );

        List<OVChipkaart> kaartenNaDelete =
                ovChipkaartDAO.findByReiziger(
                        reiziger
                );

        System.out.println(
                "Aantal kaarten in database voor testreiziger: " +
                        kaartenNaDelete.size()
        );

        System.out.println(
                "Aantal kaarten in Java Reiziger-object: " +
                        reiziger.getOvChipkaarten().size()
        );



        System.out.println(
                "\n--- Testreiziger verwijderen ---"
        );

        boolean reizigerVerwijderd =
                reizigerDAO.delete(
                        reiziger
                );

        System.out.println(
                "Reiziger verwijderd: " +
                        reizigerVerwijderd
        );



        System.out.println(
                "\n--- Controleren of Reiziger verwijderd is ---"
        );

        Reiziger controle =
                reizigerDAO.findById(
                        TEST_REIZIGER_ID
                );

        System.out.println(
                "Reiziger na verwijderen: " +
                        controle
        );



        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "      EINDE P4 OVCHIPKAART TEST"
        );

        System.out.println(
                "=========================================="
        );
    }
}

