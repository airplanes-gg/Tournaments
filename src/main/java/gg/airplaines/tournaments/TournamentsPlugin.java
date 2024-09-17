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

import gg.airplaines.tournaments.game.GameManager;
import gg.airplaines.tournaments.game.arena.ArenaManager;
import gg.airplaines.tournaments.game.kit.KitManager;
import gg.airplaines.tournaments.game.tournament.DuelEventManager;
import gg.airplaines.tournaments.settings.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class TournamentsPlugin extends JavaPlugin {
    private ArenaManager arenaManager;
    private DuelEventManager duelEventManager;
    private ConfigManager configManager;
    private KitManager kitManager;
    private GameManager gameManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.configManager = new ConfigManager(this);
        this.kitManager = new KitManager(this);
        this.arenaManager = new ArenaManager(this);
        this.gameManager = new GameManager(this);
        this.duelEventManager = new DuelEventManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
}
