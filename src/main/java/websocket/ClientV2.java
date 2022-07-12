package websocket;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;
import java.util.Scanner;

public class ClientV2 {

  private final WebSocket webSocket;
  private final Scanner input;

  private final Gson gson = new Gson();

  private static final String CHAT_SERVER_URL = "http://localhost:8080/chat/richardstest";

  public ClientV2() throws IOException {
    this.input = new Scanner(System.in);
    webSocket = new WebSocketFactory().createSocket(CHAT_SERVER_URL);

    // we can add more listeners if we want, but onMessage is the most important one
    webSocket.addListener(
        new WebSocketAdapter() {
          @Override
          public void onTextMessage(WebSocket websocket, String message) throws Exception {
            Message messageObject = gson.fromJson(message, Message.class);
            System.out.println("RECEIVED FROM SERVER: " + messageObject);
          }
        });

    try {
      // Connect to the server and perform an opening handshake.
      // This method blocks until the opening handshake is finished.
      webSocket.connect();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public boolean userHitSend() {
    // this is mocking that a user hit send basically
    return input.hasNext();
  }

  public void sendMessage() {
    // read out whatever the user typed
    String messageContent = input.nextLine();

    Message message = new Message();
    message.setContent(messageContent);
    message.setFrom(
        "zahn@th-brandenburg.de"); // will most likely be obsolete once we add authentication
    message.setTo("luedrick@th-brandenburg.de");

    webSocket.sendText(gson.toJson(message));
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    ClientV2 client = new ClientV2();

    while (true) {
      if (client.userHitSend()) {
        client.sendMessage();
      }
      Thread.sleep(100);
    }
  }
}
