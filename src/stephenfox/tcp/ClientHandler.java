package stephenfox.tcp;

import stephenfox.auction.Bidder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by stephenfox on 31/10/2016.
 */
public class ClientHandler implements Runnable {
  private Socket socket;
  private DataInputStream inputStream;
  private DataOutputStream outputStream;
  private Bidder bidder;
  private String outputMessage = "";


  public ClientHandler(Socket socket) {
    this.socket = socket;
    try {
      this.inputStream = new DataInputStream(this.socket.getInputStream());
      this.outputStream = new DataOutputStream(this.socket.getOutputStream());
      this.bidder = new Bidder(this);
    }
    catch (IOException e) {
      System.out.println("Error trying to setup communication with client.\n");
      try {
        socket.close();
      } catch (IOException err) {
        System.out.println("Error closing socket: " + err.getMessage());
      }
    }
  }

  /**
   * Exposes a way to communicate back to a client machine.
   * @param message The message to send back to the client.
   *
   * */
  public void messageClient(String message) {
    outputMessage = message;
  }

  @Override
  public void run() {
    String inputMessage = "";
    while(true) {
      if (!outputMessage.isEmpty()) {
        // Send output message back to client.
        sendMessage(outputMessage);
      }
      outputMessage = "";
      // Any message send from the client, forward to the bidder to handle.
      inputMessage = readMessage();
      bidder.handleClientMessage(inputMessage);
    }
//    sendMessage("Bye");
  }

  private void sendMessage(String message) {
    try {
      outputStream.writeUTF(message);
      outputStream.flush();
    } catch (IOException e) { }
  }

  private String readMessage() {
    try {
      return inputStream.readUTF();
    } catch (IOException e) {
      return null;
    }
  }
}
