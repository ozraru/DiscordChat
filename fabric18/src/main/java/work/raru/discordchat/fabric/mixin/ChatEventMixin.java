package work.raru.discordchat.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import work.raru.discordchat.common.DiscordMessage;

@Mixin(ServerPlayNetworkHandler.class)
public class ChatEventMixin {
    @Accessor
    public ServerPlayerEntity getPlayer() {
        throw new AssertionError();
      };

    @Inject(at = @At("HEAD"), method = "onChatMessage(Lnet/minecraft/network/packet/c2s/play/ChatMessageC2SPacket;)V")
    public void onChatMessage(ChatMessageC2SPacket packet) {
        DiscordMessage.instance.fromMinecraft(getPlayer().getUuid(), getPlayer().getDisplayName().asString(), packet.getChatMessage());
    }
}
