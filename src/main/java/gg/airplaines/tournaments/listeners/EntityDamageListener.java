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
package gg.airplaines.tournaments.listeners;

import gg.airplaines.tournaments.TournamentsPlugin;
import gg.airplaines.tournaments.game.Game;
import gg.airplaines.tournaments.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
    private final TournamentsPlugin plugin;

    public EntityDamageListener(final TournamentsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player player)) {
            return;
        }

        // Cancel damage from specific causes.
        switch (event.getCause()) {
            case LIGHTNING:
            case FALL:
                event.setCancelled(true);
                return;
        }

        Game game = plugin.gameManager().game(player);

        if(game == null) {
            return;
        }

        // Prevent spectators from taking damage.
        if(game.spectators().contains(player)) {
            event.setCancelled(true);
            return;
        }

        // Players can only take damage when the game is running.
        if(game.gameState() != GameState.RUNNING) {
            event.setCancelled(true);
            return;
        }

        // Prevents "killing" a player twice.
        if(event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }

        // Kill player if they won't survive.
        if(event.getFinalDamage() >= player.getHealth()) {
            event.setCancelled(true);
            Bukkit.getScheduler().runTaskLater(plugin, () -> game.playerKilled(player), 1);
        }
    }
}