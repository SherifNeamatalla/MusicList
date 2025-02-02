package UDP;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer extends Thread{

    public void setMp(MediaPlayer mp) {
        this.mp = mp;
    }

    private MediaPlayer mp ;

    public UDPServer(MediaPlayer mediaPlayer) {
        this.mp = mediaPlayer;
    }

    @Override
    public void run() {
        // a Socket the connect to the client
        try(DatagramSocket socket = new DatagramSocket( 5000 )) {
            if (mp != null){
                while(true) {
                    DatagramPacket packet = new DatagramPacket( new byte[14],14 );
                    try {
                        // wait for a client to connect
                        socket.receive( packet );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();
                    int len = packet.getLength();
                    byte[] data = packet.getData();
                    //System.out.println("Request from " + address + " through port " + port + " with length " + len + "\n" + new String( data,0,data.length ));
                    String da = new String( data,0,data.length  );

                    try {
                        if(da.equals( "{\"cmd\":\"time\"}" )) {
                            Duration d = this.mp.getCurrentTime();
                            int minutes = (int)d.toMinutes();
                            int seconds = (int)d.toSeconds();
                            String answer = String.format( "%02d:%02d",minutes,seconds );
                            byte[] respond = answer.getBytes();
                            packet = new DatagramPacket( respond,respond.length,address,port );
                            //send the duration the Client
                            socket.send( packet );
                        } else {
                            byte[] respond = new String("Command unknown!").getBytes();
                            //System.out.println("Checking the respond in UDPServer: " + respond);
                            packet = new DatagramPacket( respond, respond.length, address, port );
                            socket.send( packet );
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }

    }
}