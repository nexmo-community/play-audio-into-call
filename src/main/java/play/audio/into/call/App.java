package play.audio.into.call;

import static spark.Spark.*;
import io.github.cdimascio.dotenv.Dotenv;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.voice.VoiceName;
import com.nexmo.client.voice.ncco.Ncco;
import com.nexmo.client.voice.ncco.TalkAction;
import com.nexmo.client.voice.ncco.ConversationAction;
import com.nexmo.client.voice.StreamResponse;

public class App {
  public static void main(String[] args) throws Exception {
    Dotenv dotenv = Dotenv.load();
    port(Integer.parseInt(dotenv.get("PORT")));
    final String privateKeyPath = dotenv.get("NEXMO_PRIVATE_KEY_PATH");
    NexmoClient client = NexmoClient.builder()
        .applicationId(dotenv.get("NEXMO_APPLICATION_ID"))
        .privateKeyPath(privateKeyPath)
        .build();

    get("/webhooks/answer", (req, res) -> {
      String callId = req.queryParams("uuid");
      System.out.println("Call answered. The UUID for this call is: " + callId);
      TalkAction intro = TalkAction.builder("Please wait while we connect you to the conference.")
          .voiceName(VoiceName.RUSSELL)
          .build();
      ConversationAction conversation = ConversationAction.builder("Test conference")
          .build();
      res.type("application/json");
      return new Ncco(intro, conversation).toJson();
    });

    get("/play/:id", (req, res) -> {
      String id = req.params(":id");
      final String URL = dotenv.get("AUDIO_URL");
      System.out.println("Playing audio into " + id.toString());
      StreamResponse startStreamResponse = client.getVoiceClient().startStream(id, URL, 0);
      System.out.println(startStreamResponse.getMessage());
      Thread.sleep(Integer.parseInt(dotenv.get("AUDIO_DURATION_MSEC")));
      client.getVoiceClient().stopStream(id);
      return "";
    });
  }
}
