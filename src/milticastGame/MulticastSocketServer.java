package milticastGame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import udp.Jugada;
import udp.Tauler;

public class MulticastSocketServer {    
    final static String INET_ADDR = "224.0.0.3";
    final static int PORT = 8888;
    
    public  ArrayList<MulticastSocketClient> clientes;
	static int clients;
    DatagramSocket socket;
    ArrayList<Integer> ports;
    static int n;
    static String s;
    public Tauler tauler;
    public Jugada jugada;
    
    //Instàciar el socket
    public void init(int port) throws SocketException {
        socket = new DatagramSocket(port);
        clients=1;
        ports= new ArrayList<Integer>();
        tauler = new Tauler();
        clientes = new ArrayList<>();
    }

    public void runServer() throws IOException, ClassNotFoundException, InterruptedException {
        byte [] receivingData = new byte[1024];
        byte [] sendingData;
        InetAddress clientIP;
        int clientPort;
        
        while(clients!=0) {
        	//creo un paquete con la informacion que recibo del cliente
            DatagramPacket packet = new DatagramPacket(receivingData,1024);
            socket.receive(packet);
           
            sendingData = processData(packet.getData(),packet.getLength());
            
            //Llegim el port i l'adreça del client per on se li ha d'enviar la resposta
            clientIP = packet.getAddress();
            clientPort = packet.getPort();                  
          //  comprobarPort(clientPort);
            DatagramPacket send = new DatagramPacket(sendingData,sendingData.length, clientIP,clientPort);
            socket.send(send);
            
            this.sendMultiTaulers();
            
        }
    }
    
    private byte[] processData(byte[] data, int lenght) throws IOException, ClassNotFoundException {
    	
    	ByteArrayInputStream in = new ByteArrayInputStream(data);
    	ObjectInputStream ois = new ObjectInputStream(in);
    	jugada = (Jugada) ois.readObject();
       
        //Imprimir el missatge rebut i retornar-lo
        String mensaje = null;
		 Integer i = Integer.parseInt(jugada.jugada);
	        if (i>n) {
				mensaje="Es mas pequeño:";
			}else if (i==n) {
				mensaje="HAS ACERTADO";
			}else if (i<n) {
				mensaje="Es mas grande:";
			}
	    tauler.addClients(jugada.nom, jugada.intentos);
	    tauler.missatge=mensaje;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(tauler);
        byte[] missatge = os.toByteArray();
		        
        return missatge;
    }
    
    public void sendMultiTaulers() throws InterruptedException, IOException {
		InetAddress addr = InetAddress.getByName(INET_ADDR);
		DatagramSocket serverSocket = new DatagramSocket();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(tauler);
        byte[] missatge = os.toByteArray();

        DatagramPacket send = new DatagramPacket(missatge,missatge.length, addr, PORT);
        serverSocket.send(send);
	}
    
    public static void main(String[] args) throws ClassNotFoundException, InterruptedException {
    	//creo objeto servidor
    	MulticastSocketServer server = new MulticastSocketServer();
    	//numero rando entre 1-20
        n= (int) (Math.random()*20+1);
      
        try {
        	//inicio el servidor en el puerto 5555
            server.init(5555);
            //ejecuto el programa
            server.runServer();           
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}