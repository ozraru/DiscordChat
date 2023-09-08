package work.raru.discordchat.fabric.mixin;

import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.raru.discordchat.common.DiscordMessage;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ChatEventMixin {
    @Accessor
    public abstract ServerPlayerEntity getPlayer();

    @Inject(at = @At("HEAD"), method = "onChatMessage(Lnet/minecraft/network/packet/c2s/play/ChatMessageC2SPacket;)V")
    public void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo info) {
        if (!packet.getChatMessage().startsWith("/")){
            DiscordMessage.instance.fromMinecraft(getPlayer().getUuid(), getPlayer().getDisplayName().asString(), packet.getChatMessage());
        }
    }
}
