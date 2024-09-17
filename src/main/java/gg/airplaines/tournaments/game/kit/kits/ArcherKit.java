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
import gg.airplaines.tournaments.game.kit.Kit;
import gg.airplaines.tournaments.utils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class ArcherKit extends Kit {

    public ArcherKit(final TournamentsPlugin plugin) {
        super(plugin, "archer", "Archer");
        iconMaterial(Material.ARROW);

        naturalRegeneration(false);

        ItemStack helmet = new ItemBuilder(Material.LEATHER_HELMET)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .setUnbreakable(true)
                .build();
        ItemStack chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .setUnbreakable(true)
                .build();
        ItemStack leggings = new ItemBuilder(Material.LEATHER_LEGGINGS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .setUnbreakable(true)
                .build();
        ItemStack boots = new ItemBuilder(Material.LEATHER_BOOTS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .setUnbreakable(true)
                .build();

        ItemStack bow = new ItemBuilder(Material.BOW)
                .addEnchantment(Enchantment.ARROW_INFINITE, 1)
                .setUnbreakable(true)
                .build();
        ItemStack arrows = new ItemBuilder(Material.ARROW, 1).build();

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);

        addItem(0, bow);
        addItem(35, arrows);
    }
}