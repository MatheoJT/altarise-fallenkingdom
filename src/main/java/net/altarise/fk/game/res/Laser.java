package net.altarise.fk.game.res;

import net.altarise.fk.FallenKingdom;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

public class Laser {

    protected int entityId;
    protected DataWatcher dataWatcher;
    private List<Player> showTo;
    private int showTime = 0;


    private Location location;

    public Laser(Location location, List<Player> showTo, int showTime) {
        entityId = new Random().nextInt(Integer.MAX_VALUE);
        dataWatcher = new DataWatcher(null);
        byte data = (byte) calcData(0, 0, false); //onFire
        data = (byte) calcData(data, 1, false); //Crouched
        data = (byte) calcData(data, 3, false); //Sprinting
        data = (byte) calcData(data, 4, false); //Eating/Drinking/Block
        data = (byte) calcData(data, 5, true);  //Invisible
        dataWatcher.a(0, data);
        dataWatcher.a(6, 20F);
        int type = calcType(0, 2, false); //Is Elderly
        type = calcType(type, 4, false);  //Is retracting spikes
        dataWatcher.a(16, type);
        this.location = location;
        this.showTo = showTo;
        this.showTime = showTime;

        ArmorStand armorStand = location.getWorld().spawn(location.clone().add(0, 200, 0), ArmorStand.class);
        armorStand.setGravity(false);
        armorStand.setVisible(false);

        for (Player player : showTo) {
            setTarget(player, armorStand);
        }

        Bukkit.getScheduler().runTaskLater(FallenKingdom.INSTANCE(), () -> {
            for (Player player : showTo) {
                despawn(player);
            }
        }, showTime * 20L);
    }

    private void sendPacket(Packet packet) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    private void sendPacket(Player player, Packet packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }


    private void spawn(Player show) {
        try {
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
            set(packet, "a", entityId);
            set(packet, "b", 68);
            set(packet, "c", toFixedPointNumber(location.getX()));
            set(packet, "d", toFixedPointNumber(location.getY()));
            set(packet, "e", toFixedPointNumber(location.getZ()));
            set(packet, "f", (int) toPackedByte(location.getYaw()));
            set(packet, "g", (int) toPackedByte(location.getPitch()));
            set(packet, "h", (int) toPackedByte(location.getPitch()));
            set(packet, "i", (byte) 0);
            set(packet, "j", (byte) 0);
            set(packet, "k", (byte) 0);
            set(packet, "l", dataWatcher);
            if (show == null) {
                sendPacket(packet);
            } else {
                sendPacket(show, packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTarget(LivingEntity target) {
        try {
            spawn(null);
            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata();
            set(packet, "a", entityId);
            dataWatcher.a(17, (int) ((CraftEntity) target).getHandle().getId());
            set(packet, "b", dataWatcher.b());
            sendPacket(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTarget(Player show, LivingEntity target) {
        try {
            spawn(show);
            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata();
            set(packet, "a", entityId);
            dataWatcher.a(17, (int) ((CraftEntity) target).getHandle().getId());
            set(packet, "b", dataWatcher.b());
            sendPacket(show, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void despawn(Player show) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{entityId});
        if (show == null) {
            sendPacket(packet);
        } else {
            sendPacket(show, packet);
        }
    }

    protected int calcData(int data, int id, boolean flag) {
        if (flag) {
            return Integer.valueOf(data | 1 << id);
        } else {
            return Integer.valueOf(data & ~(1 << id));
        }
    }

    protected int calcType(int type, int id, boolean flag) {
        if (flag) {
            return Integer.valueOf(type | id);
        } else {
            return Integer.valueOf(type & ~id);
        }
    }

    protected byte toPackedByte(float f) {
        return (byte) ((int) (f * 256.0F / 360.0F));
    }

    protected int toFixedPointNumber(Double d) {
        return (int) (d * 32D);
    }

    protected void set(Object instance, String name, Object value) throws Exception {
        Field field = instance.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(instance, value);
    }
}