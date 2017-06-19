
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

/**
 * Created by jordi on 26/02/17.
 * Exemple Servidor UDP extret dels apunts IOC i ampliat
 * El seu CLient és DatagramSocketClient
 */
public class Servidor {
	public  ArrayList<Cliente> clientes;
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

    public void runServer() throws IOException, ClassNotFoundException {
        byte [] receivingData = new byte[1024];
        byte [] sendingData;
        InetAddress clientIP;
        int clientPort;
        
        while(clients!=0) {
        	
        	
        	
        	//creo un paquete con la informacion que recibo del cliente
            DatagramPacket packet = new DatagramPacket(receivingData,1024);
            socket.receive(packet);
           
            sendingData = processData(packet.getData(),packet.getLength());
            ByteArrayInputStream in = new ByteArrayInputStream(sendingData);
        	ObjectInputStream ois = new ObjectInputStream(in);
        	jugada = (Jugada) ois.readObject();
            //Llegim el port i l'adreça del client per on se li ha d'enviar la resposta
            clientIP = packet.getAddress();
            clientPort = packet.getPort();
            tauler.addClients(jugada.nom, jugada.intentos);            
            comprobarPort(clientPort);
           
            
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(tauler);
            byte[] missatge = os.toByteArray();
            //creo un paquete con el mensaje que he de enviar al cliente
            packet = new DatagramPacket(missatge,missatge.length,clientIP,clientPort);
            System.out.println("devuelto el tablero.");
            socket.send(packet);
        }
        System.out.println("Todos los jugadores han ganado");
    }
    
    public void comprobarPort(int clientPort){
    	boolean trobat=false;
    	if (ports.isEmpty()) {
			ports.add(clientPort);
		}else{
			for (Integer integer : ports) {
				if (integer==clientPort) {
					trobat=true;
				}
			}
			if (trobat==false) {
				ports.add(clientPort);
				clients+=1;
			}
		}
    }

    private byte[] processData(byte[] data, int lenght) {
        String msg = new String(data,0,lenght);
        //Imprimir el missatge rebut i retornar-lo
        String mensaje = null;
        if (msg.equals("Acertado")) {
			clients-=1;
			mensaje="";
		}else{
			 Integer i = Integer.parseInt(msg);
		        if (i>n) {
					mensaje="Es mas pequeño:";
				}else if (i==n) {
					mensaje="HAS ACERTADO";
				}else if (i<n) {
					mensaje="Es mas grande:";
				}
		}        
        return mensaje.getBytes();
    }

    public static void main(String[] args) throws ClassNotFoundException {
    	//creo objeto servidor
    	Servidor server = new Servidor();
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
