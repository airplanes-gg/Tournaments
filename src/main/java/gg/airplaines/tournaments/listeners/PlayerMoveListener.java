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
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    private final TournamentsPlugin plugin;

    public PlayerMoveListener(TournamentsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.gameManager().game(player);

        if(game == null) {
            return;
        }

        if(game.kit().waterKills()) {
            if(game.spectators().contains(player)) {
                player.teleport(game.arena().spectatorSpawn(game.world()));
                return;
            }

            Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
            Block block2 = player.getLocation().getBlock();

            if(block.getType() == Material.WATER || block2.getType() == Material.WATER) {
                game.playerKilled(player);
                return;
            }
        }

        if(player.getLocation().getY() < game.kit().voidLevel()) {
            if(game.gameState() == GameState.COUNTDOWN || game.gameState() == GameState.END) {
                player.teleport(game.arena().spectatorSpawn(game.world()));
                return;
            }

            if(game.spectators().contains(player)) {
                player.teleport(game.arena().spectatorSpawn(game.world()));
                return;
            }

            game.playerKilled(player);
            player.teleport(game.arena().spectatorSpawn(game.world()));
        }
    }
}