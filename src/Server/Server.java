package Server;

import Klient.GameController;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class Server {

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        java.rmi.registry.LocateRegistry.createRegistry(20077);
       System.setProperty("java.rmi.server.hostname", "dist.saluton.dk");
    //    System.setProperty("java.rmi.server.hostname", "localhost");
        GalgeLogikInterface galgelogik = new GalgeLogik();

        // Local host setup
      //  Naming.rebind("rmi://localhost:1099/controller", controller);
        Naming.rebind("rmi://dist.saluton.dk:20077/logik", galgelogik);
        System.out.println("Galgeserver registreret på port: 20077");
    }
}
