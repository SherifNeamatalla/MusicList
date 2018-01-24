package UDP;

import javafx.application.Platform;
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
            ia = InetAddress.getByName( "localhost" );
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
        // Socket to connent to the Server
        try (DatagramSocket dSocket = new DatagramSocket(  )) {

            try {
                String command = "{\"cmd\":\"time\"}";
                byte buffer[] = command.getBytes();

                DatagramPacket packet = new DatagramPacket( buffer,buffer.length , ia, 5000 );
                // Send the packet to the Server to get the Duration
                dSocket.send( packet );
                //System.out.println("here "+new String (packet.getData(),0,packet.getLength()));

                byte[] answer = new byte[1024];
                packet = new DatagramPacket( answer, answer.length );
                dSocket.receive( packet );
                String respond = new String( packet.getData(),0,packet.getLength() );
                if(!respond.contains( "unknown" )) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                           label.setText( respond );
                        }
                    });

                }

                //System.out.println("The server responded with: " + respond);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
    }

}