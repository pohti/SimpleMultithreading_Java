package Server;

public class GameServer {


    private Server_ViewerThread viewerThread;
    private Server_PlayerThread playerThread;

    public GameServer(){}

    public void run(){

        Broadcaster broadcaster = new Broadcaster();


        // A thread to let viewers connect to
        viewerThread = new Server_ViewerThread(broadcaster);
        viewerThread.start();

        // A thread to let player connect to
        playerThread = new Server_PlayerThread(this, broadcaster);
        playerThread.start();



    }// end of constructor

    public void endServerViewerThread(){ viewerThread.end(); }

    public static void main(String[] args) {
        new GameServer().run();
    }
}// end of class GameServer