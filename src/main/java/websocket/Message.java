package websocket;

import lombok.Data;

@Data
public class Message {
  private String bearerToken;
  private String recipient;
  private String content;
}
