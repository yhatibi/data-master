import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.stream.Collectors;

/**
 * Created by jordi on 26/02/17.
 * Exemple Servidor UDP extret dels apunts IOC i ampliat
 * El seu CLient és DatagramSocketClient
 */
public class DatagramSocketServer {
    DatagramSocket socket;
    NombreSecret nombreSecret;

    Llista llista1;
    Llista llista2;
    InputStream in;
    ObjectInputStream oiStream;
    OutputStream out;
    ObjectOutputStream ooStream;

    boolean acabado;
    //Instàciar el socket
    public void init(int port) throws SocketException {
        socket = new DatagramSocket(port);

    }

    public void runServer() throws IOException {
        byte [] receivingData = new byte[4];
        byte [] sendingData;
        InetAddress clientIP;
        int clientPort;

        while(true) {
            try {
                llista1 = (Llista) oiStream.readObject();
                llista2 = processData(llista1);
                ooStream.writeObject(llista2);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                ooStream.writeObject(llista2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //El server retorna al client el mateix missatge que li arriba però en majúscules
    private Llista processData(Llista llista) {
        System.out.println(llista.getNom());
        Llista ret = null;

        if (llista.getNumberList().size() > 0 && llista != null) {
            ret = new Llista(llista.getNom(), llista.getNumberList().stream().sorted().distinct().collect(Collectors.toList()));
            acabado = true;
        }
        return ret;
    }

    public static void main(String[] args) {


        DatagramSocketServer server = new DatagramSocketServer();
        try {
            server.init(5555);
            server.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}