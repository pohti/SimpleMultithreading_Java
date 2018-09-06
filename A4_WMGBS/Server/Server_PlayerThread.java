package Server;

import Common.Message;
import PokemonGame.Pokemon_Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_PlayerThread extends Thread {
    private static final int DEFAULT_PORT = 4445;
    private Broadcaster broadcaster;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private GameServer gameServer;
    private Pokemon_Server pokemonServer;


    public Server_PlayerThread(GameServer gameServer, Broadcaster broadcaster){
        this.gameServer = gameServer;
        this.broadcaster = broadcaster;
    }//end of constructor

    public void run(){
        int port = DEFAULT_PORT;

        // setup server socket
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("Player thread started on port: " + port);
        } catch (IOException e){
            System.out.println("Failed to create server socket");
            System.exit(0);
        }

        // only accept one player socket
        try{
            clientSocket = serverSocket.accept();
            System.out.println("Connected to player");

            handleClient();
        } catch (IOException e){
            System.out.println("Problem accepting client socket");
        }
    }//end of run()

    // this is where WMGBS game takes place
    // a series of 'input' will be requested from the Player
    // the game shall be broadcasted back to Viewers using Broadcaster object
    private void handleClient() {
        pokemonServer = new Pokemon_Server(this);
        pokemonServer.run();
    }

    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    // utility
    // TODO: return statement
    public OutputStream getOutputStream(){
        OutputStream outputStream = null;
        try {
            outputStream = clientSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream;
    }
    public InputStream getInputStream(){
        InputStream inputStream = null;
        try {
            inputStream = clientSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return inputStream;
    }
    public void broadcast(String message){
        broadcaster.sendMessage(message);
    }
}//end of class Server_PlayerThread
