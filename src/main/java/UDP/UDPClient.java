package UDP;

import javafx.scene.control.Label;

import java.io.IOException;
import java.net.*;

public class UDPClient extends Thread {

    private Label label ;

    public UDPClient (Label lb){
        this.label = lb;
    }
    @Override
    public void run() {
        InetAddress ia = null;

        try{
            ia = InetAddress.getByName( "localHost" );
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
        try (DatagramSocket dSocket = new DatagramSocket( 5555 )) {

            try {
                String command = "{\"cmd\":\"time\"}";
                byte buffer[] = command.getBytes();

                DatagramPacket packet = new DatagramPacket( buffer,buffer.length , ia, 5000 );
                dSocket.send( packet );
                System.out.println(new String (packet.getData(),0,packet.getLength()));

                byte[] answer = new byte[1024];
                packet = new DatagramPacket( answer, answer.length );
                dSocket.receive( packet );
                String respond = new String( packet.getData(),0,packet.getLength() );
                this.label.setText( respond );
                System.out.println("The server responded with: " + respond);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
    }

}