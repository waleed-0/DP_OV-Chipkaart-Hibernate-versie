package main.java.DAO;

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


    /*
     * ==========================================
     * P4 TEST-ID'S
     * ==========================================
     */

    private static final int TEST_REIZIGER_ID =
            100;

    private static final int TEST_KAART_ID_1 =
            123456;

    private static final int TEST_KAART_ID_2 =
            654321;


    /*
     * ==========================================
     * MAIN
     * ==========================================
     */

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

            /*
             * ==========================================
             * P4 DAO'S AANMAKEN
             * ==========================================
             */

            ReizigerDAOPsql reizigerDAO =
                    new ReizigerDAOPsql(
                            conn
                    );

            OVChipkaartDAOPsql ovChipkaartDAO =
                    new OVChipkaartDAOPsql(
                            conn
                    );


            /*
             * ==========================================
             * P4 DAO'S KOPPELEN
             * ==========================================
             *
             * ReizigerDAOPsql gebruikt
             * OVChipkaartDAO om bij het ophalen
             * van een Reiziger ook zijn
             * OVChipkaarten te koppelen.
             */

            reizigerDAO.setOVChipkaartDAO(
                    ovChipkaartDAO
            );


            /*
             * ==========================================
             * OUDE P4-TESTDATA OPRUIMEN
             * ==========================================
             */

            verwijderOudeP4TestData(
                    conn
            );


            /*
             * ==========================================
             * P4 TEST UITVOEREN
             * ==========================================
             */

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


    /*
     * ==========================================
     * DATABASEVERBINDING OPENEN
     * ==========================================
     */

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


    /*
     * ==========================================
     * DATABASEVERBINDING SLUITEN
     * ==========================================
     */

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


    /*
     * ==========================================
     * OUDE P4-TESTDATA VERWIJDEREN
     * ==========================================
     *
     * Eerst verwijderen we OVChipkaarten.
     *
     * Daarna verwijderen we de testreiziger.
     *
     * Dit is nodig vanwege de foreign key:
     *
     * ov_chipkaart.reiziger_id
     *          ->
     * reiziger.reiziger_id
     */

    private static void verwijderOudeP4TestData(
            Connection conn)
            throws SQLException {

        System.out.println(
                "\n--- Oude P4-testdata controleren ---"
        );


        /*
         * ==========================================
         * 1. TESTKAARTEN VERWIJDEREN
         * ==========================================
         */

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


        /*
         * ==========================================
         * 2. ALLE KAARTEN VAN TESTREIZIGER
         *    VERWIJDEREN
         * ==========================================
         *
         * Extra beveiliging voor het geval
         * een eerdere test andere kaarten
         * aan Reiziger 100 heeft gekoppeld.
         */

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


        /*
         * ==========================================
         * 3. EVENTUEEL ADRES VAN TESTREIZIGER
         *    VERWIJDEREN
         * ==========================================
         *
         * Main test Adres niet meer.
         *
         * Deze DELETE is alleen een beveiliging
         * voor oude testdata uit eerdere runs.
         *
         * Anders kan de foreign key van adres
         * verhinderen dat Reiziger 100 wordt
         * verwijderd.
         */

        String deleteAdres =
                "DELETE FROM adres " +
                        "WHERE reiziger_id = ?";


        try (PreparedStatement statement =
                     conn.prepareStatement(
                             deleteAdres
                     )) {

            statement.setInt(
                    1,
                    TEST_REIZIGER_ID
            );

            statement.executeUpdate();
        }


        /*
         * ==========================================
         * 4. TESTREIZIGER VERWIJDEREN
         * ==========================================
         */

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
                        "Oude P4-testreiziger met ID " +
                                TEST_REIZIGER_ID +
                                " verwijderd."
                );
            }
        }


        System.out.println(
                "Database is klaar voor de P4-test."
        );
    }


    /*
     * ==========================================
     * P4
     * TEST OVCHIPKAARTDAO
     * ==========================================
     *
     * Deze methode test uitsluitend P4:
     *
     * - Reiziger opslaan
     * - één-op-veel-relatie
     * - bidirectionele relatie
     * - voegToeOVChipkaart()
     * - OVChipkaart save()
     * - findByReiziger()
     * - findAll()
     * - update()
     * - Reiziger opnieuw ophalen
     * - OVChipkaarten bij Reiziger
     * - delete()
     * - verwijderOVChipkaart()
     */

    public static void testOVChipkaartDAO(
            ReizigerDAO reizigerDAO,
            OVChipkaartDAO ovChipkaartDAO)
            throws SQLException {

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "        P4 - TEST OVCHIPKAARTDAO"
        );

        System.out.println(
                "=========================================="
        );


        /*
         * ==========================================
         * 1. TESTREIZIGER MAKEN
         * ==========================================
         */

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


        /*
         * ==========================================
         * 2. REIZIGER OPSLAAN
         * ==========================================
         */

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
                    "P4-test gestopt: Reiziger kon niet worden opgeslagen."
            );

            return;
        }


        /*
         * ==========================================
         * 3. TWEE OVCHIPKAARTEN MAKEN
         * ==========================================
         */

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


        /*
         * ==========================================
         * 4. BIDIRECTIONELE RELATIE MAKEN
         * ==========================================
         */

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
                        reiziger
                                .getOvChipkaarten()
                                .size()
        );


        System.out.println(
                "Reiziger van kaart 1: #" +
                        kaart1
                                .getReiziger()
                                .getId()
        );


        System.out.println(
                "Reiziger van kaart 2: #" +
                        kaart2
                                .getReiziger()
                                .getId()
        );


        /*
         * ==========================================
         * 5. OVCHIPKAARTEN OPSLAAN
         * ==========================================
         */

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


        /*
         * ==========================================
         * 6. FIND BY REIZIGER
         * ==========================================
         */

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


        /*
         * ==========================================
         * 7. FIND ALL OVCHIPKAARTEN
         * ==========================================
         */

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


        /*
         * ==========================================
         * 8. OVCHIPKAART WIJZIGEN
         * ==========================================
         */

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


        /*
         * ==========================================
         * 9. UPDATE CONTROLEREN
         * ==========================================
         */

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


        /*
         * ==========================================
         * 10. REIZIGER OPNIEUW OPHALEN
         * ==========================================
         *
         * Hiermee controleren we dat de
         * één-op-veel-relatie ook vanuit
         * Reiziger correct wordt opgebouwd.
         */

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


        /*
         * ==========================================
         * 11. ALLE REIZIGERS
         * ==========================================
         *
         * Hiermee controleren we dat ook
         * findAll() van ReizigerDAO de
         * OVChipkaarten correct koppelt.
         */

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


        /*
         * ==========================================
         * 12. KAART 1 VERWIJDEREN
         * ==========================================
         */

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


        /*
         * ==========================================
         * 13. KAART 2 VERWIJDEREN
         * ==========================================
         */

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


        /*
         * ==========================================
         * 14. DELETE CONTROLEREN
         * ==========================================
         */

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
                        reiziger
                                .getOvChipkaarten()
                                .size()
        );


        /*
         * ==========================================
         * 15. TESTREIZIGER VERWIJDEREN
         * ==========================================
         */

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


        /*
         * ==========================================
         * 16. DELETE REIZIGER CONTROLEREN
         * ==========================================
         */

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


        /*
         * ==========================================
         * EINDE P4
         * ==========================================
         */

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