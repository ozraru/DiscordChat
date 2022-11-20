package work.raru.discordchat.common;

import java.time.Instant;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.TimeFormat;

public class ShutdownManager extends ListenerAdapter {
    private final static String EXTEND_BUTTON = "extend";
    private final static String SHUTDOWN_BUTTON = "shutdown";

    @Nonnull private static Timer timer = new Timer(true);
    @Nullable private static TimerTask task = null;
    @Nullable private static Message msg = null;

    private static boolean isEnabled() {
        return Main.platform.getConfig().getShutdownTimeSecond() >= 0;
    }

    public static void joinEvent() {
        if (!isEnabled())
            return;
        cancel();
    }

    public static void quitEvent() {
        if (!isEnabled())
            return;
        if (Main.platform.getUtility().getPlayerNum() > 1) {
            return;
        }
        if (task != null) {
            return;
        }
        Instant schedule = Instant.now().plusSeconds(Main.platform.getConfig().getShutdownTimeSecond());

        if (schedule == null)
            throw new AssertionError();

        if (schedule(schedule, false)) {
            DiscordMessage.getChannel().sendMessage("Shutdown scheduled " + TimeFormat.RELATIVE.atInstant(schedule))
                    .addActionRow(Button.primary(EXTEND_BUTTON, "Reset timer"), Button.danger(SHUTDOWN_BUTTON, "Shutdown immediately")).submit().thenAccept((Message m) -> {
                        msg = m;
                    });
        }
    }

    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        if (event.getMessageIdLong() != msg.getIdLong()) {
            return;
        }
        String id = event.getButton().getId();
        if (id == null) {
            return;
        }
        switch (id) {
            case EXTEND_BUTTON: {
                Instant schedule = Instant.now().plusSeconds(Main.platform.getConfig().getShutdownTimeSecond());

                if (schedule == null)
                    throw new AssertionError();

                if (schedule(schedule, true)) {
                    event.editMessage("Shutdown scheduled " + TimeFormat.RELATIVE.atInstant(schedule)).queue();
                }
                break;
            }
            case SHUTDOWN_BUTTON: {
                TimerTask task2 = task;
                if (task2 != null) {
                    task2.run();
                }
                break;
            }
        }
    }

    public static boolean isScheduled() {
        return task != null;
    }

    public static boolean schedule(@Nonnull Instant schedule, boolean override) {
        if (!isEnabled())
            return false;
        if (Main.platform.getUtility().isShutdowning())
            return false;
        if (task != null) {
            if (override) {
                task.cancel();
                task = null;
            } else {
                return false;
            }
        }
        task = new ShutdownTask();
        timer.schedule(task, Date.from(schedule));
        return true;
    }

    public static boolean cancel() {
        if (task != null) {
            task.cancel();
            task = null;
            if (msg != null) {
                msg.editMessage("Shutdown canceled").setComponents().queue();
                msg = null;
            }
            return true;
        } else {
            return false;
        }
    }

    static class ShutdownTask extends TimerTask {
        @Override
        public void run() {
            Main.platform.getUtility().shutdownServer();
            if (msg != null) {
                msg.editMessage("Shutdown executed").setComponents().queue();
                msg = null;
            }
        }
    }
}
