package de.hglabor.slowrun.mixin;

import de.hglabor.slowrun.SlowRun;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(PlayerManager.class)
abstract class PlayerManagerMixin {
    @Shadow
    public abstract void broadcast(Text message, MessageType type, UUID sender);

    @Redirect(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"))
    private void cancelJoinMessage(PlayerManager instance, Text message, MessageType type, UUID sender, ClientConnection connection, ServerPlayerEntity player) {
        if (!player.getUuid().equals(SlowRun.NORISK_UUID)) {
            this.broadcast(message, MessageType.SYSTEM, Util.NIL_UUID);
        }
    }
}
