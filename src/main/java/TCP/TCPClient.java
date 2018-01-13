package TCP;

import java.io.*;
import java.net.Socket;

public class TCPClient extends Thread {
    private String username;
    private String password;
    private String serviceName;

    public TCPClient(String u, String p) {
        this.username = u;
        this.password = p;
    }

    @Override
    public void run() {
        try(Socket servercon = new Socket( "localhost", 5020 );
            InputStream in = servercon.getInputStream();
            OutputStream out = servercon.getOutputStream();
            ObjectInputStream oin = new ObjectInputStream( in );
            ObjectOutputStream oout = new ObjectOutputStream( out )) {
            oout.writeUTF( this.username );
            oout.writeUTF( this.password );
            oout.flush();
            serviceName = oin.readUTF();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}