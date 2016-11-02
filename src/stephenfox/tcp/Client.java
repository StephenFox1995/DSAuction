package stephenfox.tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.*;
import java.util.Scanner;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class Client {

  private Socket socket;
  private KeyboardInputHandler keyboardInputHandler;
  private ServerResponseHandler serverResponseHandler;

  public void accessServer(InetAddress host, int port) {
    try {
      socket = new Socket(host, port);
      System.out.println("Connected to server " + host.getCanonicalHostName() + " port " + port);

      setupServerResponseHandler();// Setup thread for server responses.
      setupKeyboardHandler(); // Setup thread for keyboard inputs.

    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  private void setupKeyboardHandler() throws IOException {
    keyboardInputHandler = new KeyboardInputHandler(socket);
    new Thread(keyboardInputHandler).start();
  }

  private void setupServerResponseHandler() throws IOException {
    serverResponseHandler = new ServerResponseHandler(socket);
    new Thread(serverResponseHandler).start();
  }

  /**
   * Handles keyboard inputs on a separate thread.
   * */
  private class KeyboardInputHandler implements Runnable {
    private BufferedReader keyboard;
    private DataOutputStream outputStream;

    KeyboardInputHandler(Socket socket) throws IOException {
      keyboard = new BufferedReader(new InputStreamReader(System.in));
      outputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
      String message = "";
      System.out.print("Enter command:");
      while(true) {
        try {
          message = keyboard.readLine();
          outputStream.writeUTF(message);
          outputStream.flush();
        } catch (IOException e) { }
      }
    }
  }

  /**
   * Handles server response on a separate thread.
   * */
  private class ServerResponseHandler implements Runnable {
    private DataInputStream serverResponse;

    ServerResponseHandler(Socket socket) throws IOException {
      serverResponse = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
      String serverMessage;
      while(true) {
        try {
          serverMessage = serverResponse.readUTF();
          System.out.println();
          System.out.println("Server says: " + serverMessage);
          System.out.print("Enter Command:");
        } catch (IOException e) { }
      }
    }
  }
}