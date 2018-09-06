package Server;

import Common.Message;

import java.util.ArrayList;

public class Broadcaster {

    private ArrayList<ClientHandler> clients;
    public Broadcaster(){
        clients = new ArrayList<ClientHandler>();
    }

    // send message to all connected clients
    public synchronized void sendMessage(String message){
        for(ClientHandler client : clients){
            client.sendMessage(message);
        }
    }

    public synchronized void removeAllClients(){
        for(ClientHandler client : clients){
            client.end();
        }

        clients.clear();
    }

    public synchronized void addClient(ClientHandler client){
        clients.add(client);
    }
}
