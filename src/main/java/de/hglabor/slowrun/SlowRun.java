package de.hglabor.slowrun;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SlowRun implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("slowrun");
    public static final UUID NORISK_UUID = UUID.fromString("26a4fcde-de39-4ff0-8ea1-786582b7d8ee");

    @Override
    public void onInitialize() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            Executors.newScheduledThreadPool(1).schedule(() -> server.executeSync(() -> hideNoRisk(handler, server)), 1, TimeUnit.SECONDS);
        });

        LOGGER.info("Slowrun wurde gestartet hehehe!");
    }

    private void hideNoRisk(ServerPlayNetworkHandler handler, MinecraftServer server) {
        var norisk = server.getPlayerManager().getPlayer(NORISK_UUID);

        if (!handler.player.getUuid().equals(NORISK_UUID)) {
            if (norisk != null) {
                LOGGER.info("Hiding NoRisk to " + handler.player.getEntityName());
                handler.connection.send(new PlayerListS2CPacket(PlayerListS2CPacket.Action.REMOVE_PLAYER, norisk));
            }
        } else {
            LOGGER.info("Hiding NoRisk to everyone");
            server.getPlayerManager().getPlayerList().stream().filter(player -> !player.getUuid().equals(NORISK_UUID)).forEach(player -> {
                player.networkHandler.connection.send(new PlayerListS2CPacket(PlayerListS2CPacket.Action.REMOVE_PLAYER, norisk));
            });
        }
    }
}
