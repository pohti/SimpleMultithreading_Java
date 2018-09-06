package Client;

import java.io.IOException;
import java.util.Scanner;

public class Client_Viewer {
    Client_Connection connection;
    public Client_Viewer(Client_Connection connection){
        this.connection = connection;
    }

    public void run(){
        try {
            connection.listenToServer();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Client_Connection connection = new Client_Connection(4444);
        new Client_Viewer(connection).run();
    }
}
