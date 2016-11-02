package stephenfox.tcp;

import stephenfox.auction.Bidder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Created by stephenfox on 31/10/2016.
 */
public class ClientHandler implements Runnable {
  private Socket socket;
  private DataInputStream inputStream;
  private DataOutputStream outputStream;
  private Bidder bidder;
  private Queue<String> outputMessageQueue;
  private Queue<String> inputMessageQueue;

  public ClientHandler(Socket socket) {
    this.socket = socket;

    this.inputMessageQueue = new LinkedList<>();
    this.outputMessageQueue = new LinkedList<>();

    try {
      // Set time out for socket, so input stream does not constantly block, which
      // gives the server the chance to check on updates that may have been sent from other clients, such
      // as new bids or enterIntoAuction timeouts etc.
      this.socket.setSoTimeout(1000);
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
    outputMessageQueue.add(message);
  }

  @Override
  public void run() {
    while(true) {
      readMessage();
      while (!inputMessageQueue.isEmpty()) {
        // Any message send from the client, forward to the bidder to handle.
        String inputMessage = inputMessageQueue.remove();
        bidder.handleClientMessage(inputMessage);
      }

      while (!outputMessageQueue.isEmpty()) {
        String outputMessage = outputMessageQueue.remove();
        System.out.println("Sending message to client: " + outputMessage + Thread.currentThread().getName());
        // Send output message back to client.
        sendMessage(outputMessage);
      }
    }
  }

  private void sendMessage(String message) {
    try {
      outputStream.writeUTF(message);
      outputStream.flush();
    } catch (IOException e) {
      System.out.println("Could not send message to client");
    }
  }


  private void readMessage() {
    try {
      String inputMessage = inputStream.readUTF();
      inputMessageQueue.add(inputMessage);
    } catch (IOException e) {
    }
  }
}
