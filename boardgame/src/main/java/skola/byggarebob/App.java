    package skola.byggarebob;

    import java.util.Scanner;

    public class App {
        public static void main(String[] args) {
            // Skapa en scanner för användarinmatning
            Scanner scan = new Scanner(System.in);

            // Visa startmenyn
            System.out.print("Välkommen till det ultimata brädspelet! \nFörbered dig på en resa fylld av spänning, överraskningar och massor av skratt! \nSamla dina vänner och låt äventyret börja!");
            startMeny(scan);
            
            // Fråga användaren om spelplanens längd, antal mysterielådor och antal spelare
            int längdPåSpelplan = längdPåBrädet(scan);
            int[] mysterielådor = new int[antal(scan)];
            int[] spelarPositioner = new int[antalSpelare(scan)];
            int[] extraTärningar = new int[spelarPositioner.length];
            boolean någonHarVunnit = false;
            boolean[] fryst = new boolean[spelarPositioner.length];
            int tärning;

            // Generera slumpmässiga positioner för mysterielådorna
            for (int i = 0; i < mysterielådor.length; i++) {
                mysterielådor[i] = (int) (Math.random() * (längdPåSpelplan - 1) + 1);
            }

            // Spelloopen
            do {
                for (int i = 0; i < spelarPositioner.length; i++) {
                    // Kontrollera om spelaren är fryst
                    if (fryst[i]) {
                        System.out.println("Spelare " + (i + 1) + " är fryst och missar denna runda!");
                        fryst[i] = false;
                        continue; // Hoppa till nästa spelare
                    }

                    // Spelaren slår tärningen
                    System.out.print("Spelare " + (i + 1) + ": Tryck enter för att slå tärningen!");
                    scan.nextLine();
                    tärning = (int) (Math.random() * 6 + 1);
                    spelarPositioner[i] += tärning;
                    System.out.println("Du slog en " + tärning + " Din nya plats är: " + spelarPositioner[i]);

                    // Kontrollera om spelaren har extra tärningar
                    if (extraTärningar[i] > 0) {
                        int extra = (int) (Math.random() * 6 + 1);
                        System.out.println("Extra tärning! Du slog en " + extra);
                        spelarPositioner[i] += extra;
                        extraTärningar[i]--;
                    }

                    // Visualisera spelplanen
                    visualiseraSpelplanen(spelarPositioner, längdPåSpelplan, mysterielådor);

                    // Kontrollera om spelaren landar på en mysterielåda
                    for (int j = 0; j < mysterielådor.length; j++) {
                        if (spelarPositioner[i] == mysterielådor[j]) {
                            // Generera en slumpmässig effekt
                            int effekt = (int) (Math.random() * 5);
                            switch (effekt) {
                                case 0 -> {
                                    // Spelaren fastnar och missar nästa runda
                                    System.out.println("Du fastnade i klister! Missa nästa runda!");
                                    fryst[i] = true;
                                }
                                case 1 -> {
                                    // Matteutmaning där spelaren gissar vilket tal som är störst
                                    int tal1 = (int) (Math.random() * 100 + 1) * (int) (Math.random() * 100 + 1);
                                    int tal2 = (int) (Math.random() * 100 + 1) * (int) (Math.random() * 100 + 1);
                                    int tal3 = (int) (Math.random() * 100 + 1) * (int) (Math.random() * 100 + 1);

                                    System.out.println("Vilket tal är störst? \n1. " + tal1 + "\n2. " + tal2 + "\n3. " + tal3);
                                    int gissning = scan.nextInt();
                                    scan.nextLine();

                                    if ((gissning == 1 && tal1 > tal2 && tal1 > tal3) ||
                                            (gissning == 2 && tal2 > tal1 && tal2 > tal3) ||
                                            (gissning == 3 && tal3 > tal1 && tal3 > tal2)) {
                                        System.out.println("Rätt gissat! Flytta fram 5 steg!");
                                        spelarPositioner[i] += 5;
                                    } else {
                                        System.out.println("Fel gissat! Flytta tillbaka 5 steg!");
                                        spelarPositioner[i] -= 5;
                                    }
                                }
                                case 2 -> {
                                    // Spelaren flyttas till en slumpmässig plats
                                    System.out.println("Du hittade en magisk portal! Flytta till en slumpmässig plats på brädet!");
                                    spelarPositioner[i] = (int) (Math.random() * längdPåSpelplan + 1);
                                }
                                case 3 -> {
                                    // Spelaren väljer mellan två vägar
                                    System.out.println("Du kommer till ett vägskäl! Vilken väg väljer du?\n1. Den mörka skogen (hög risk, hög belöning)\n2. Den säkra vägen (låg risk, låg belöning)");
                                    int vägVal = scan.nextInt();
                                    scan.nextLine();
                                    if (vägVal == 1) {
                                        if (Math.random() > 0.5) {
                                            System.out.println("Du klarade skogen! +10 steg!");
                                            spelarPositioner[i] += 10;
                                        } else {
                                            System.out.println("Du gick vilse i skogen! -8 steg!");
                                            spelarPositioner[i] -= 8;
                                        }
                                    } else {
                                        if (Math.random() > 0.5) {
                                            System.out.println("Säkra vägen lönade sig! +4 steg!");
                                            spelarPositioner[i] += 4;
                                        } else {
                                            System.out.println("Inte så säkert ändå... -2 steg!");
                                            spelarPositioner[i] -= 2;
                                        }
                                    }
                                }
                                case 4 -> {
                                    // Spelaren får en extra tärning
                                    System.out.println("Du hittade en extra tärning! Du får slå en extra tärning i nästa runda.");
                                    extraTärningar[i]++;
                                }
                            }
                            System.out.println("Din nya plats är: " + spelarPositioner[i]);
                            break;
                        }
                    }

                    // Kontrollera om spelaren landar på samma ruta som en annan spelare
                    kollaOmSpelareSammaPlats(spelarPositioner, i);

                    // Kontrollera om någon har vunnit
                    if (spelarPositioner[i] >= längdPåSpelplan) {
                        System.out.println("GRATTIS SPELARE " + (i + 1) + "! Du vann spelet.");
                        någonHarVunnit = true;
                        break;
                    }
                }
            } while (!någonHarVunnit);

            // Stäng scannern
            scan.close();
        }

        // Fråga hur många spelare det är
        public static int antalSpelare(Scanner scan) {
            System.out.println("Hur många spelare?");
            return scan.nextInt();
        }

        // Startmeny
        public static void startMeny(Scanner scan) {
            System.out.println("\n1. Starta spelet \n2. Regler \n3. Avsluta");
            switch (scan.nextInt()) {
                case 1:
                    startaSpelet();
                    break;
                case 2:
                    regler(scan);
                    break;
                case 3:
                    System.out.println("Avslutar spelet...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Ogiltigt val, startar spelet...");
                    startaSpelet();
                    break;
            }
        }

        // Visa regler
        public static void regler(Scanner scan) {
            System.out.println("Regler: \n- Målet är att nå ruta \"n\" först! \n- Slå tärningen och flytta framåt det antal steg du slog. \n- Landar du på en mysterielåda händer något oväntat! \n- Om du landar på samma ruta som en annan spelare så backar den spelaren 1 ruta! \n- Första spelaren att nå eller passera sista rutan vinner! \nLycka till!");
            startMeny(scan);
        }

        // Starta spelet
        public static void startaSpelet() {
            System.out.println("Startar spelet...");
        }

        // Fråga hur lång spelplanen ska vara
        public static int längdPåBrädet(Scanner scan) {
            System.out.println("Hur lång ska brädet vara? (Standard är 100)");
            return scan.nextInt();
        }

        // Fråga hur många mysterielådor det ska vara
        public static int antal(Scanner scan) {
            System.out.println("Hur många mysterielådor vill du ha? (Standard är 20% av totala rutor)");
            return scan.nextInt();
        }

        // Visualisera spelplanen
        public static void visualiseraSpelplanen(int[] spelarPositioner, int längdPåSpelplan, int[] mysterielådor) {
            for (int rad = 0; rad < längdPåSpelplan / 10; rad++) {
                for (int kol = 0; kol < 10; kol++) {
                    int ruta;
                    if (rad % 2 == 0) {
                        ruta = rad * 10 + kol + 1; // vänster → höger
                    } else {
                        ruta = rad * 10 + (9 - kol) + 1; // höger → vänster
                    }

                    // Kontrollera mysterielåda, spelare, annars visa ruta
                    boolean ärMysterielåda = false;
                    for (int j = 0; j < mysterielådor.length; j++) {
                        if (ruta == mysterielådor[j]) {
                            System.out.print("[M ] ");
                            ärMysterielåda = true;
                            break;
                        }
                    }
                    if (!ärMysterielåda) {
                        boolean ärSpelare = false;
                        for (int k = 0; k < spelarPositioner.length; k++) {
                            if (ruta == spelarPositioner[k]) {
                                System.out.print("[P" + (k + 1) + "] ");
                                ärSpelare = true;
                                break;
                            }
                        }
                        if (!ärSpelare) {
                            System.out.printf("[%2d] ", ruta); // %2d = alltid minst 2 siffror
                        }
                    }
                }
                System.out.println();
            }
        }

        // Kontrollera om spelare landar på samma ruta
        public static void kollaOmSpelareSammaPlats(int[] spelarPositioner, int nuvarandeSpelarePlats) {
            for (int i = 0; i < spelarPositioner.length; i++) {
                if(i != nuvarandeSpelarePlats && spelarPositioner[i] == spelarPositioner[nuvarandeSpelarePlats]) {
                System.out.println("Du landade på samma ruta som spelare " + (i + 1) + "! De går tillbaka 1 steg!");
                spelarPositioner[i]--;
            }
        }
    }
}
