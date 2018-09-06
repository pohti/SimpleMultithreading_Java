package Client;


import PokemonGame.Pokemon_Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Client_Player {
    Client_Connection connection;
    Pokemon_Client pokemonClient;

    public Client_Player(Client_Connection connection) {
        this.connection = connection;
    }

    public void run(){
        pokemonClient = new Pokemon_Client(this);
        pokemonClient.run();
    }

    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    // communication related methods
    public void sendMsg(String line){
        connection.sendMessage(line);
    }

    public InputStream getInputStream(){
        InputStream inputStream = null;
        try {
            inputStream = connection.clientSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }
    public OutputStream getOutputStream(){
        OutputStream outputStream = null;
        try{
            outputStream = connection.clientSocket.getOutputStream();
        } catch (IOException e){
            e.printStackTrace();
        }
        return outputStream;
    }

    public static void main(String[] args) {
        Client_Connection connection = new Client_Connection(4445);
        new Client_Player(connection).run();
    }
}
