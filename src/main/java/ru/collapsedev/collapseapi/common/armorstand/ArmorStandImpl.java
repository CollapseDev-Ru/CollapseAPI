package ru.collapsedev.collapseapi.common.armorstand;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.collapsedev.collapseapi.api.armorstand.ArmorStand;
import ru.collapsedev.collapseapi.common.armorstand.store.EntityEquipments;
import ru.collapsedev.collapseapi.common.armorstand.store.EntitySettings;
import ru.collapsedev.collapseapi.service.ArmorStandsService;
import ru.collapsedev.collapseapi.util.RandomUtil;
import ru.collapsedev.collapseapi.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArmorStandImpl implements ArmorStand {

    final Map<UUID, Boolean> players = new HashMap<>();

    final int entityId;
    final WrapperPlayServerSpawnLivingEntity spawnPacket;
    final WrapperPlayServerDestroyEntities destroyPacket;
    WrapperPlayServerEntityEquipment equipmentPacket;
    WrapperPlayServerEntityMetadata metadataPacket;

    @Getter
    org.bukkit.Location location;

    @Getter
    boolean visibleAll;

    @Override
    public void setVisibleAll(boolean visibleAll) {
        this.visibleAll = visibleAll;

        if (visibleAll) {
            Bukkit.getOnlinePlayers().forEach(this::addPlayer);
        } else {
            Bukkit.getOnlinePlayers().forEach(this::removePlayer);
        }
    }

    public ArmorStandImpl(Plugin plugin, org.bukkit.Location location) {
        this.location = location;

        this.entityId = RandomUtil.randomInt(Integer.MAX_VALUE);
        UUID uuid = UUID.randomUUID();

        this.spawnPacket = new WrapperPlayServerSpawnLivingEntity(
                entityId,
                uuid,
                EntityTypes.ARMOR_STAND,
                reassemblyLocation(location),
                location.getPitch(),
                Vector3d.zero(),
                Collections.emptyList()
        );
        this.destroyPacket = new WrapperPlayServerDestroyEntities(entityId);

        ArmorStandsService.addArmorStand(plugin, this);
    }

    private static Location reassemblyLocation(org.bukkit.Location location) {
        return new Location(
                new Vector3d(location.getX(), location.getY(), location.getZ()),
                location.getYaw(),
                location.getPitch()
        );
    }

    private void sendPlayerPacket(Player player, PacketWrapper<?> packet) {
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    private void sendVisiblePlayersPacket(PacketWrapper<?> packet) {
        players.keySet().forEach(uuid -> sendPlayerPacket(Bukkit.getPlayer(uuid), packet));
    }

    public void addPlayer(Player player) {
        players.put(player.getUniqueId(), true);
        visible(player);
    }

    public void removePlayer(Player player) {
        if (!players.containsKey(player.getUniqueId())) {
            return;
        }
        players.remove(player.getUniqueId());
        hide(player);
    }

    public void hidePlayer(Player player) {
        players.put(player.getUniqueId(), false);
        hide(player);
    }

    public void spawn(Player player) {
        sendPlayerPacket(player, spawnPacket);
        Optional.ofNullable(equipmentPacket)
                .ifPresent(paket -> sendPlayerPacket(player, paket));
        Optional.ofNullable(metadataPacket)
                .ifPresent(paket -> sendPlayerPacket(player, paket));
    }

    public void hide(Player player) {
        sendPlayerPacket(player, destroyPacket);
    }

    public void hideAll() {
        getPlayers(true).forEach(this::hide);
    }

    public List<Player> getPlayers(boolean visible) {
        return players.entrySet().stream()
                .filter(map -> map.getValue() == visible)
                .map(map -> Bukkit.getPlayer(map.getKey()))
                .collect(Collectors.toList());
    }

    public void sendSpawnPacket(Player player) {
        if (player.getWorld() == location.getWorld()) {
            spawn(player);
        }
    }

    public void sendHidePacket(Player player) {
        if (player.getWorld() != location.getWorld()) {
            hide(player);
        }
    }

    public void visible(Player player) {
        if (!players.getOrDefault(player.getUniqueId(), false)) {
            return;
        }
        if (player.getWorld() == location.getWorld()) {
            sendSpawnPacket(player);
        } else {
            sendHidePacket(player);
        }
    }

    public void teleport(org.bukkit.Location location) {
        this.location = location;

        WrapperPlayServerEntityTeleport teleportPacket = new WrapperPlayServerEntityTeleport(
                entityId,
                reassemblyLocation(location),
                false
        );

        sendVisiblePlayersPacket(teleportPacket);
    }

    public void setEquipments(EntityEquipments equipments) {
        List<Equipment> equipmentsList = new ArrayList<>();

        Optional.ofNullable(equipments.getHelmet())
                .ifPresent(item -> equipmentsList.add(new Equipment(EquipmentSlot.HELMET, item)));

        Optional.ofNullable(equipments.getBody())
                .ifPresent(item -> equipmentsList.add(new Equipment(EquipmentSlot.BODY, item)));

        Optional.ofNullable(equipments.getLeggings())
                .ifPresent(item -> equipmentsList.add(new Equipment(EquipmentSlot.LEGGINGS, item)));

        Optional.ofNullable(equipments.getBoots())
                .ifPresent(item -> equipmentsList.add(new Equipment(EquipmentSlot.BOOTS, item)));

        Optional.ofNullable(equipments.getMainHand())
                .ifPresent(item -> equipmentsList.add(new Equipment(EquipmentSlot.MAIN_HAND, item)));

        Optional.ofNullable(equipments.getOffHand())
                .ifPresent(item -> equipmentsList.add(new Equipment(EquipmentSlot.OFF_HAND, item)));


        this.equipmentPacket = new WrapperPlayServerEntityEquipment(
                entityId,
                equipmentsList
        );
        sendVisiblePlayersPacket(equipmentPacket);
    }


    public void setSettings(EntitySettings settings) {
        List<EntityData> entityData = new ArrayList<>();

        byte invisible = (byte) (settings.isInvisible() ? 0x20 : 0x00);
        entityData.add(new EntityData(0, EntityDataTypes.BYTE, invisible));

        byte bitmask14 = 0;

        bitmask14 |= (byte) (settings.isSmall() ? 0x01 : 0x00);
        bitmask14 |= (byte) (settings.isVisibleArms() ? 0x04 : 0x00);
        entityData.add(new EntityData(14, EntityDataTypes.BYTE, bitmask14));

        entityData.add(new EntityData(3, EntityDataTypes.BOOLEAN, settings.isVisibleCustomName()));

        if (settings.isVisibleCustomName()) {
            String customName = StringUtil.color(settings.getCustomName());
            entityData.add(new EntityData(2, EntityDataTypes.OPTIONAL_ADV_COMPONENT, Optional.of(Component.text(customName))));
        }


        Optional.ofNullable(settings.getHead())
                .ifPresent(vector -> entityData.add(new EntityData(15, EntityDataTypes.ROTATION, vector)));

        Optional.ofNullable(settings.getBody())
                .ifPresent(vector -> entityData.add(new EntityData(16, EntityDataTypes.ROTATION, vector)));

        Optional.ofNullable(settings.getLeftArm())
                .ifPresent(vector -> entityData.add(new EntityData(17, EntityDataTypes.ROTATION, vector)));

        Optional.ofNullable(settings.getRightArm())
                .ifPresent(vector -> entityData.add(new EntityData(18, EntityDataTypes.ROTATION, vector)));

        Optional.ofNullable(settings.getLeftLeg())
                .ifPresent(vector -> entityData.add(new EntityData(19, EntityDataTypes.ROTATION, vector)));

        Optional.ofNullable(settings.getRightLeg())
                .ifPresent(vector -> entityData.add(new EntityData(20, EntityDataTypes.ROTATION, vector)));

        this.metadataPacket = new WrapperPlayServerEntityMetadata(
                entityId,
                entityData
        );

        sendVisiblePlayersPacket(metadataPacket);
    }


}