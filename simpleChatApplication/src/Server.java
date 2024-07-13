import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    ServerSocket server;
    Socket socket;
    BufferedReader bufferedReader;
    PrintWriter printWriter;

    Server() {
        try {
            System.out.println("Server app started");
            server = new ServerSocket(7777);
            System.out.println("Server is ready...");
            socket = server.accept();
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public void startReading() {
        
            Runnable r1 = () -> {
                try {
                    while (true) {
                        String msg = bufferedReader.readLine();
                        if(msg == null || msg.equalsIgnoreCase("exit"))  {
                            System.out.println("Client stopped communication");
                            socket.close();
                            break;
                        }

                        System.out.println(msg);
                    }
                } catch (Exception e) {
                    System.out.println("Connection closed.");
                }
            };
            
            new Thread(r1).start();
    }

    public void startWriting() {
        Runnable r2 = () -> {
            try {
                while (socket.isConnected() && !socket.isClosed()) {
                    BufferedReader bfReader = new BufferedReader(new InputStreamReader(System.in));
                    String msg = bfReader.readLine();
                    printWriter.println("server : "+ msg);
                    printWriter.flush();
                    if(socket.isClosed()) {
                        break;
                    }
                    if(msg.equalsIgnoreCase("exit")) {
                        socket.close();
                        System.out.println("Server(we) closed connection");
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Connection Closed.");
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        new Server();
    }

}
