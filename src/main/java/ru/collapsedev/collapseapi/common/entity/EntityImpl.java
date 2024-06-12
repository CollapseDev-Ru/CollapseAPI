package ru.collapsedev.collapseapi.common.entity;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import com.sun.tools.javac.Main;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.collapsedev.collapseapi.APILoader;
import ru.collapsedev.collapseapi.api.entity.Entity;
import ru.collapsedev.collapseapi.util.RandomUtil;

import java.util.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EntityImpl implements Entity {

    final PlayerManager playerManager = APILoader.getPacketEventsAPI().getPlayerManager();
    final Set<Player> visiblePlayers = new HashSet<>();

    final org.bukkit.Location location;
    final EntityType entityType;

    final int entityId;
    final WrapperPlayServerSpawnEntity spawnPacket;

    public EntityImpl(EntityType entityType, org.bukkit.Location location) {
        this.entityType = entityType;
        this.location = location;

        this.entityId = RandomUtil.randomInt(10000);
        UUID uuid = UUID.randomUUID();

        this.spawnPacket = new WrapperPlayServerSpawnEntity(
                entityId, uuid,
                entityType,
                reassemblyLocation(location),
                0, 0,
                new Vector3d()
        );
    }

    private static Location reassemblyLocation(org.bukkit.Location location) {
        return new Location(
                new Vector3d(location.getX(), location.getY(), location.getZ()),
                location.getYaw(),
                location.getPitch()
        );
    }

    public void sendPlayers(List<Player> players) {
        players.forEach(this::sendPlayer);
    }

    public void sendPlayerPacket(Player player, PacketWrapper<?> packet) {
        playerManager.sendPacket(player, packet);
    }

    public void sendVisiblePlayersPacket(PacketWrapper<?> packet) {
        visiblePlayers.forEach(player -> sendPlayerPacket(player, packet));
    }

    public void sendPlayer(Player player) {

        if (!visiblePlayers.contains(player)) {
            sendPlayerPacket(player, spawnPacket);
            visiblePlayers.add(player);
        }
    }

    public void teleport(org.bukkit.Location location) {
        WrapperPlayServerEntityTeleport teleportPacket = new WrapperPlayServerEntityTeleport(
                entityId,
                reassemblyLocation(location),
                false
        );

        sendVisiblePlayersPacket(teleportPacket);
    }

    public void setInvisible(boolean invisible) {
        byte invisibleByte = (byte) (invisible ? 0x20 : 0x00);
        WrapperPlayServerEntityMetadata invisiblePacket = new WrapperPlayServerEntityMetadata(
                entityId,
                Collections.singletonList(new EntityData(0, EntityDataTypes.BYTE, invisibleByte))
        );
        sendVisiblePlayersPacket(invisiblePacket);
    }

    public void setCustomName(String name) {
        WrapperPlayServerEntityMetadata namePacket = new WrapperPlayServerEntityMetadata(
                entityId,
                Arrays.asList(
                        new EntityData(3, EntityDataTypes.BOOLEAN, true),
                        new EntityData(2, EntityDataTypes.OPTIONAL_ADV_COMPONENT, Optional.of(new TextComponent(name))))
        );
        sendVisiblePlayersPacket(namePacket);
    }


    public void removeEntity(Player player) {
        WrapperPlayServerDestroyEntities destroyPacket = new WrapperPlayServerDestroyEntities(entityId);
        sendPlayerPacket(player, destroyPacket);
        visiblePlayers.remove(player);
    }

    public void removeEntity(List<Player> players) {
        players.forEach(this::removeEntity);
    }

    public void killEntity() {
        visiblePlayers.forEach(this::removeEntity);
        visiblePlayers.clear();
    }



}
