package main.java.DAO;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import main.java.POJO.Adres;
import main.java.POJO.Reiziger;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args)
            throws SQLException {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory(
                        "ovchip"
                );

        try {

            ReizigerDAOHibernate reizigerDAO =
                    new ReizigerDAOHibernate(
                            emf
                    );

            AdresDAOHibernate adresDAO =
                    new AdresDAOHibernate(
                            emf
                    );

            testAdresDAO(
                    adresDAO,
                    reizigerDAO
            );

        } finally {

            emf.close();
        }
    }

    public static void testAdresDAO(
            AdresDAO adresDAO,
            ReizigerDAO reizigerDAO)
            throws SQLException {

        System.out.println(
                "\n---------- Test AdresDAO Hibernate -------------"
        );


        Adres bestaandAdres =
                adresDAO.findById(
                        9999
                );

        if (bestaandAdres != null) {

            adresDAO.delete(
                    bestaandAdres
            );
        }

        Reiziger bestaandeReiziger =
                reizigerDAO.findById(
                        9999
                );

        if (bestaandeReiziger != null) {

            reizigerDAO.delete(
                    bestaandeReiziger
            );
        }


        Reiziger reiziger =
                new Reiziger(
                        9999,
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
                        reiziger
                );

        System.out.println(
                "Reiziger opgeslagen: " +
                        reizigerOpgeslagen
        );

        Adres adres =
                new Adres(
                        9999,
                        "3511 LX",
                        "37",
                        "Straatnaam 1",
                        "Utrecht",
                        reiziger
                );

        reiziger.setAdres(
                adres
        );

        adres.setReiziger(
                reiziger
        );


        System.out.println(
                "\n--- Adres opslaan ---"
        );

        boolean adresOpgeslagen =
                adresDAO.save(
                        adres
                );

        System.out.println(
                "Adres opgeslagen: " +
                        adresOpgeslagen
        );


        System.out.println(
                "\n--- Adres ophalen op ID ---"
        );

        Adres gevondenAdresOpId =
                adresDAO.findById(
                        9999
                );

        System.out.println(
                "Opgehaald adres: " +
                        gevondenAdresOpId
        );


        System.out.println(
                "\n--- Adres ophalen via Reiziger ---"
        );

        Adres gevondenAdres =
                adresDAO.findByReiziger(
                        reiziger
                );

        System.out.println(
                "Opgehaald adres: " +
                        gevondenAdres
        );


        System.out.println(
                "\n--- Adres wijzigen ---"
        );

        adres.setPostcode(
                "3521 AL"
        );

        adres.setHuisnummer(
                "6A"
        );

        adres.setStraat(
                "Nieuwe Straat"
        );

        boolean adresGewijzigd =
                adresDAO.update(
                        adres
                );

        System.out.println(
                "Adres gewijzigd: " +
                        adresGewijzigd
        );

        Adres gewijzigdAdres =
                adresDAO.findById(
                        9999
                );

        System.out.println(
                "Gewijzigd adres: " +
                        gewijzigdAdres
        );


        System.out.println(
                "\n--- Reiziger wijzigen ---"
        );

        reiziger.setVoorletters(
                "G.A."
        );

        boolean reizigerGewijzigd =
                reizigerDAO.update(
                        reiziger
                );

        System.out.println(
                "Reiziger gewijzigd: " +
                        reizigerGewijzigd
        );


        System.out.println(
                "\n--- Reiziger ophalen op ID ---"
        );

        Reiziger gevondenReiziger =
                reizigerDAO.findById(
                        9999
                );

        System.out.println(
                "Opgehaalde reiziger: " +
                        gevondenReiziger
        );


        System.out.println(
                "\n--- Alle adressen ---"
        );

        List<Adres> adressen =
                adresDAO.findAll();

        for (Adres a : adressen) {

            System.out.println(
                    a
            );
        }


        System.out.println(
                "\n--- Alle reizigers ---"
        );

        List<Reiziger> reizigers =
                reizigerDAO.findAll();

        for (Reiziger r : reizigers) {

            System.out.println(
                    r
            );
        }



        System.out.println(
                "\n--- Reizigers zoeken op geboortedatum ---"
        );

        List<Reiziger> gevondenReizigers =
                reizigerDAO.findByGbdatum(
                        "2002-09-17"
                );

        for (Reiziger r : gevondenReizigers) {

            System.out.println(
                    r
            );
        }


        System.out.println(
                "\n--- Adres verwijderen ---"
        );

        boolean adresVerwijderd =
                adresDAO.delete(
                        adres
                );

        System.out.println(
                "Adres verwijderd: " +
                        adresVerwijderd
        );

        if (adresVerwijderd) {

            reiziger.setAdres(
                    null
            );

            adres.setReiziger(
                    null
            );
        }



        System.out.println(
                "\n--- Reiziger verwijderen ---"
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
                "\n--- Controle na verwijderen ---"
        );

        System.out.println(
                "Adres 9999: " +
                        adresDAO.findById(
                                9999
                        )
        );

        System.out.println(
                "Reiziger 9999: " +
                        reizigerDAO.findById(
                                9999
                        )
        );

        System.out.println(
                "\n---------- Einde Test AdresDAO Hibernate -------------"
        );
    }
}
