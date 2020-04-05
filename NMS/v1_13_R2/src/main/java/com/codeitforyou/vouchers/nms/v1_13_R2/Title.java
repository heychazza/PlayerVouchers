package com.codeitforyou.vouchers.nms.v1_13_R2;

import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Title implements com.codeitforyou.vouchers.nms.Title {
    public Title() {
        super();
    }

    @Override
    public void sendTitle(final Player player, final String message) {
        final PacketPlayOutTitle title2 = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a(message));
        final PacketPlayOutTitle length = new PacketPlayOutTitle(0, 11, 10);
        ((CraftPlayer) player.getPlayer()).getHandle().playerConnection.sendPacket(title2);
        ((CraftPlayer) player.getPlayer()).getHandle().playerConnection.sendPacket(length);
    }

    @Override
    public void sendSubtitle(final Player player, final String message) {
        final PacketPlayOutTitle title2 = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a(message));
        ((CraftPlayer) player.getPlayer()).getHandle().playerConnection.sendPacket(title2);
    }
}
