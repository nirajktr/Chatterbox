import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SocketServer {
    ServerSocket server; // Server socket to accept client connections
    Socket sk; // Socket for individual client connection
    InetAddress addr; // Server address
    
    ArrayList<ServerThread> list = new ArrayList<ServerThread>(); // List to keep track of connected clients

    public SocketServer() {
        try {
            addr = InetAddress.getByName("127.0.0.1"); // Localhost address
            // addr = InetAddress.getByName("192.168.43.1"); // Example for a different address
            
            server = new ServerSocket(1234, 50, addr); // Create server socket on port 1234
            System.out.println("\n Waiting for Client connection");
            
            while (true) {
                sk = server.accept(); // Accept client connection
                System.out.println(sk.getInetAddress() + " connected");

                // Create and start a new thread for the connected client
                ServerThread st = new ServerThread(this);
                addThread(st);
                st.start();
            }
        } catch (IOException e) {
            System.out.println(e + " -> ServerSocket failed");
        }
    }

    // Add a client thread to the list
    public void addThread(ServerThread st) {
        list.add(st);
    }

    // Remove a client thread from the list
    public void removeThread(ServerThread st) {
        list.remove(st);
    }

    public void broadCast(String message){
        for(ServerThread st : list){
            st.pw.println(message);
        }
    }

    public static void main(String[] args) {
        new SocketServer();
    }
}

class ServerThread extends Thread {
    SocketServer server;
    PrintWriter pw;
    String name;

    public ServerThread(SocketServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            // read
            BufferedReader br = new BufferedReader(new InputStreamReader(server.sk.getInputStream()));

            // writing
            pw = new PrintWriter(server.sk.getOutputStream(), true);
            name = br.readLine();
            server.broadCast("**["+name+"] Entered**");

            String data;
            while((data = br.readLine()) != null ){
                if(data == "/list"){
                    pw.println("a");
                }
                server.broadCast("["+name+"] "+ data);
            }
        } catch (Exception e) {
            //Remove the current thread from the ArrayList.
            server.removeThread(this);
            server.broadCast("**["+name+"] Left**");
            System.out.println(server.sk.getInetAddress()+" - ["+name+"] Exit");
            System.out.println(e + "---->");
        }
    }
}