
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class Server {

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        java.rmi.registry.LocateRegistry.createRegistry(20077);
        System.setProperty("java.rmi.server.hostname", "dist.saluton.dk");
        GalgeLogikInterface galgelogik = new GalgeLogik();


        // Local host setup
     //   Naming.rebind("rmi://localhost:1099/print", impl);
        // System.setProperty("java.rmi.server.hostname", "localhost");

        Naming.rebind("rmi://dist.saluton.dk:20077/print", galgelogik);
        System.out.println("Print registreret på port: 20077");
    }
}
