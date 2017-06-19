package multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MulticastSocketServer {
    
    final static String INET_ADDR = "224.0.0.3";
    final static int PORT = 8888;

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
    	// cogemos la direccion a la que vamos a conectar:
        InetAddress addr = InetAddress.getByName(INET_ADDR);
     
        // Abrimos un datagramSocket que sera lo que envie los datos:
        try (DatagramSocket serverSocket = new DatagramSocket()) {
            for (int i = 0; i < 100000; i++) {
                String msg = "Estas recibiendo el numero: " + i;

                // pasamos el paquete a binario, y lo mandamos como en los ejercicios anteriores
                DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),msg.getBytes().length, addr, PORT);
                serverSocket.send(msgPacket);
                //y sacamos el valor de lo que estamos mandando por la  pantalla del server:
                System.out.println("El server ha mandado un paquete con el mensaje: " + msg.toUpperCase());
                //y un pequeño threadSleep para que no lo haga todo corriendo:
                Thread.sleep(500);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}