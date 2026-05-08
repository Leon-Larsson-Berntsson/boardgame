package skola.byggarebob;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) {
        // Skapa en scanner för användarinmatning
        Scanner scan = new Scanner(System.in);

        // Deklarering av variabler för spelet
        int längdPåSpelplan;
        int[] mysterielådor;
        int[] spelarPositioner;
        int[] extraTärningar;
        boolean[] skyddsamulet;
        boolean[] fryst;
        boolean någonHarVunnit;
        int tärning;
        int count;
        boolean gissatRätt;
        boolean debugMode = false;
        int nyPosition;
        boolean ärMysterielåda; 

        // Visa startmenyn
        System.out.print("Välkommen till det ultimata brädspelet! \nFörbered dig på en resa fylld av spänning, överraskningar och massor av skratt! \nSamla dina vänner och låt äventyret börja!");
        debugMode = startMeny(scan);

        // Fråga användaren om spelplanens längd, antal mysterielådor och antal spelare
        if (!debugMode) {
            längdPåSpelplan = längdPåBrädet(scan);
            mysterielådor = new int[antalLådor(scan, längdPåSpelplan)];
            spelarPositioner = new int[antalSpelare(scan)];
        } else {
            längdPåSpelplan = 100;
            mysterielådor = new int[20];
            spelarPositioner = new int[2];
        }

        // Deklarering av variabler för spelet
        extraTärningar = new int[spelarPositioner.length];
        skyddsamulet = new boolean[spelarPositioner.length];
        någonHarVunnit = false;
        fryst = new boolean[spelarPositioner.length];
        count = 0;
        gissatRätt = false;

        // Generera slumpmässiga positioner för mysterielådorna
        for (int i = 0; i < mysterielådor.length; i++) {
            mysterielådor[i] = (int) (Math.random() * (längdPåSpelplan - 1) + 1);

            do { 
                nyPosition = (int) (Math.random() * (längdPåSpelplan - 1) + 1);
                ärMysterielåda = false;

                // Kolla om positionen redan är en mysterielåda
                for (int j = 0; j < mysterielådor.length; j++) {
                    if (nyPosition == mysterielådor[j]) {
                        ärMysterielåda = true;
                        break;
                    }
                }
            } while (ärMysterielåda);
    
            mysterielådor[i] = nyPosition;
        }

        // Spelloopen
        do {
            for (int i = 0; i < spelarPositioner.length; i++) {
                // Tärningens utfall
                if (debugMode) {
                    System.out.println("Debug mode: Hoppa till ruta (1-" + längdPåSpelplan + "): ");
                    spelarPositioner[i] = scan.nextInt();
                    scan.nextLine();
                    tärning = 0;
                } else {
                    tärning = (int) (Math.random() * 6 + 1);
                }

                // Visualisera spelplanen
                visualiseraSpelplanen(spelarPositioner, längdPåSpelplan, mysterielådor);

                // Kontrollera om spelaren har en skyddsamulet
                if (skyddsamulet[i]) {
                    System.out.println("Skyddsamulet aktiverad! Du är skyddad mot nästa negativa effekt.");
                    skyddsamulet[i] = false;
                    pausa(1000);
                }
                // Kontrollera om spelaren är fryst
                else if (fryst[i]) {
                    System.out.println("Spelare " + (i + 1) + " är fryst och missar denna runda!");
                    fryst[i] = false;
                    pausa(1000);
                    continue; // Hoppa till nästa spelare
                }

                // Spelaren slår tärningen
                if (!debugMode) {
                    System.out.print("Spelare " + (i + 1) + ": Tryck enter för att slå tärningen!");
                    scan.nextLine();
                    spelarPositioner[i] += tärning;
                    System.out.println("Du slog en " + tärning + " Din nya plats är: " + spelarPositioner[i]);
                    pausa(1000);
                }

                // Kontrollera om spelaren har extra tärningar
                if (extraTärningar[i] > 0) {
                    int extra = (int) (Math.random() * 6 + 1);
                    System.out.println("Extra tärning! Du slog en " + extra);
                    System.out.println(" Din nya plats är: " + spelarPositioner[i]);
                    spelarPositioner[i] += extra;
                    extraTärningar[i]--;
                    pausa(1000);
                }

                // Kontrollera om spelaren landar på en mysterielåda
                for (int j = 0; j < mysterielådor.length; j++) {
                    if (spelarPositioner[i] == mysterielådor[j]) {
                        // Generera en slumpmässig effekt
                        int effekt;
                        if (debugMode) {
                            System.out.print("Debug mode: Ange effektens utfall (0-9): ");
                            effekt = scan.nextInt();
                            scan.nextLine();
                        } else {
                            effekt = (int) (Math.random() * 10);
                        }

                        switch (effekt) {
                            case 0 -> {
                                System.out.println("Du fastnade i klister! Missa nästa runda!");
                                fryst[i] = true;
                            }
                            case 1 -> {
                                int tal11 = (int) (Math.random() * 100 + 1);
                                int tal12 = (int) (Math.random() * 100 + 1);
                                int tal21 = (int) (Math.random() * 100 + 1);
                                int tal22 = (int) (Math.random() * 100 + 1);
                                int tal31 = (int) (Math.random() * 100 + 1);
                                int tal32 = (int) (Math.random() * 100 + 1);
                                int tal1 = tal11 * tal12;
                                int tal2 = tal21 * tal22;
                                int tal3 = tal31 * tal32;

                                System.out.println("Du hittade en magisk gåta! Vilket av följande tal är störst?\n1. " + tal11 + " * " + tal12 + "\n2. " + tal21 + " * " + tal22 + "\n3. " + tal31 + " * " + tal32 + "\nSkriv 1, 2 eller 3 för att gissa vilket tal som är störst!");
                                int gissning = scan.nextInt();
                                scan.nextLine();

                                if ((gissning == 1 && tal1 > tal2 && tal1 > tal3)
                                        || (gissning == 2 && tal2 > tal1 && tal2 > tal3)
                                        || (gissning == 3 && tal3 > tal1 && tal3 > tal2)) {
                                    System.out.println("Rätt gissat! Flytta fram 5 steg!");
                                    spelarPositioner[i] += 5;
                                } else {
                                    System.out.println("Fel gissat! Flytta tillbaka 5 steg!");
                                    spelarPositioner[i] -= 5;
                                    spelarPositioner[i] = klämmaPosition(spelarPositioner[i]);
                                }
                            }
                            case 2 -> {
                                System.out.println("Du hittade en magisk portal! Flytta till en slumpmässig plats på halva brädet!");
                                spelarPositioner[i] = (int) ((Math.random() * (längdPåSpelplan / 2)) + 1);
                            }
                            case 3 -> {
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
                                        spelarPositioner[i] = klämmaPosition(spelarPositioner[i]);
                                    }
                                } else {
                                    if (Math.random() > 0.5) {
                                        System.out.println("Säkra vägen lönade sig! +4 steg!");
                                        spelarPositioner[i] += 4;
                                    } else {
                                        System.out.println("Inte så säkert ändå... -2 steg!");
                                        spelarPositioner[i] -= 2;
                                        spelarPositioner[i] = klämmaPosition(spelarPositioner[i]);
                                    }
                                }
                            }
                            case 4 -> {
                                System.out.println("Du hittade en extra tärning! Du får slå en extra tärning i nästa runda.");
                                extraTärningar[i]++;
                            }
                            case 5 -> {
                                System.out.println("Du hittade en tidsmaskin! Flytta tillbaka " + tärning + " steg i tiden (till din tidigare position)!");
                                spelarPositioner[i] -= tärning;
                                spelarPositioner[i] = klämmaPosition(spelarPositioner[i]);
                                pausa(1000);
                            }
                            case 6 -> {
                                int annanSpelare = (int) (Math.random() * spelarPositioner.length);
                                while (annanSpelare == i) {
                                    annanSpelare = (int) (Math.random() * spelarPositioner.length);
                                }
                                System.out.println("Du hittade en teleport! Byt plats med spelare " + (annanSpelare + 1) + "!");
                                int temp = spelarPositioner[i];
                                spelarPositioner[i] = spelarPositioner[annanSpelare];
                                spelarPositioner[annanSpelare] = temp;
                                pausa(1000);
                            }
                            case 7 -> {
                                System.out.println("Du hittade en skyddsamulet! Den skyddar dig mot nästa negativa effekt du skulle få.");
                                skyddsamulet[i] = true;
                            }
                            case 8 -> {
                                System.out.println("Du snubblade på en rot! Du missar din nästa tur!");
                                fryst[i] = true;
                            }
                            case 9 -> {
                                int nummer = (int) (Math.random() * 100 + 1);
                                count = 0;
                                gissatRätt = false;
                                System.out.println("Gissa det magiska numret mellan 1 och 100! Du har 5 försök.");
                                for (int försök = 1; försök <= 5; försök++) {
                                    System.out.print("Försök " + försök + ": ");
                                    int gissning = scan.nextInt();
                                    count++;
                                    scan.nextLine();
                                    if (gissning == nummer) {
                                        pausa(1000);
                                        System.out.println("Rätt gissat!");
                                        gissatRätt = true;
                                        break;
                                    } else if (gissning < nummer) {
                                        System.out.println("För lågt! Försök igen.");
                                    } else {
                                        System.out.println("För högt! Försök igen.");
                                    }
                                }
                                if (gissatRätt && count == 1) {
                                    System.out.println("Du gissade rätt på första försöket! Flytta fram 30 steg!");
                                    spelarPositioner[i] += 30;
                                } else if (gissatRätt && count <= 3) {
                                    System.out.println("Du gissade rätt! Flytta fram 10 steg!");
                                    spelarPositioner[i] += 10;
                                }
                                if (!gissatRätt) {
                                    System.out.println("Du gissade inte rätt på 5 försök! Flytta tillbaka 15 steg!");
                                    spelarPositioner[i] -= 15;
                                }
                                System.out.println("Din nya plats är: " + spelarPositioner[i]);
                            }
                            default -> {
                                System.out.println("Du hittade en vanlig mysterielåda! Inget händer... eller gör det?");
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
                    pausa(1500); // Vänta lite innan spelet avslutas för att ge spelaren tid att läsa meddelandet
                    någonHarVunnit = true;
                    break;
                }
            }
        } while (!någonHarVunnit);

        System.out.println("Spelet är slut!");
        System.out.println("Stänger automatisk av programmet...");
        pausa(1000);
        // Stäng scannern
        scan.close();
    }

    // Metod för att fråga användaren om antal spelare
    public static int antalSpelare(Scanner scan) {
        System.out.println("Hur många spelare?");
        int antal = scan.nextInt();
        scan.nextLine();
        if (antal < 2) {
            System.out.println("För få spelare! Ange nytt antal spelare.");
            return antalSpelare(scan);
        }
        return antal;
    }

    // Metod för att visa startmenyn
    public static boolean startMeny(Scanner scan) {
        System.out.println("\n1. Starta spelet \n2. Regler \n3. Avsluta");
        int val = scan.nextInt();
        scan.nextLine();
        switch (val) {
            case 1 -> {
                startaSpelet();
                return false;
            }
            case 2 -> {
                return regler(scan);
            }
            case 3 -> {
                System.out.println("Avslutar spelet...");
                System.exit(0);
            }
            case 10 -> {
                System.out.println("Debug mode aktiverat! Visar debug-information...");
                return true;
            }
            default -> {
                System.out.println("Ogiltigt val, startar spelet...");
                startaSpelet();
                return false;
            }
        }
        return false;
    }

    // Metod för att visa reglerna
    public static boolean regler(Scanner scan) {
        System.out.println("Regler: \n- Målet är att nå ruta \"n\" först! \n- Slå tärningen och flytta framåt det antal steg du slog. \n- Landar du på en mysterielåda händer något oväntat! \n- Om du landar på samma ruta som en annan spelare så backar den spelaren 1 ruta! \n- Första spelaren att nå eller passera sista rutan vinner! \nLycka till!");
        return startMeny(scan);
    }

    // Metod för att starta spelet
    public static void startaSpelet() {
        System.out.println("Startar spelet...");
    }

    // Metod för att fråga användaren om längden på spelplanen
    public static int längdPåBrädet(Scanner scan) {
        System.out.println("Hur lång ska brädet vara? (Standard är 100)");
        int längd = scan.nextInt();
        scan.nextLine();
        if (längd < 20) {
            System.out.println("För kort bräde! Sätter längden på brädet till 100.");
            return 100;
        } else if (längd > 500) {
            System.out.println("För långt bräde! Sätter längden på brädet till 100.");
            return 100;
        }
        return längd;
    }

    // Metod för att fråga användaren om antal mysterielådor
    public static int antalLådor(Scanner scan, int längdPåSpelplan) {
        System.out.println("Hur många mysterielådor vill du ha? (Standard är 20% av totala rutor)");
        int antal = scan.nextInt();
        scan.nextLine();
        if (antal > längdPåSpelplan * 0.75) {
            System.out.println("För många mysterielådor! Sätter antal mysterielådor till 20% av totala rutor.");
            return (int) (längdPåSpelplan * 0.2);
        } else if (antal < 1) {
            System.out.println("För få mysterielådor! Sätter antal mysterielådor till 20% av totala rutor.");
            return (int) (längdPåSpelplan * 0.2);
        }
        return antal;
    }

    // Metod för att visualisera spelplanen
    public static void visualiseraSpelplanen(int[] spelarPositioner, int längdPåSpelplan, int[] mysterielådor) {
        for (int rad = 0; rad < längdPåSpelplan / 10; rad++) {
            for (int kol = 0; kol < 10; kol++) {
                int ruta;
                if (rad % 2 == 0) {
                    ruta = rad * 10 + kol + 1;
                } else {
                    ruta = rad * 10 + (9 - kol) + 1;
                }
                boolean ärMysterielåda = false;
                for (int j = 0; j < mysterielådor.length; j++) {
                    if (ruta == mysterielådor[j]) {
                        System.out.print("[M ] ");
                        ärMysterielåda = true;
                        break;
                    }
                }
                // Kontrollera om det är en spelare på rutan
                if (!ärMysterielåda) {
                    boolean ärSpelare = false;
                    // Om det är en spelare på rutan, skriv ut deras nummer istället för rutnumret
                    for (int k = 0; k < spelarPositioner.length; k++) {
                        if (ruta == spelarPositioner[k]) {
                            System.out.print("[P" + (k + 1) + "] ");
                            ärSpelare = true;
                            break;
                        }
                    }
                    if (!ärSpelare) {
                        System.out.printf("[%2d] ", ruta);
                    }
                }
            }
            System.out.println();
        }
    }

    // Metod för att kolla om spelare landar på samma ruta och i så fall backa den andra spelaren 1 steg
    public static void kollaOmSpelareSammaPlats(int[] spelarPositioner, int nuvarandeSpelarePlats) {
        for (int i = 0; i < spelarPositioner.length; i++) {
            if (i != nuvarandeSpelarePlats && spelarPositioner[i] == spelarPositioner[nuvarandeSpelarePlats]) {
                System.out.println("Du landade på samma ruta som spelare " + (i + 1) + "! De går tillbaka 1 steg!");
                spelarPositioner[i] = klämmaPosition(spelarPositioner[i]);
                pausa(1000);
                spelarPositioner[i]--;
            }
        }
    }

    public static void pausa(int milisekunder) {
        try {
            TimeUnit.MILLISECONDS.sleep(milisekunder);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    // Ser till så att spelaren's position inte går under 0 eller över längden på spelplanen
    public static int klämmaPosition(int position){
        return Math.max(0, position);
    }
}