package net.blumbo.ctsanticheat.mixin;

import net.blumbo.ctsanticheat.players.CombatUtil;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerMixin {

    @Shadow public ServerPlayer player;
    Entity targetEntity;

    @Inject(method = "handleInteract", at = @At("HEAD"))
    private void handleInteract(ServerboundInteractPacket packet, CallbackInfo ci) {
        targetEntity = packet.getTarget(player.getLevel());
    }

    // If return value is smaller than player reach the interaction will be a success, otherwise pass
    @Redirect(method = "handleInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;distanceToSqr(Lnet/minecraft/world/phys/Vec3;)D"))
    private double getAttackDistance(Vec3 instance, Vec3 vec3) {

        // If target is not a player do vanilla code
        if (!(targetEntity instanceof ServerPlayer)) {
            Vec3 eyePosition = player.getEyePosition(0);
            return (eyePosition).distanceToSqr(targetEntity.getBoundingBox().getNearestPointTo(eyePosition));
        }

        ServerPlayer target = (ServerPlayer) targetEntity;
        if (CombatUtil.allowReach(player, target)) {
            return 0;
        } else {
            return Integer.MAX_VALUE;
        }

    }

}
