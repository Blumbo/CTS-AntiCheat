package net.blumbo.ctsanticheat.players;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    public AABB[] previousPositions;
    public int positionIndex;

    public PlayerData() {
        this.previousPositions = new AABB[CombatUtil.savedPositionAmount];
        this.positionIndex = 0;
    }

    private final static HashMap<UUID, PlayerData> allPlayerData = new HashMap<>();

    public static PlayerData get(ServerPlayer player) {
        if (!allPlayerData.containsKey(player.getUUID())) {
            addPlayerData(player);
        }
        return allPlayerData.get(player.getUUID());
    }

    public static void addPlayerData(ServerPlayer player) {
        PlayerData playerData = new PlayerData();
        allPlayerData.put(player.getUUID(), playerData);
    }

    public static void removePlayerData(ServerPlayer player) {
        if (!allPlayerData.containsKey(player.getUUID())) return;
        allPlayerData.remove(player.getUUID());
    }

}
