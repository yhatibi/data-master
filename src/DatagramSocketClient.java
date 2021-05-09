import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Scanner;

/**
 * Created by jordi on 26/02/17.
 * Exemple Client UDP extret dels apunts del IOC i ampliat
 * El server és DatagramSocketServer
 *
 * Aquest client reb del server el mateix que se li envia
 * Si s'envia adeu s'acaba la connexió
 */

public class DatagramSocketClient {
    InetAddress serverIP;
    int serverPort;

    Socket socket;
    InputStream in;
    ObjectInputStream oiStream;

    OutputStream out;
    ObjectOutputStream ooStream;
    Scanner sc;

    String host;
    int port;

    Llista ret = null;
    String nombre;
    List<Integer> numberList;
    int numero = 0;

    Llista llista;

    public DatagramSocketClient() {
        sc = new Scanner(System.in);
    }

    public void init(String host, int port) throws SocketException, UnknownHostException {
        serverIP = InetAddress.getByName(host);
        serverPort = port;


        this.host = host;
        this.port = port;
    }

    public void runClient() throws IOException {

        do {
            numero = sc.nextInt();
            if (numero > 0) numberList.add(numero);
        } while (numero > 0);
        ret = new Llista(nombre, numberList);


        try {
            socket = new Socket(InetAddress.getByName(host), port);
            out = socket.getOutputStream();
            ooStream = new ObjectOutputStream(out);
            ooStream.writeObject(ret);

            in = socket.getInputStream();
            oiStream = new ObjectInputStream(in);
            ret = (Llista) oiStream.readObject();
            getDataToRequest(ret);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    //Resta de conversa que se li envia al server
    private void getDataToRequest(Llista serverData) {

        serverData.getNumberList().forEach(
                integer -> System.out.print(integer + ", ")
        );

    }

    //primer missatge que se li envia al server
    private byte[] getFirstRequest() {
        System.out.println("Entra un numero entre el 0 i el 10: ");

        int num = sc.nextInt();
        byte[] resposta = ByteBuffer.allocate(4).putInt(num).array(); //num és un int
        return resposta;
    }

    //Si se li diu adeu al server el client es desconnecta
    private boolean mustContinue(byte [] data) {
        String msg = new String(data).toLowerCase();
        return !msg.equals("adeu");
    }

    public static void main(String[] args) {
        DatagramSocketClient client = new DatagramSocketClient();
        try {
            client.init("localhost",5555);
            client.runClient();
        } catch (IOException e) {
            e.getStackTrace();
        }

    }

}