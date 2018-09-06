package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server_ViewerThread extends Thread{

    private static final int DEFAULT_PORT = 4444;
    private Broadcaster broadcaster;
    private boolean state;
    private ServerSocket serverSocket;

    public Server_ViewerThread(Broadcaster broadcaster){
        this.broadcaster = broadcaster;
        state = true;
    } // end of constructor

    public void run(){
        int port = DEFAULT_PORT;

        // setup serverSocket
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Viewer thread started on port: " + port);
        } catch (IOException e) {
            System.out.println("Failed to create server socket");
            System.exit(0);
        }

        // listen to client connections
        while(state){
            Socket clientSocket = null;

            try{
                clientSocket = serverSocket.accept();
                ClientHandler client = new ClientHandler(clientSocket);
                broadcaster.addClient(client);
                System.out.println("Ding! new viewer!");
            } catch (SocketException e){
                // closing the serverSocket will cause this exception to be thrown
                state = false;
                broadcaster.removeAllClients();
            } catch (IOException e){
                System.out.println("Problem accepting client socket");
            }

        }

        end();
        System.out.println("Server_ViewerThread ends");
    } // end of run() method

    public void end(){
        try{
            serverSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}// end of class Server_ViewerThread
