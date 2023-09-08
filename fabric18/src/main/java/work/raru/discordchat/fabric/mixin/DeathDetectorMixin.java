package work.raru.discordchat.fabric.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.raru.discordchat.common.DiscordMessage;

@Mixin(ServerPlayerEntity.class)
public abstract class DeathDetectorMixin{
    ServerPlayerEntity player =  ((ServerPlayerEntity)(Object)this);

    @Inject(at = @At("HEAD"), method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V")
    public void onDeath(DamageSource source, CallbackInfo info){
        if(player.world.getGameRules().getBoolean(GameRules.SHOW_DEATH_MESSAGES)){
            DiscordMessage.instance.gameMessage(player.getDamageTracker().getDeathMessage().getString());
        }
    }
}
