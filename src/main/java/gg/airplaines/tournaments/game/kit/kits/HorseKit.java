/*
 * This file is part of Tournament, licensed under the MIT License.
 *
 *  Copyright (c) airplanes.gg
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package gg.airplaines.tournaments.game.kit.kits;

import gg.airplaines.tournaments.TournamentsPlugin;
import gg.airplaines.tournaments.game.Game;
import gg.airplaines.tournaments.game.kit.Kit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HorseKit extends Kit {
    private final TournamentsPlugin plugin;

    public HorseKit(@NotNull final TournamentsPlugin tournamentsPlugin) {
        super(tournamentsPlugin, "horse", "Horse");
        this.plugin = tournamentsPlugin;

        iconMaterial(Material.DIAMOND_BARDING);
        exitVehicle(false);

        addItem(0, new ItemStack(Material.IRON_SWORD));
        addItem(3, new ItemStack(Material.BOW));
        addItem(5, new ItemStack(Material.GOLDEN_APPLE, 2));
        addItem(6, new ItemStack(Material.COOKED_BEEF, 64));

        addItem(34, new ItemStack(Material.ARROW, 64));
        addItem(35, new ItemStack(Material.ARROW, 64));

        addItem(39, new ItemStack(Material.IRON_HELMET));
        addItem(38, new ItemStack(Material.IRON_CHESTPLATE));
        addItem(37, new ItemStack(Material.IRON_LEGGINGS));
        addItem(36, new ItemStack(Material.IRON_BOOTS));
    }

    @Override
    public void onKitApply(Game game, Player player) {
        Horse horse = (Horse) game.world().spawnEntity(player.getLocation(), EntityType.HORSE);
        horse.setTamed(true); // Sets horse to tamed
        horse.setOwner(player); // Makes the horse the player's
        horse.setColor(Horse.Color.BROWN);
        horse.setMaxHealth(40);
        horse.setHealth(40);
        horse.setAdult();
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE, 1)); // Gives horse saddle
        horse.getInventory().setArmor(new ItemStack(Material.DIAMOND_BARDING)); // Gives the horse armor
        horse.teleport(player);

        // Delay fixes some weird client/server desync issue
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            horse.setPassenger(player);
        }, 3);
    }
}
