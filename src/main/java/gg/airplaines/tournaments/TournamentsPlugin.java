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
package gg.airplaines.tournaments;

import gg.airplaines.tournaments.commands.AbstractCommand;
import gg.airplaines.tournaments.game.GameManager;
import gg.airplaines.tournaments.game.arena.ArenaManager;
import gg.airplaines.tournaments.game.kit.KitManager;
import gg.airplaines.tournaments.game.lobby.LobbyManager;
import gg.airplaines.tournaments.game.tournament.DuelEventManager;
import gg.airplaines.tournaments.listeners.*;
import gg.airplaines.tournaments.settings.ConfigManager;
import gg.airplaines.tournaments.utils.chat.ChatUtils;
import gg.airplaines.tournaments.utils.gui.GUIListeners;
import gg.airplaines.tournaments.utils.scoreboard.ScoreboardUpdate;
import org.bukkit.plugin.java.JavaPlugin;

public final class TournamentsPlugin extends JavaPlugin {
    private ArenaManager arenaManager;
    private DuelEventManager duelEventManager;
    private ConfigManager configManager;
    private KitManager kitManager;
    private GameManager gameManager;
    private LobbyManager lobbyManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        ChatUtils.initialize(this);
        this.configManager = new ConfigManager(this);
        this.kitManager = new KitManager(this);
        this.arenaManager = new ArenaManager(this);
        this.gameManager = new GameManager(this);
        this.duelEventManager = new DuelEventManager(this);
        this.lobbyManager = new LobbyManager(this);

        arenaManager.loadArenas();

        // Updates scoreboards every second
        new ScoreboardUpdate().runTaskTimer(this, 20L, 20L);

        AbstractCommand.registerCommands(this);
        registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ChatUtils.disable();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListeners(), this);

        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityRegainHealthListener(this), this);
        getServer().getPluginManager().registerEvents(new EntitySpawnListener(this), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChangeListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItemListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerEggThrowListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerToggleFlightListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerToggleSneakListener(this), this);
        getServer().getPluginManager().registerEvents(new ProjectileLaunchListener(this), this);
        getServer().getPluginManager().registerEvents(new VehicleDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new VehicleExitListener(this), this);
    }

    public ArenaManager arenaManager() {
        return arenaManager;
    }

    public ConfigManager settingsManager() {
        return this.configManager;
    }

    public DuelEventManager duelEventManager() {
        return this.duelEventManager;
    }

    public KitManager kitManager() {
        return this.kitManager;
    }

    public GameManager gameManager() {
        return gameManager;
    }

    public LobbyManager getLobbyManager() {
        return this.lobbyManager;
    }
}
