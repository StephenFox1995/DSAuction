package stephenfox.tcp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class ClientHandler implements Runnable {
  private Socket socket;
  private Scanner input;
  private PrintWriter output;

  public ClientHandler(Socket socket) {
    this.socket = socket;
    try {
      this.input = new Scanner(this.socket.getInputStream());
      this.output = new PrintWriter(this.socket.getOutputStream(), true);
    } catch (IOException e) {
      System.out.println("Error trying to setup communication with client.\n");
      try {
        socket.close();
      } catch (IOException err) {
        System.out.println("Error closing socket: " + err.getMessage());
      }
    }
  }

  @Override
  public void run() {
    String message = input.nextLine();

    // New thread for this client.
    while (!message.equals(Server.ServerCommandMessages.SERVER_CLOSE)) {
      output.println("Hello client!");
      message = input.nextLine();
    }
    output.println("Bye Client!");
  }
}
