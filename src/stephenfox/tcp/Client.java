package stephenfox.tcp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.*;
import java.util.Scanner;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class Client {
  private ServerResponseHandler serverResponseHandler;
  private KeyboardInputHandler keyboardInputHandler;

  public void accessServer(InetAddress host, int port) {
    Socket socket = null;
    try {
      // Connect to server.
      socket = new Socket(host, port);
      System.out.println("Connect to server on port: " + port);

      // Handle Keyboard inputs from user on new thread.
      setupKeyboardInputHandler(socket);
      // Handle server responses on a new thread.
      setupServerResponseHandler(socket);

    } catch(IOException e) {
      System.out.println("Could not connect to server.");
    }
    finally {
      try {
        socket.close();
      } catch (IOException e) {
        System.out.println("Error setting up socket.");
      } catch (NullPointerException e) {
        System.out.println("Error setting up socket.");
      }
    }
  }

  /**
   * Sets up a new instance of {@link ServerResponseHandler} which will wait for responses
   * from the server.
   * @param socket The socket to set up the communication with the server.
   * */
  private void setupServerResponseHandler(Socket socket) throws IOException {
    serverResponseHandler = new ServerResponseHandler(socket);
    new Thread(serverResponseHandler).start();
  }

  /**
   * Sets up new instance of {@link KeyboardInputHandler} to handle keyboard inputs on new thread.
   * @param socket The socket to that will be used to send input from the keyboard.
   * */
  private void setupKeyboardInputHandler(Socket socket) throws IOException {
    keyboardInputHandler = new KeyboardInputHandler(socket);
    new Thread(keyboardInputHandler).start();
  }


  /**
   * This inner class is used to wait for input from the keyboard from the user
   * */
  private class KeyboardInputHandler implements Runnable {
    private Scanner keyboardScanner;
    private PrintWriter output;

    KeyboardInputHandler(Socket socket) throws IOException {
      keyboardScanner = new Scanner(System.in);
      output = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
      String commandForServer;

      // Ask the server to allow this client to join.
      output.println("join");
      do {
        System.out.print("Enter command: ");
        commandForServer = keyboardScanner.nextLine();
        System.out.println(commandForServer);
        output.println(commandForServer); // Send message to server.
      }
      while (!commandForServer.equals("QUIT"));
    }
  }


  /**
   * This inner class is used to handle responses from the server.
   * */
  private class ServerResponseHandler implements Runnable {
    private Scanner serverResponse;

    ServerResponseHandler(Socket socket) throws IOException {
      this.serverResponse = new Scanner(socket.getInputStream());
      this.serverResponse.useDelimiter("\n");
    }

    @Override
    public void run() {
      while (true) {
        if (serverResponse.hasNextLine()) {
          String response = serverResponse.nextLine();
          System.out.print(response);
        }
      }
    }
  }
}
