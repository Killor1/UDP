package multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class MulticastSocketClient {
    //damos la direccion en uno de los rangos validos:
    final static String INET_ADDR = "224.0.0.3";
    final static int PORT = 8888;

    public static void main(String[] args) throws UnknownHostException {
        // cogemos la direccion a la que vamos a conectar:
        InetAddress address = InetAddress.getByName(INET_ADDR);
        
        // creamos un espacio en memoria para alojar nuestro mensaje
        byte[] buf = new byte[256];
        
        // y creamos un multicastsocket, que seran donde se uniran los diferentes clients
        try (MulticastSocket clientSocket = new MulticastSocket(PORT)){
            //añadimos al cliente al grupo
            clientSocket.joinGroup(address);
     
            while (true) {
                // recibimos la info y la sacamos por pantalla
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                clientSocket.receive(msgPacket);

                String msg = new String(buf, 0, buf.length);
                System.out.println("Mensaje recibido del servidor: " + msg);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

