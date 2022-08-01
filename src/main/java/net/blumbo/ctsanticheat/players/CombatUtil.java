package net.blumbo.ctsanticheat.players;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CombatUtil {

    // Optimal amount of ticks is the highest accepted ping in ms divided by 50 plus one
    // e.g. for 200ms it would be 5, for 300ms would be 7 etc.
    public static int savedPositionAmount = 9;

    // If target is not in reach (possibly due to ping) check if target's previous locations are in reach
    public static boolean allowReach(ServerPlayer attacker, ServerPlayer target) {
        Vec3 eyePosition = attacker.getEyePosition(0);

        double reach = attacker.getCurrentAttackReach(1f);
        if (!attacker.canSee(target)) reach = 2.5;
        reach *= reach;

        if (canReach(eyePosition, target.getBoundingBox(), reach)) return true;

        PlayerData victimData = PlayerData.get(target);
        System.out.println(victimData.positionIndex);
        for (AABB boundingBox : victimData.previousPositions) {
            if (boundingBox == null) continue;
            if (canReach(eyePosition, boundingBox, reach)) return true;
        }

        return false;
    }

    private static boolean canReach(Vec3 eyePosition, AABB boundingBox, double reach) {
        return eyePosition.distanceToSqr(boundingBox.getNearestPointTo(eyePosition)) < reach;
    }

    // Update players' previous positions every tick
    public static void setPosition(ServerPlayer player) {
        PlayerData playerData = PlayerData.get(player);

        playerData.positionIndex++;
        if (playerData.positionIndex >= playerData.previousPositions.length) playerData.positionIndex = 0;

        playerData.previousPositions[playerData.positionIndex] = player.getBoundingBox();
    }

    // Decrease invulnerability ticks for non-fast hits to prevent no-regs for fast attacks
    public static int modifyInvulnerableTicks(int original) {
        if (original >= 4) return original - 1;
        return original;
    }

}
