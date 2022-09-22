package work.raru.discordchat.common;

import java.io.OutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Copied from https://gist.github.com/k3kdude/fba6f6b37594eae3d6f9475330733bdb
 * @author k3kdude
 * edited by ozraru
 *
 */
public class DiscordWebhook implements Runnable {

    private final URL url;
    private String content;
    private String username = null;
    private String avatarUrl = null;
    private boolean tts = false;

    /**
     * Constructs a new DiscordWebhook instance
     *
     * @param url The webhook URL obtained in Discord
     */
    public DiscordWebhook(URL url) {
        this.url = url;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setTts(boolean tts) {
        this.tts = tts;
    }
    
    @Override
    public void run() {
    	try {
            if (this.content == null) {
                throw new IllegalArgumentException("Set content");
            }

            JsonObject json = new JsonObject();

            json.add("content", new JsonPrimitive(this.content));
            if (this.username != null) {
                json.add("username", new JsonPrimitive(this.username));
            }
            if (this.avatarUrl != null) {
                json.add("avatar_url", new JsonPrimitive(this.avatarUrl));
            }
            json.add("tts", new JsonPrimitive(this.tts));

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            OutputStream stream = connection.getOutputStream();
            stream.write(json.toString().getBytes());
            stream.flush();
            stream.close();

            connection.getInputStream().close(); //I'm not sure why but it doesn't work without getting the InputStream
            connection.disconnect();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}