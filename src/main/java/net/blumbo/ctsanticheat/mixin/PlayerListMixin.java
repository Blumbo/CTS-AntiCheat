package net.blumbo.ctsanticheat.mixin;

import net.blumbo.ctsanticheat.players.PlayerData;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    // Create player data on join
    @Inject(at = @At("HEAD"), method = "placeNewPlayer")
    private void placeNewPlayerHead(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
        PlayerData.addPlayerData(serverPlayer);
    }

    // Clear player data on leave
    @Inject(at = @At("TAIL"), method = "remove")
    private void removeTail(ServerPlayer serverPlayer, CallbackInfo ci) {
        PlayerData.removePlayerData(serverPlayer);
    }

}
