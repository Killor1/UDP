package udp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
	public InetAddress serverIP;
	public int serverPort;
	public DatagramSocket socket;
	public BufferedReader buff;
	public String nom;
    public String nombre;
    public Tauler t;
    static boolean guanyat=false;
    public int intents;
    public Jugada jugada;
    
    public Client() throws IOException{
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
    
    public void runClient() throws IOException, ClassNotFoundException {
        byte [] receivedData = new byte[1024];
        byte [] sendingData;        
        
        DatagramPacket packet;
        
        while(guanyat!=true){
        	sendingData= sendJugada();
        	packet = new DatagramPacket(sendingData,sendingData.length,serverIP,serverPort);
            socket.send(packet);
            //creo un paquete con lo que recivo
            packet = new DatagramPacket(receivedData,1024);
            socket.receive(packet);
            //mira si ha ganado o si no y depende de que te imprimer por pantalla el mensaje que envia el servidor
            sendingData = getTauler(packet.getData(), packet.getLength());
        }        
    }
    
    private byte[] getTauler(byte[] data, int length) throws IOException, ClassNotFoundException {
    	
    	ByteArrayInputStream in = new ByteArrayInputStream(data);
    	ObjectInputStream ois = new ObjectInputStream(in);
    	t = (Tauler) ois.readObject();
        t.showTauler();
        if (t.missatge.equals("HAS ACERTADO")) {
			guanyat=true;
			System.out.print(t.missatge);
		}else{
			System.out.print(t.missatge);
		}
        return null;
    }
    
    private byte[] sendJugada() throws IOException {
    	
   	 System.out.println("Introduce un numero: ");
        nom = buff.readLine();
        intents = intents+1;
   	ByteArrayOutputStream bStream = new ByteArrayOutputStream();
   	ObjectOutput oo = new ObjectOutputStream(bStream); 
   	jugada= new Jugada(this.nombre, intents, nom);
   	oo.writeObject(jugada);
   	oo.close();

   	byte[] serializedMessage = bStream.toByteArray();
      
       return serializedMessage;
   }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
    	//creo objeto clietne
        Client client = new Client();
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