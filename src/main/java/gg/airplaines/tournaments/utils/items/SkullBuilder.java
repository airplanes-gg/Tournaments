package gg.airplaines.tournaments.utils.items;

import com.cryptomorin.xseries.XMaterial;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Makes player skulls easier to create.
 */
@SuppressWarnings("unused")
public class SkullBuilder {
    private final ItemStack itemStack;
    private final SkullMeta skullMeta;

    /**
     * Create a SkullBuilder using a given identifier.
     * Uses Base64.
     * @param base64 Base64 to use.
     */
    public SkullBuilder(String base64) {
        itemStack = new ItemStack(Material.SKULL_ITEM);
        this.itemStack.setDurability((short) 3);

        skullMeta = (SkullMeta) itemStack.getItemMeta();

        // https://www.spigotmc.org/threads/how-to-create-heads-with-custom-base64-texture.352562/
        {
            GameProfile profile = new GameProfile(UUID.randomUUID(), "");
            profile.getProperties().put("textures", new Property("textures", base64));
            Field profileField = null;
            try {
                profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, profile);
            }
            catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create a SkullBuilder using an (Offline)Player.
     * @param offlinePlayer (Offline)Player to use.
     */
    public SkullBuilder(OfflinePlayer offlinePlayer) {
        this.itemStack = new ItemBuilder(XMaterial.PLAYER_HEAD).build();
        this.skullMeta = (SkullMeta) this.itemStack.getItemMeta();
        this.skullMeta.setOwner(offlinePlayer.getName());
    }

    /**
     * Convert the SkullBuilder to an ItemBuilder.
     * @return ItemBuilder.
     */
    public ItemBuilder asItemBuilder() {
        itemStack.setItemMeta(skullMeta);
        return new ItemBuilder(itemStack);
    }
}