package main.java.DAO;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import main.java.POJO.OVChipkaart;
import main.java.POJO.Reiziger;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class Main {

    private static final int TEST_REIZIGER_ID =
            100;

    private static final int TEST_KAART_ID_1 =
            123456;

    private static final int TEST_KAART_ID_2 =
            654321;


    public static void main(String[] args) {

        EntityManagerFactory emf =
                null;

        try {

            emf =
                    Persistence.createEntityManagerFactory(
                            "ovchip"
                    );


            ReizigerDAOHibernate reizigerDAO =
                    new ReizigerDAOHibernate(
                            emf
                    );

            OVChipkaartDAOHibernate ovChipkaartDAO =
                    new OVChipkaartDAOHibernate(
                            emf
                    );


            testP4H(
                    reizigerDAO,
                    ovChipkaartDAO
            );


        } catch (Exception e) {

            System.out.println(
                    "Er is een fout opgetreden tijdens de P4H-test."
            );

            e.printStackTrace();

        } finally {

            if (emf != null &&
                    emf.isOpen()) {

                emf.close();

                System.out.println(
                        "\nEntityManagerFactory gesloten."
                );
            }
        }
    }


    public static void testP4H(
            ReizigerDAO reizigerDAO,
            OVChipkaartDAO ovChipkaartDAO)
            throws Exception {

        System.out.println(
                "\n=========================================="
        );

        Reiziger bestaandeReiziger =
                reizigerDAO.findById(
                        TEST_REIZIGER_ID
                );


        if (bestaandeReiziger != null) {

            List<OVChipkaart> bestaandeKaarten =
                    ovChipkaartDAO.findByReiziger(
                            bestaandeReiziger
                    );


            for (OVChipkaart kaart :
                    bestaandeKaarten) {

                ovChipkaartDAO.delete(
                        kaart
                );
            }


            reizigerDAO.delete(
                    bestaandeReiziger
            );


            System.out.println(
                    "Oude P4H-testdata verwijderd."
            );

        } else {

            System.out.println(
                    "Geen oude P4H-testreiziger gevonden."
            );
        }


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
                "\n--- Nieuwe Reiziger ---"
        );

        System.out.println(
                reiziger
        );



        System.out.println(
                "\n--- Reiziger opslaan met Hibernate ---"
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
                    "P4H-test gestopt: " +
                            "Reiziger kon niet worden opgeslagen."
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
                        25.50
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
                        50.00
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
                "Kaart 1 toegevoegd: " +
                        kaart1Toegevoegd
        );

        System.out.println(
                "Kaart 2 toegevoegd: " +
                        kaart2Toegevoegd
        );


        System.out.println(
                "Aantal kaarten bij Reiziger: " +
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



        System.out.println(
                "\n--- OVChipkaarten opslaan met Hibernate ---"
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
                "Aantal gevonden kaarten: " +
                        kaartenVanReiziger.size()
        );



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
                "\n--- Reiziger opnieuw ophalen ---"
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
                    "Aantal OVChipkaarten vanuit Reiziger: " +
                            reizigerUitDatabase
                                    .getOvChipkaarten()
                                    .size()
            );


            for (OVChipkaart kaart :
                    reizigerUitDatabase
                            .getOvChipkaarten()) {

                System.out.println(
                        "  -> " +
                                kaart
                );
            }
        }


        System.out.println(
                "\n--- Bidirectionele relatie controleren ---"
        );


        if (reizigerUitDatabase != null) {

            for (OVChipkaart kaart :
                    reizigerUitDatabase
                            .getOvChipkaarten()) {

                boolean correct =
                        kaart.getReiziger() != null &&
                                kaart.getReiziger().getId() ==
                                        reizigerUitDatabase.getId();


                System.out.println(
                        "Kaart #" +
                                kaart.getKaart_nummer() +
                                " verwijst terug naar Reiziger #" +
                                reizigerUitDatabase.getId() +
                                ": " +
                                correct
                );
            }
        }



        System.out.println(
                "\n--- Alle Reizigers ---"
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
                "\n--- Verwijdering controleren ---"
        );


        List<OVChipkaart> kaartenNaDelete =
                ovChipkaartDAO.findByReiziger(
                        reiziger
                );


        System.out.println(
                "Aantal kaarten in database: " +
                        kaartenNaDelete.size()
        );


        System.out.println(
                "Aantal kaarten in Java-object: " +
                        reiziger
                                .getOvChipkaarten()
                                .size()
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
                "          EINDE P4H TEST"
        );

        System.out.println(
                "=========================================="
        );
    }
}