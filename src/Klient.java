import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Klient {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {

        GalgeLogikInterface spil = (GalgeLogikInterface) Naming.lookup("rmi://dist.saluton.dk:20077/print");
        GameController controller = new GameController(spil);
        controller.start();
    }
}
