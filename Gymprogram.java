package Inlämningsuppgift2;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Gymprogram {

    public static void main(String[] args) {

        // filer för medlemmar och ptloggen
        String medlemsFil = "C:\\Users\\Simon\\IdeaProjects\\Sprint.2\\src\\Inlämningsuppgift2\\medlem.txt";
        String ptLoggFil = "PtLogg.txt";

        //datumformatet i medlemsfilen
        DateTimeFormatter datumFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate idag = LocalDate.now();

        //lista, information om medlemmar i en array av strängar
        List<String[]> medlemsLista = new ArrayList<>();

        // läser in medlemmar från textfilen
        try (BufferedReader läsare = new BufferedReader(new FileReader(medlemsFil))) {
            läsare.readLine();
            String rad;

            // delar upp raden på semikolon och arrayen läggs till i medlemslista
            while ((rad = läsare.readLine()) != null) {
                medlemsLista.add(rad.split(";"));
            }
        } catch (IOException e) {
            System.out.println("Fel när filen skulle läsas! " + e.getMessage());
            return;
        }

        // användare får skriva in namn eller personnummer
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ange namn eller personnummer (xxxxxx-xxxx): ");
        String personSökning = scanner.nextLine();

        // loopar igenom varje medlem
        for (String[] medlem : medlemsLista) {
            String namn = medlem[0].trim(); // namn
            String personnummer = medlem[3].trim(); // personnnummer
            String datumBetaltText = medlem[5].trim(); // senast betalning
            String medlemskap = medlem[6].trim(); // medlemskapstyp

            // checkar om inmatning matchar namn/personnummer
            if (namn.equalsIgnoreCase(personSökning) || personnummer.equals(personSökning)) {
                // vi match konverteras datum i texten till localdate
                LocalDate senasteBetalning = LocalDate.parse(datumBetaltText, datumFormat);

                // kontrollerar giltigt medlemskap
                if (senasteBetalning.plusYears(1).isAfter(idag)) {
                    System.out.println("Medlem: " + namn + " (" + medlemskap + ")");
                    sparaBesökTillLogg(ptLoggFil,  namn, personnummer, idag);
                } else {
                    // om medlemskapet gått ut
                    System.out.println("Före detta medlem: " + namn);
                }
                return;
            }
        }
        // om ingen match hittas
        System.out.println("Personen är ej medlem!");

    }

    // statiskt metod
    static void sparaBesökTillLogg(String loggFil, String namn, String personnummer, LocalDate datum) {
        // Skapar skrivobjekt och öppnar filen
        try (BufferedWriter skrivare = new BufferedWriter(new FileWriter(loggFil, true))) {
            // skapar en text till loggen
            skrivare.write(namn + " " + personnummer + ",checkade in " + datum + "\n");
        } catch (IOException e) {
            System.out.println("Fel uppstod vid uppdatering av filen!: " + e.getMessage());
        }
    }
}