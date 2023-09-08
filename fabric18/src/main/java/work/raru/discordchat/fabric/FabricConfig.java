package work.raru.discordchat.fabric;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import work.raru.discordchat.common.IConfig;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

public class FabricConfig implements IConfig {

    JsonObject jsonObj = null;
    FabricConfig(){
        reload();
    }

    @Override
    public void reload() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve("discordchat.json");
        Gson gson = new Gson();
        FabricLoader.getInstance().getConfigDir().toFile().mkdir();
        try {
            FileReader fileReader = new FileReader(path.toFile());
            jsonObj = gson.fromJson(fileReader, JsonObject.class);
            fileReader.close();
        } catch (IOException ignored) {
        }
        if (jsonObj == null){
            generateConfigFile(path);
            try {
                FileReader fileReader = new FileReader(path.toFile());
                jsonObj = gson.fromJson(fileReader, JsonObject.class);
                fileReader.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    private void generateConfigFile(Path path){
        String resourcePath = "/config.json";
        try (InputStream is = getClass().getResourceAsStream(resourcePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            FileWriter fileWriter = new FileWriter(path.toFile());
            br.transferTo(fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean getDebug() {
        return jsonObj.get("debug").getAsBoolean();
    }

    @Override
    public String getToken() {
        return jsonObj.get("discord").getAsJsonObject().get("token").getAsString();
    }

    @Override
    public long getDiscordChannelID() {
        return jsonObj.get("discord").getAsJsonObject().get("channel").getAsLong();
    }

    @Override
    public String getDiscordPrefix() {
        return jsonObj.get("discord").getAsJsonObject().get("prefix").getAsString();
    }

    @Override
    public URL getWebhookURL() {
        try {
            return new URL(jsonObj.get("discord").getAsJsonObject().get("webhook").getAsString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean getLoginRestriction() {
        return jsonObj.get("login_restriction").getAsBoolean();
    }

    @Override
    public int getTokenExpire() {
        return jsonObj.get("link").getAsJsonObject().get("token_expire").getAsInt();
    }

    @Override
    public String getDatabaseType() {
        return jsonObj.get("database").getAsJsonObject().get("type").getAsString();
    }

    @Override
    public String getDatabaseUrl() {
        return jsonObj.get("database").getAsJsonObject().get("url").getAsString();
    }

    @Override
    public String getDatabaseUser() {
        return jsonObj.get("database").getAsJsonObject().get("user").getAsString();
    }

    @Override
    public String getDatabasePassword() {
        return jsonObj.get("database").getAsJsonObject().get("password").getAsString();
    }

    @Override
    public String getDatabaseTablePrefix() {
        return jsonObj.get("database").getAsJsonObject().get("table_prefix").getAsString();
    }

    @Override
    public int getShutdownTimeSecond() {
        return jsonObj.get("shutdown_time").getAsInt();
    }

    @Override
    public ChatFormats getChatFormats() {
        return new ChatFormats() {
            final JsonObject chatFormatObj = jsonObj.get("chat_format").getAsJsonObject();
            @Override
            public String getDiscordLinkedName() {
                return chatFormatObj.get("discord").getAsJsonObject().get("linked_name").getAsString();
            }

            @Override
            public String getDiscordUnlinkedName() {
                return chatFormatObj.get("discord").getAsJsonObject().get("unlinked_name").getAsString();
            }

            @Override
            public String getMinecraftFormat(boolean linked, boolean reply, boolean attachment) {
                JsonObject minecraftFormat = chatFormatObj.get("minecraft").getAsJsonObject();
                if (attachment && reply){
                    minecraftFormat = minecraftFormat.get("reply_attachment").getAsJsonObject();
                }else if(reply){
                    minecraftFormat = minecraftFormat.get("reply").getAsJsonObject();
                }else if(attachment){
                    minecraftFormat = minecraftFormat.get("attachment").getAsJsonObject();
                }else{
                    minecraftFormat = minecraftFormat.get("normal").getAsJsonObject();
                }
                if (linked){
                    return minecraftFormat.get("linked").getAsString();
                }else{
                    return minecraftFormat.get("unlinked").getAsString();
                }
            }
        };
    }
}
