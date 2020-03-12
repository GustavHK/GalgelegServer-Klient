import brugerautorisation.data.Bruger;
import brugerautorisation.transport.rmi.Brugeradmin;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;


public class GameController {
    GalgeLogikInterface spil;

    GameController(GalgeLogikInterface galgeLogikInterface){
        this.spil = galgeLogikInterface;
    }


    public void start() throws RemoteException, MalformedURLException, NotBoundException{


        Scanner scanner = new Scanner(System.in);
        Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");

        spil.nulstil();
        System.out.println(spil.getOrdet());
        System.out.println("Velkommen til galgespillet.");
        Boolean logIn = false;
        System.out.println("Log ind for at spille");
        while (!logIn) {
            Bruger bruger = null;
            System.out.println("Brugernavn: ");
            String brugernavn = scanner.next();
            System.out.println("Kode: ");
            String kode = scanner.next();

            try {
                bruger = ba.hentBruger(brugernavn, kode);
            } catch (IllegalArgumentException a) {
                System.out.println(a);
            }

            if (bruger != null) {
                logIn = true;
                System.out.println("Velkommen " + bruger.fornavn);
                System.out.println("Du er nu logget ind og spillet begynder!");
            }
        }

        while (!spil.erSpilletSlut()) {
            System.out.println("Skriv et bogstav: ");
            String bogstav = scanner.next();
            while (bogstav.length() != 1) {
                System.out.println("Kun skriv et tegn");
                bogstav = scanner.next();
            }

            spil.gætBogstav(bogstav);

            if (spil.erSidsteBogstavKorrekt()) {
                System.out.println("Korrekt");
                System.out.println(spil.getSynligtOrd());
                System.out.println("______________________________________ \n");
            } else {
                System.out.println("Forkert");
                System.out.println("Liv tilbage: " + (7 - spil.getAntalForkerteBogstaver()));
                System.out.println("______________________________________ \n");
            }
        }

        if (spil.erSpilletVundet()) {
            System.out.println("Du vandt spillet");
        } else if (spil.erSpilletTabt()) {
            System.out.println("Du tabte");
        }
    }















}
