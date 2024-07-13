import java.io.*;
import java.net.Socket;

public class Client {

    Socket socket;
    BufferedReader bufferedReader;
    PrintWriter printWriter;

    Client() {
        try {
            System.out.println("Client app started");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Client is ready...");

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();
        } catch (Exception exception) {
            System.out.println("Connection is Closed");
        }
    }

    public void startReading() {

        Runnable r1 = () -> {
            try {
                while (true) {
                    String msg = bufferedReader.readLine();
                    if(msg == null || msg.equalsIgnoreCase("exit")) {
                        System.out.println("Server Stopped Communication");
                        socket.close();
                        break;
                    }

                    System.out.println(msg);
                }
            } catch (Exception e) {
                System.out.println("connection closed.");
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
                    printWriter.println("Client : "+ msg);
                    printWriter.flush();
                    if(socket.isClosed()) {
                        break;
                    }
                    if(msg.equalsIgnoreCase("exit")) {
                        socket.close();
                        System.out.println("Client(we) closed connection");
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
        new Client();
    }
}
