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

    public String getServiceName() {
        return serviceName;
    }

    @Override
    public void run() {
        try(Socket servercon = new Socket( "localhost", 5020 );
            BufferedReader in = new BufferedReader(new InputStreamReader(servercon.getInputStream()));
            PrintWriter out = new PrintWriter(servercon.getOutputStream(), true)) {
            System.out.println("in TCPClient pushing username and password \n" + username + password);
            out.println(username);
            out.println(password);
            serviceName = in.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}