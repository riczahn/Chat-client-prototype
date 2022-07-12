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
  private static final String BEARER_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2xvY2FsaG9zdC9zcGFyZWZvb2QiLCJ1cG4iOiJ6YWhuQHRoLWJyYW5kZW5idXJnLmRlIiwiZXhwIjoxNjU3NzA4MjgxLCJncm91cHMiOlsiVXNlciJdLCJiaXJ0aGRhdGUiOiIxOTk5LTA4LTEyIiwiaWF0IjoxNjU3NjQ4MjgxLCJqdGkiOiI4MGI0ZjM3ZS01Y2FkLTQ5NzQtODUwNi05MjVkYmU0OTNjZmMifQ.hZEZg09NCdVjTsYd-hiMRBqgWGpbvaFzIlxciqfV2q86zHprnlcGzlV6cmc8YWevM0zGyvvBZtI38gOBBa_VvbcbFVNxAvG6CdGsmvMgpOIniz7mcU7ZuxHEaYIliNEeOb4FqiO4tuzdG7GTLwKtCJBlRhGgXnXonaA1cLDhw4mkt0xovk9WDux11DRX4YX03XNHYjupv0PcuukttOcAoZprAg5pBC_5hcf2kTe8-eF1qbAFQVngE9JcN6oTnY_MZSmBlb3wGiyqEB3yIHWuI3wz4zBVXLr5KeT0BGA4VkxbpdP7mxXpNcjLWf3VyGcrqhCdjOEsHtYIv0CtRgQ7jQ";
  private static final String BEARER_TOKEN_TOBIAS = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2xvY2FsaG9zdC9zcGFyZWZvb2QiLCJ1cG4iOiJ0cm9tcGVsbEB0aC1icmFuZGVuYnVyZy5kZSIsImV4cCI6MTY1NzcwOTEyMSwiZ3JvdXBzIjpbIlVzZXIiXSwiYmlydGhkYXRlIjoiMTk5OS0wOC0xMiIsImlhdCI6MTY1NzY0OTEyMSwianRpIjoiYjEzMjJmOTItMmRlNC00OWM4LTlhM2EtOWVjOTliOWEzNmY2In0.OM9N1jtqwrD1TRgITBgnXwbVduOHF_jSHoIePq7ZWuHPyW0Gu9sGZReTpRmgMlcnDdB6VRVytGiHJMcs9l5cuJkk4OG72gggc5OH4KQjxpS-ojOskKjdgRNyeoTibxIalxAhCGhfY_fci2hAswOvCMWaPODGkVBtU0H2pU3VMaWOC5qUoPiBy-ER1_tI13F6GeQyBzct1EIY6-Dr2VjU8mGhAQ-RKz6FFIUTiT2N7cCVVKhagbCz4572nKYbm6BqcUBVGKc5iRqaYvVfD_ERGXOzFniqXZOtPzFgMnh35RVkcMaX9zli61AHnxByPI-Pxjb1zxOgaDMqrj02TPTJUA";
  private static final String CHAT_SERVER_URL = "http://localhost:8080/chat";

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
      webSocket.addHeader("Authorization", "Bearer " + BEARER_TOKEN);
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
    message.setBearerToken(BEARER_TOKEN);
    message.setRecipient("trompell@th-brandenburg.de");

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
