
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.*;

/**
 * Created by jordi on 26/02/17.
 * Exemple Client UDP extret dels apunts del IOC i ampliat
 * El server és DatagramSocketServer
 */
public class Cliente {
	public InetAddress serverIP;
	public int serverPort;
	public DatagramSocket socket;
	public BufferedReader buff;
	public String nom;
    public String nombre;
    public Tauler tauler;
    static boolean guanyat=false;
    public int intents;
    public Jugada jugada;

    //nada mas crear un objeto de tipo cliente creo el lector por pantalla
    public Cliente() throws IOException {
    	
        buff= new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Introduce el nombre del jugador: ");
        nombre= buff.readLine();
    }

    public void init(String host, int port) throws SocketException, UnknownHostException {
    	//le digo que puerto es y en que ip
        serverIP = InetAddress.getByName(host);
        serverPort = port;
        //creo el socket
        socket = new DatagramSocket();
        intents = 0;
    }

    public void runClient() throws IOException {
        byte [] receivedData = new byte[1024];
        byte [] sendingData;

        sendingData = getFirstRequest();
        DatagramPacket packet;
        //hata que guanya no sea true que siga
        while (guanyat!=true) {
        	//creo un paquete con los datoa que me introduce el usuario y lo envio al servidor
            packet = new DatagramPacket(sendingData,sendingData.length,serverIP,serverPort);
            socket.send(packet);
            //creo un paquete con lo que recivo
            packet = new DatagramPacket(receivedData,1024);
            socket.receive(packet);
            //mira si ha ganado o si no y depende de que te imprimer por pantalla el mensaje que envia el servidor
            sendingData = getDataToRequest(packet.getData(), packet.getLength());
            
        }
        String msg="Acertado";
        sendingData=msg.getBytes();
        packet = new DatagramPacket(sendingData,sendingData.length,serverIP,serverPort);
        socket.send(packet);

    }

    private byte[] getDataToRequest(byte[] data, int length) throws IOException {
        String rebut = new String(data,0,length);
        //Imprimeix el nom del client + el que es reb del server i demana més dades
        if (rebut.equals("HAS ACERTADO")) {
			guanyat=true;
			System.out.print(rebut);
		}else{
			System.out.print(rebut);
			 String msg = buff.readLine();
			 return msg.getBytes();
		}
        return null;
    }

    private byte[] getFirstRequest() throws IOException {
    	
    	 System.out.println("Introduce un numero: ");
         nom = buff.readLine();
         intents = intents+1;
    	ByteArrayOutputStream bStream = new ByteArrayOutputStream();
    	ObjectOutput oo = new ObjectOutputStream(bStream); 
    	oo.writeObject(new Jugada(this.nombre, intents, nom));
    	oo.close();

    	byte[] serializedMessage = bStream.toByteArray();
       
        return serializedMessage;
    }


    public static void main(String[] args) throws IOException {
    	//creo objeto clietne
        Cliente client = new Cliente();
        try {
        	//llamo al metodo init i le paso localhost y el puerto
            client.init("localhost",5555);
            //ejecutamos el programa
            client.runClient();
        } catch (IOException e) {
            e.getStackTrace();
        }

    }

}
