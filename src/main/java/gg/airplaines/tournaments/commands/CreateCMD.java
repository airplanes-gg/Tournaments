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
package gg.airplaines.tournaments.commands;

import com.cryptomorin.xseries.XMaterial;
import gg.airplaines.tournaments.TournamentsPlugin;
import gg.airplaines.tournaments.game.kit.Kit;
import gg.airplaines.tournaments.game.tournament.BestOf;
import gg.airplaines.tournaments.game.tournament.EliminationType;
import gg.airplaines.tournaments.game.tournament.EventStatus;
import gg.airplaines.tournaments.game.tournament.TeamType;
import gg.airplaines.tournaments.utils.chat.ChatUtils;
import gg.airplaines.tournaments.utils.gui.CustomGUI;
import gg.airplaines.tournaments.utils.items.ItemBuilder;
import gg.airplaines.tournaments.utils.items.SkullBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class CreateCMD extends AbstractCommand {
    private final TournamentsPlugin plugin;

    public CreateCMD(TournamentsPlugin plugin) {
        super("create", "tournament.use", false);
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {

        // Make sure no other tournaments are currently running.
        if(plugin.duelEventManager().activeEvent() != null) {
            ChatUtils.chat(sender, "&c&lError &8» &cThere is already a tournament active!");
            return;
        }

        Player player = (Player) sender;
        plugin.duelEventManager().host(player);
        new SetKitGUI().open(player);
    }

    private class SetKitGUI extends CustomGUI {
        public SetKitGUI() {
            super(54, "Select A Kit");
            addFiller(0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);

            int i = 0;
            for(Kit kit : plugin.kitManager().kits()) {
                if(plugin.arenaManager().getArenas(kit).size() == 0) {
                    continue;
                }

                setItem(i + 9, new ItemBuilder(kit.iconMaterial())
                                .setDisplayName("<green><bold>" + kit.name())
                                .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                                .addLore("").addLore("<green>Click to Select!").build(),
                        (p, a) -> {
                            plugin.duelEventManager().kit(kit);
                            new SetBracketGUI().open(p);
                        });
                i++;
            }
        }
    }

    private class SetBracketGUI extends CustomGUI {
        public SetBracketGUI() {
            super(54, "Select Bracket");
            addFiller(0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);

            ItemStack singleElim = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQ2NWNlODNmMWFhNWI2ZTg0ZjliMjMzNTk1MTQwZDViNmJlY2ViNjJiNmQwYzY3ZDFhMWQ4MzYyNWZmZCJ9fX0=")
                    .asItemBuilder()
                    .setDisplayName("<green><bold>Single Elimination")
                    .addLore("").addLore("<green>Click to Select!").build();
            setItem(20, singleElim, (p,a) -> {
                plugin.duelEventManager().eventType(EliminationType.SINGLE_ELIMINATION);
                new SetBestOfGUI().open(p);
            });

            ItemStack doubleElim = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGQ1NGQxZjhmYmY5MWIxZTdmNTVmMWJkYjI1ZTJlMzNiYWY2ZjQ2YWQ4YWZiZTA4ZmZlNzU3ZDMwNzVlMyJ9fX0=")
                    .asItemBuilder()
                    .setDisplayName("<green><bold>Double Elimination")
                    .addLore("").addLore("<green>Click to Select!").build();
            setItem(24, doubleElim, (p,a) -> {
                plugin.duelEventManager().eventType(EliminationType.DOUBLE_ELIMINATION);
                new SetBestOfGUI().open(p);
            });
        }
    }

    private class SetBestOfGUI extends CustomGUI {
        public SetBestOfGUI() {
            super(54, "Select Best Of");
            addFiller(0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);

            ItemStack bo1 = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQ2NWNlODNmMWFhNWI2ZTg0ZjliMjMzNTk1MTQwZDViNmJlY2ViNjJiNmQwYzY3ZDFhMWQ4MzYyNWZmZCJ9fX0=")
                    .asItemBuilder()
                    .setDisplayName("<green><bold>Best of 1")
                    .addLore("").addLore("<green>Click to Select!").build();
            setItem(19, bo1, (p,a) -> {
                plugin.duelEventManager().bestOf(BestOf.ONE);
                //plugin.duelEventManager().eventStatus(EventStatus.WAITING);
                //p.closeInventory();

                //new HostPlayingGUI().open(p);
                new TeamSizeGUI().open(p);

                // TODO: Update Bungeecord.
            });

            ItemStack bo3 = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjFlNGVhNTliNTRjYzk5NDE2YmM5ZjYyNDU0OGRkYWMyYTM4ZWVhNmEyZGJmNmU0Y2NkODNjZWM3YWM5NjkifX19")
                    .asItemBuilder()
                    .setDisplayName("<green><bold>Best of 3")
                    .addLore("").addLore("<green>Click to Select!").build();
            setItem(21, bo3, (p,a) -> {
                plugin.duelEventManager().bestOf(BestOf.THREE);
                //plugin.duelEventManager().eventStatus(EventStatus.WAITING);
                //p.closeInventory();
                // TODO: Update Bungeecord.

                //new HostPlayingGUI().open(p);
                new TeamSizeGUI().open(p);
            });

            ItemStack bo5 = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODRjOGMzNzEwZGEyNTU5YTI5MWFkYzM5NjI5ZTljY2VhMzFjYTlkM2QzNTg2YmZlYTZlNmUwNjEyNGIzYyJ9fX0=")
                    .asItemBuilder()
                    .setDisplayName("<green><bold>Best of 5")
                    .addLore("").addLore("<green>Click to Select!").build();
            setItem(23, bo5, (p,a) -> {
                plugin.duelEventManager().bestOf(BestOf.FIVE);
                //plugin.duelEventManager().eventStatus(EventStatus.WAITING);
                //p.closeInventory();
                //new HostPlayingGUI().open(p);
                new TeamSizeGUI().open(p);

                // TODO: Update Bungeecord.
            });

            ItemStack bo7 = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjRiZGU3OWY4NGZjNWYzZjFmYmM1YmMwMTA3MTA2NmJkMjBjZDI2M2ExNjU0ZDY0ZDYwZDg0MjQ4YmE5Y2QifX19")
                    .asItemBuilder()
                    .setDisplayName("<green><bold>Best of 7")
                    .addLore("").addLore("<green>Click to Select!").build();
            setItem(25, bo7, (p,a) -> {

                plugin.duelEventManager().bestOf(BestOf.SEVEN);
                //plugin.duelEventManager().eventStatus(EventStatus.WAITING);
                //p.closeInventory();
                //new HostPlayingGUI().open(p);
                new TeamSizeGUI().open(p);

                // TODO: Update Bungeecord.
            });
        }
    }

    private class HostPlayingGUI extends CustomGUI {
        public HostPlayingGUI() {
            super(54, "Are you playing?");

            addFiller(0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);

            ItemStack yes = new ItemBuilder(XMaterial.GREEN_WOOL)
                    .setDisplayName("<green><bold>Yes")
                    .addLore("<green>I am playing.")
                    .build();
            setItem(20, yes, (p,a) -> {
                plugin.duelEventManager().hostPlaying(true);
                plugin.duelEventManager().eventStatus(EventStatus.WAITING);
                p.closeInventory();

                ChatUtils.broadcast("&b▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                ChatUtils.broadcast(ChatUtils.centerText("&b&lTournament"));
                ChatUtils.broadcast("");
                ChatUtils.broadcast(ChatUtils.centerText("&f" + p.getName() + " &bis hosting a &f" + plugin.duelEventManager().kit().name() + " Duels &btournament!"));
                ChatUtils.broadcast("");
                ChatUtils.broadcast("  <dark_gray>» <click:run_command:'/event'><hover:show_text:'<yellow>Click to Join!'><yellow>Click here to join!</hover></click>");
                ChatUtils.broadcast("");
                ChatUtils.broadcast("&b▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            });

            ItemStack no = new ItemBuilder(XMaterial.RED_WOOL)
                    .setDisplayName("<red><bold>No")
                    .addLore("<red>I am not playing.")
                    .build();
            setItem(24, no, (p,a) -> {
                plugin.duelEventManager().hostPlaying(false);
                plugin.duelEventManager().eventStatus(EventStatus.WAITING);
                p.closeInventory();

                ChatUtils.broadcast("&b▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                ChatUtils.broadcast(ChatUtils.centerText("&b&lTournament"));
                ChatUtils.broadcast("");
                ChatUtils.broadcast(ChatUtils.centerText("&f" + p.getName() + " &bis hosting a &f" + plugin.duelEventManager().kit().name() + " Duels &btournament!"));
                ChatUtils.broadcast("");
                ChatUtils.broadcast("  <dark_gray>» <click:run_command:'/event'><hover:show_text:'<yellow>Click to Join!'><yellow>Click here to join!</hover></click>");
                ChatUtils.broadcast("");
                ChatUtils.broadcast("&b▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            });
        }
    }

    private class TeamSizeGUI extends CustomGUI {
        public TeamSizeGUI() {
            super(54, "Select a Team Size");

            ItemStack one = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQ2NWNlODNmMWFhNWI2ZTg0ZjliMjMzNTk1MTQwZDViNmJlY2ViNjJiNmQwYzY3ZDFhMWQ4MzYyNWZmZCJ9fX0=")
                    .asItemBuilder()
                    .setDisplayName("<green><bold>1v1")
                    .build();
            setItem(20, one, (p,a) -> {
                plugin.duelEventManager().teamSize(TeamType.ONE_V_ONE);
                new HostPlayingGUI().open(p);
            });

            ItemStack two = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGQ1NGQxZjhmYmY5MWIxZTdmNTVmMWJkYjI1ZTJlMzNiYWY2ZjQ2YWQ4YWZiZTA4ZmZlNzU3ZDMwNzVlMyJ9fX0=")
                    .asItemBuilder()
                    .setDisplayName("<green><bold>2v2 Random")
                    .build();
            setItem(22, two, (p,a) -> {
                plugin.duelEventManager().teamSize(TeamType.TWO_V_TWO_RANDOM);
                new HostPlayingGUI().open(p);
            });

            ItemStack three = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjFlNGVhNTliNTRjYzk5NDE2YmM5ZjYyNDU0OGRkYWMyYTM4ZWVhNmEyZGJmNmU0Y2NkODNjZWM3YWM5NjkifX19")
                    .asItemBuilder()
                    .setDisplayName("<green><bold>3v3 Random")
                    .build();
            setItem(24, three, (p,a) -> {
                plugin.duelEventManager().teamSize(TeamType.THREE_V_THREE_RANDOM);
                new HostPlayingGUI().open(p);
            });
        }
    }
}