    package skola.byggarebob;

    import java.util.Scanner;


    /*  För att starta Projektet i terminalen:
    cd C:\Users\leon.larssonberntss\Documents\vscode\Java\boardgame
    javac -d target\classes src\main\java\skola\byggarebob\App.java
    java -cp target\classes skola.byggarebob.App
    */
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

            // Deklarering av variabler för spelet
            int[] extraTärningar = new int[spelarPositioner.length];
            boolean[] skyddsamulet = new boolean[spelarPositioner.length];
            boolean någonHarVunnit = false;
            boolean[] fryst = new boolean[spelarPositioner.length];
            int tärning;
            int count = 0;
            boolean gissatRätt = false;

            // Generera slumpmässiga positioner för mysterielådorna
            for (int i = 0; i < mysterielådor.length; i++) {
                mysterielådor[i] = (int) (Math.random() * (längdPåSpelplan - 1) + 1);
            }

            // Spelloopen
            do {
                for (int i = 0; i < spelarPositioner.length; i++) {
                    // Kontrollera om spelaren har en skyddsamulet
                    if (skyddsamulet[i]) {
                        System.out.println("Skyddsamulet aktiverad! Du är skyddad mot nästa negativa effekt.");
                        skyddsamulet[i] = false;
                    }
                    // Kontrollera om spelaren är fryst
                    else if (fryst[i]) {
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
                        System.out.println(" Din nya plats är: " + spelarPositioner[i]);
                        spelarPositioner[i] += extra;
                        extraTärningar[i]--;
                    }

                    // Visualisera spelplanen
                    visualiseraSpelplanen(spelarPositioner, längdPåSpelplan, mysterielådor);

                    // Kontrollera om spelaren landar på en mysterielåda
                    for (int j = 0; j < mysterielådor.length; j++) {
                        if (spelarPositioner[i] == mysterielådor[j]) {
                            // Generera en slumpmässig effekt
                            int effekt = (int) (Math.random() * 10);
                            switch (effekt) {
                                case 0 -> {
                                    System.out.println("Du fastnade i klister! Missa nästa runda!");
                                    fryst[i] = true;
                                }
                                case 1 -> {
                                    // Generera tre slumpmässiga tal och låt spelaren gissa vilket som är störst
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
                                    // Flytta spelaren till en slumpmässig plats på halva brädet
                                    System.out.println("Du hittade en magisk portal! Flytta till en slumpmässig plats på halva brädet!");
                                    spelarPositioner[i] = (int) (Math.random() * längdPåSpelplan + 1)/2;
                                }
                                case 3 -> {
                                    // Låt spelaren välja mellan två vägar med olika risk och belöning
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
                                    // Ge spelaren en extra tärning i nästa runda
                                    System.out.println("Du hittade en extra tärning! Du får slå en extra tärning i nästa runda.");
                                    extraTärningar[i]++;
                                }
                                case 5 -> {
                                    // Flytta spelaren tillbaka till sin tidigare position
                                    System.out.println("Du hittade en tidsmaskin! Flytta tillbaka " + tärning + " steg i tiden (till din tidigare position)!");
                                    spelarPositioner[i] -= tärning; // Flytta tillbaka till tidigare position
                                }
                                case 6 -> {
                                    // Byt plats med en slumpmässigt vald spelare
                                    int annanSpelare = (int) (Math.random() * spelarPositioner.length);
                                    while (annanSpelare == i) {
                                        annanSpelare = (int) (Math.random() * spelarPositioner.length);
                                    }
                                    System.out.println("Du hittade en teleport! Byt plats med spelare " + (annanSpelare + 1) + "!");
                                    int temp = spelarPositioner[i];
                                    spelarPositioner[i] = spelarPositioner[annanSpelare];
                                    spelarPositioner[annanSpelare] = temp;
                                }
                                case 7 -> {
                                    // Ge spelaren en skyddsamulet som skyddar mot nästa negativa effekt
                                    System.out.println("Du hittade en skyddsamulet! Den skyddar dig mot nästa negativa effekt du skulle få.");
                                    skyddsamulet[i] = true;
                                }
                                case 8 -> {
                                    // Frys spelaren i nästa runda
                                    System.out.println("Du snubblade på en rot! Du missar din nästa tur!");
                                    fryst[i] = true;
                                }
                                case 9 -> {
                                    // Låt spelaren gissa ett magiskt nummer mellan 1 och 100. Ju snabbare de gissar rätt, desto större belöning!
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
                                            System.out.println("Rätt gissat!");
                                            gissatRätt = true;
                                            continue;
                                        } else if (gissning < nummer) {
                                            System.out.println("För lågt! Försök igen.");
                                        } else {
                                            System.out.println("För högt! Försök igen.");
                                        }
                                    }
                                    if (gissatRätt && count == 1) {
                                        System.out.println("Du gissade rätt på första försöket! Flytta fram 10 steg!");
                                        spelarPositioner[i] += 30;
                                    } else if (gissatRätt && count <= 3) {
                                        System.out.println("Du gissade rätt! Flytta fram 5 steg!");
                                        spelarPositioner[i] += 10;
                                    }
                                    if (!gissatRätt) {
                                        System.out.println("Du gissade inte rätt på 5 försök! Flytta tillbaka 15 steg!");
                                        spelarPositioner[i] -= 15;
                                    }
                                    System.out.println("Din nya plats är: " + spelarPositioner[i]);
                                    break;
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
                        någonHarVunnit = true;
                        break;
                    }
                }
            } while (!någonHarVunnit);

            // Stäng scannern
            scan.close();
        }
        // Metod för att fråga användaren om antal spelare
        public static int antalSpelare(Scanner scan) {
            System.out.println("Hur många spelare?");
            return scan.nextInt();
        }
        // Metod för att visa startmenyn
        public static void startMeny(Scanner scan) {
            System.out.println("\n1. Starta spelet \n2. Regler \n3. Avsluta");
            switch (scan.nextInt()) {
                case 1 -> startaSpelet();
                case 2 -> regler(scan);
                case 3 -> {
                    System.out.println("Avslutar spelet...");
                    System.exit(0);
                }
                default -> {
                    System.out.println("Ogiltigt val, startar spelet...");
                    startaSpelet();
                }
            }
        }
        // Metod för att visa reglerna
        public static void regler(Scanner scan) {
            System.out.println("Regler: \n- Målet är att nå ruta \"n\" först! \n- Slå tärningen och flytta framåt det antal steg du slog. \n- Landar du på en mysterielåda händer något oväntat! \n- Om du landar på samma ruta som en annan spelare så backar den spelaren 1 ruta! \n- Första spelaren att nå eller passera sista rutan vinner! \nLycka till!");
            startMeny(scan);
        }
        // Metod för att starta spelet
        public static void startaSpelet() {
            System.out.println("Startar spelet...");
        }
        // Metod för att fråga användaren om längden på spelplanen
        public static int längdPåBrädet(Scanner scan) {
            System.out.println("Hur lång ska brädet vara? (Standard är 100)");
            return scan.nextInt();
        }
        // Metod för att fråga användaren om antal mysterielådor
        public static int antal(Scanner scan) {
            System.out.println("Hur många mysterielådor vill du ha? (Standard är 20% av totala rutor)");
            return scan.nextInt();
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
                    spelarPositioner[i]--;
                }
            }
        }
    }
