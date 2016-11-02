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

  public void accessServer(InetAddress host, int port) {
    try {
      socket = new Socket(host, port);
      System.out.println("Connected to server " + host.getCanonicalHostName() + " port " + port);

      // Setup thread for keyboard inputs.
      try {
        setupKeyboardHandler();
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }

    } catch (IOException e) {
      System.out.println("Error opening socket. " + e.getMessage());
    }
  }

  private void setupKeyboardHandler() throws IOException {
    keyboardInputHandler = new KeyboardInputHandler(socket);
    new Thread(keyboardInputHandler).start();
  }

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
      while(true) {
        try {
          System.out.print("Enter command:");
          message = keyboard.readLine();
          System.out.println("Going to send: " + message);
          outputStream.writeUTF(message);
          outputStream.flush();
        } catch (IOException e) { }
      }
    }
  }

}