package work.raru.discordchat.common;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Member;

public class UserLinkManager {
    private UserLinkManager() {}

    static Random random = new Random();

    public static String linkQueue(@Nonnull UUID minecraftUUID, int expireSeconds) throws SQLException {
        String token = String.format("%06d", random.nextInt(1000000));
        linkQueue(minecraftUUID, token, expireSeconds);
        return token;
    }

    public static void linkQueue(@Nonnull UUID minecraftUUID, @Nonnull String token, int expireSeconds)
            throws SQLException {
        DatabaseManager.createToken(minecraftUUID, token,
                Timestamp.valueOf(LocalDateTime.now().plusSeconds(expireSeconds)));
    }

    @SuppressWarnings("null")
    public static String useToken(@Nonnull Member member, @Nonnull String token) throws SQLException {
        String minecraft = DatabaseManager.useToken(token);
        if (minecraft == null) {
            return null;
        }
        linkWrite(UUID.fromString(minecraft), member);
        return minecraft;
    }

    public static void linkWrite(@Nonnull UUID minecraftUUID, @Nonnull Member member) throws SQLException {
        unlink(member);
        unlink(minecraftUUID);
        DatabaseManager.addLink(minecraftUUID, member.getIdLong());
        Main.platform.getChat().tell(minecraftUUID,
                "You are linked by " + member.getEffectiveName() + "(" + member.getIdLong() + ")");
    }

    public static void unlink(UUID minecraft) throws SQLException {
        DatabaseManager.removeLink(minecraft);
    }

    public static void unlink(String minecraft) throws SQLException {
        DatabaseManager.removeLink(minecraft);
    }

    public static void unlink(Member member) throws SQLException {
        DatabaseManager.removeLink(member.getIdLong());
    }

    public static Member getDiscordMember(UUID minecraftUUID) throws SQLException {
        long discordId = getDiscordId(minecraftUUID);
        if (discordId > 0) {
            return DiscordMessage.getChannel().getGuild().retrieveMemberById(getDiscordId(minecraftUUID)).complete();
        } else {
            return null;
        }
    }

    public static long getDiscordId(UUID minecraftUUID) throws SQLException {
        return DatabaseManager.getDiscord(minecraftUUID);
    }

    public static UUID getMinecraftUUID(Member member) throws SQLException {
        return getMinecraftUUID(member.getIdLong());
    }

    public static UUID getMinecraftUUID(long discord) throws SQLException {
        String stringUUID = DatabaseManager.getMinecraft(discord);
        if (stringUUID != null) {
            return UUID.fromString(stringUUID);
        } else {
            return null;
        }
    }
}
