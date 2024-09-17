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
package gg.airplaines.tournaments.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class GameUtils {

    /**
     * Get the health of a player, formatted, and in percent form.
     * @param player Player to get health of.
     * @return Formatted health of player.
     */
    public static String getFormattedHealth(Player player) {
        int percent = MathUtils.percent(player.getHealth(), player.getMaxHealth());
        ChatColor color;

        if(percent < 25) {
            color = ChatColor.RED;
        }
        else if(percent < 50) {
            color = ChatColor.GOLD;
        }
        else if(percent < 75) {
            color = ChatColor.YELLOW;
        }
        else {
            color = ChatColor.GREEN;
        }

        return "" + color + percent + "%";
    }

    /**
     * Get a colored string of a player's ping.
     * @param player Player to get ping of.
     * @return Formatted ping.
     */
    public static String getFormattedPing(Player player) {
        //int ping = player.getPing();
        int ping = 0;
        ChatColor color;

        if(ping < 40) {
            color = ChatColor.GREEN;
        }
        else if(ping < 70) {
            color = ChatColor.YELLOW;
        }
        else if (ping < 110) {
            color = ChatColor.GOLD;
        }
        else {
            color = ChatColor.RED;
        }
        return color + "" + ping + " ms";
    }
}