package Server;

import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class GalgeLogik extends UnicastRemoteObject implements GalgeLogikInterface {
         // AHT afprøvning er muligeOrd synlig på pakkeniveau
        ArrayList<String> muligeOrd = new ArrayList<String>();
        private String ordet;
        private ArrayList<String> brugteBogstaver = new ArrayList<String>();
        private String synligtOrd;
        private int antalForkerteBogstaver;
        private boolean sidsteBogstavVarKorrekt;
        private boolean spilletErVundet;
        private boolean spilletErTabt;

        public GalgeLogik() throws java.rmi.RemoteException {
            muligeOrd.add("bil");
            muligeOrd.add("computer");
            muligeOrd.add("programmering");
            muligeOrd.add("motorvej");
            muligeOrd.add("busrute");
            muligeOrd.add("gangsti");
            muligeOrd.add("skovsnegl");
            muligeOrd.add("solsort");
            muligeOrd.add("nitten");
            nulstil();
        }

        public ArrayList<String> getBrugteBogstaver() {
            return brugteBogstaver;
        }

        public String getSynligtOrd() {
            return synligtOrd;
        }

        public String getOrdet() {
            return ordet;
        }

        public int getAntalForkerteBogstaver() {
            return antalForkerteBogstaver;
        }

        public boolean erSidsteBogstavKorrekt() {
            return sidsteBogstavVarKorrekt;
        }

        public boolean erSpilletVundet() {
            return spilletErVundet;
        }

        public boolean erSpilletTabt() {
            return spilletErTabt;
        }

        public boolean erSpilletSlut() {
            return spilletErTabt || spilletErVundet;
        }

        public void nulstil() {
            brugteBogstaver.clear();
            antalForkerteBogstaver = 0;
            spilletErVundet = false;
            spilletErTabt = false;
            if (muligeOrd.isEmpty()) throw new IllegalStateException("Listen over ord er tom!");
            ordet = muligeOrd.get(new Random().nextInt(muligeOrd.size()));
            opdaterSynligtOrd();
        }

        @Override
        public void opdaterSynligtOrd() {
            synligtOrd = "";
            spilletErVundet = true;
            for (int n = 0; n < ordet.length(); n++) {
                String bogstav = ordet.substring(n, n + 1);
                if (brugteBogstaver.contains(bogstav)) {
                    synligtOrd = synligtOrd + bogstav;
                } else {
                    synligtOrd = synligtOrd + "*";
                    spilletErVundet = false;
                }
            }
        }

        public void gætBogstav(String bogstav) {
            if (bogstav.length() != 1) return;
            System.out.println("Der gættes på bogstavet: " + bogstav);
            if (brugteBogstaver.contains(bogstav)) return;
            if (spilletErVundet || spilletErTabt) return;

            brugteBogstaver.add(bogstav);

            if (ordet.contains(bogstav)) {
                sidsteBogstavVarKorrekt = true;
                System.out.println("Bogstavet var korrekt: " + bogstav);
            } else {
                // Vi gættede på et bogstav der ikke var i ordet.
                sidsteBogstavVarKorrekt = false;
                System.out.println("Bogstavet var IKKE korrekt: " + bogstav);
                antalForkerteBogstaver = antalForkerteBogstaver + 1;
                if (antalForkerteBogstaver > 6) {
                    spilletErTabt = true;
                }
            }
            opdaterSynligtOrd();
        }

        public void logStatus() {
            System.out.println("---------- ");
            System.out.println("- ordet (skult) = " + ordet);
            System.out.println("- synligtOrd = " + synligtOrd);
            System.out.println("- forkerteBogstaver = " + antalForkerteBogstaver);
            System.out.println("- brugeBogstaver = " + brugteBogstaver);
            if (spilletErTabt) System.out.println("- SPILLET ER TABT");
            if (spilletErVundet) System.out.println("- SPILLET ER VUNDET");
            System.out.println("---------- ");
        }


    public void hentOrdFraDr() throws Exception {
        // Henter ord med dr's rest API
        String URL = new String("http://www.dr.dk/mu-online/api/1.3/list/view/news?limit=10&offset=0");
        JSONObject object = Unirest.get(URL).asJson().getBody().getObject();
        JSONArray array = object.getJSONArray("Items");
        StringBuilder sb =new StringBuilder();
        for (int i = 0; i < array.length(); i++) {
            sb.append(array.getJSONObject(i).get("Title"));
            }
        String[] ord = sb.toString().split(" ");
        List<String> rigtigeOrd = new ArrayList<String>();
        for (String s : ord) {
            if (s.length() > 3 && s.length() < 15 && !s.contains(":")) {
                rigtigeOrd.add(s.toLowerCase());
            }
        }
        muligeOrd.clear();
        muligeOrd.addAll(rigtigeOrd);
        System.out.println("muligeOrd = " + muligeOrd);
        nulstil();
    }


}
