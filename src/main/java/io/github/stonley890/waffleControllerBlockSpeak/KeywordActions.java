package io.github.stonley890.waffleControllerBlockSpeak;

import net.dv8tion.jda.api.entities.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static io.github.stonley890.waffleControllerBlockSpeak.WaffleControllerBlockSpeak.*;

public class KeywordActions implements Listener {

    @NotNull
    private static ComponentBuilder formattedName(@NotNull User user) {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        String name = user.getName();
        componentBuilder.append(discordToName.get(name)).color(discordToColor.get(name));
        return componentBuilder;
    }

    private static final String[] badWords = {
            "arse",
            "arsehead",
            "arsehole",
            "ass",
            "ass hole",
            "asshole",
            "bastard",
            "bitch",
            "bloody",
            "bollocks",
            "brotherfucker",
            "bugger",
            "bullshit",
            "child-fucker",
            "cock",
            "cocksucker",
            "crap",
            "cunt",
            "dammit",
            "damn",
            "damned",
            "damn it",
            "dick",
            "dick-head",
            "dickhead",
            "dumb ass",
            "dumb-ass",
            "dumbass",
            "dyke",
            "faggot",
            "father-fucker",
            "fatherfucker",
            "fuck",
            "fucker",
            "fucking",
            "god dammit",
            "goddammit",
            "God damn",
            "god damn",
            "goddamn",
            "Goddamn",
            "goddamned",
            "goddamnit",
            "godsdamn",
            "hell",
            "holy shit",
            "horseshit",
            "in shit",
            "jackarse",
            "jack-ass",
            "jackass",
            "kike",
            "mother fucker",
            "mother-fucker",
            "motherfucker",
            "nigga",
            "nigra",
            "pigfucker",
            "piss",
            "prick",
            "pussy",
            "shit",
            "shit ass",
            "shite",
            "sibling fucker",
            "sisterfuck",
            "sisterfucker",
            "slut",
            "son of a bitch",
            "son of a whore",
            "spastic",
            "twat",
            "wanker"
    };


    public static void processKeywords(User user, @NotNull String text) {
        String lowerCase = text.toLowerCase();
        if (lowerCase.contains("laughter") || lowerCase.contains("laughs")) {

            for (Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.getScheduler().runTask(WaffleControllerBlockSpeak.getInstance(), () -> player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 30 * 20, 0, false, false)));

            }

            ComponentBuilder componentBuilder = formattedName(user);
            componentBuilder.append(" laughed.").color(ChatColor.WHITE);

            BaseComponent message = componentBuilder.build();

            Bukkit.getServer().spigot().broadcast(message);
        }
        if (lowerCase.contains("mumbling") || lowerCase.contains("mumbles")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getGameMode() != GameMode.SPECTATOR) Bukkit.getScheduler().runTask(WaffleControllerBlockSpeak.getInstance(), () -> player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30 * 20, 0, false, false)));

            }

            ComponentBuilder componentBuilder = formattedName(user);
            componentBuilder.append(" mumbled.").color(ChatColor.WHITE);

            BaseComponent message = componentBuilder.build();

            Bukkit.getServer().spigot().broadcast(message);
        }
        if (lowerCase.contains("kill")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getGameMode() != GameMode.SPECTATOR) Bukkit.getScheduler().runTask(WaffleControllerBlockSpeak.getInstance(), () -> player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10 * 20, 0, false, false)));
            }

            ComponentBuilder componentBuilder = formattedName(user);
            componentBuilder.append(" said" + lowerCase.split("kill")[0]).color(ChatColor.WHITE).append("kill").color(ChatColor.RED).append(lowerCase.split("kill")[1]).color(ChatColor.WHITE);

            BaseComponent message = componentBuilder.build();

            Bukkit.getServer().spigot().broadcast(message);
        }
        if (lowerCase.contains("eat")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getGameMode() != GameMode.SPECTATOR) Bukkit.getScheduler().runTask(WaffleControllerBlockSpeak.getInstance(), () -> player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 10 * 20, 0, false, false)));
            }

            ComponentBuilder componentBuilder = formattedName(user);
            componentBuilder.append(" said" + lowerCase.split("eat")[0]).color(ChatColor.WHITE).append("eat").color(ChatColor.RED).append(lowerCase.split("eat")[1]).color(ChatColor.WHITE);

            BaseComponent message = componentBuilder.build();

            Bukkit.getServer().spigot().broadcast(message);
        }
        if (lowerCase.contains("jump")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getGameMode() != GameMode.SPECTATOR) Bukkit.getScheduler().runTask(WaffleControllerBlockSpeak.getInstance(), () -> player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 10 * 20, 0, false, false)));
            }

            ComponentBuilder componentBuilder = formattedName(user);
            componentBuilder.append(" said" + lowerCase.split("jump")[0]).color(ChatColor.WHITE).append("jump").color(ChatColor.RED).append(lowerCase.split("jump")[1]).color(ChatColor.WHITE);

            BaseComponent message = componentBuilder.build();

            Bukkit.getServer().spigot().broadcast(message);
        }
        if (lowerCase.contains("run")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getGameMode() != GameMode.SPECTATOR) Bukkit.getScheduler().runTask(WaffleControllerBlockSpeak.getInstance(), () -> player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 1, false, false)));
            }

            ComponentBuilder componentBuilder = formattedName(user);
            componentBuilder.append(" said" + lowerCase.split("run")[0]).color(ChatColor.WHITE).append("run").color(ChatColor.RED).append(lowerCase.split("run")[1]).color(ChatColor.WHITE);

            BaseComponent message = componentBuilder.build();

            Bukkit.getServer().spigot().broadcast(message);
        }
        if (lowerCase.contains("break")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                final int iterations = 2;
                Bukkit.getScheduler().runTask(WaffleControllerBlockSpeak.getInstance(), () -> {
                    for (int i = 0; i < iterations; i++) {
                        for (int x = -1*i; x <= i; x++) {
                            for (int y = -1*i; y <= i; y++) {
                                for (int z = -1*i; z <= i; z++) {
                                    player.getLocation().add(x, y, z).getBlock().breakNaturally();
                                }
                            }
                        }
                    }
                });

            }

            ComponentBuilder componentBuilder = formattedName(user);
            componentBuilder.append(" said" + lowerCase.split("break")[0]).color(ChatColor.WHITE).append("break").color(ChatColor.RED).append(lowerCase.split("break")[1]).color(ChatColor.WHITE);

            BaseComponent message = componentBuilder.build();

            Bukkit.getServer().spigot().broadcast(message);
        }
        if (lowerCase.contains("mob")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getGameMode() != GameMode.SPECTATOR) Bukkit.getScheduler().runTask(WaffleControllerBlockSpeak.getInstance(), () -> player.getWorld().spawn(player.getLocation().add(0, 2, 0), Creeper.class));
            }

            ComponentBuilder componentBuilder = formattedName(user);
            componentBuilder.append(" said" + lowerCase.split("mob")[0]).color(ChatColor.WHITE).append("mob").color(ChatColor.RED).append(lowerCase.split("mob")[1]).color(ChatColor.WHITE);

            BaseComponent message = componentBuilder.build();

            Bukkit.getServer().spigot().broadcast(message);
        }
        if (lowerCase.contains("boom")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getGameMode() != GameMode.SPECTATOR) Bukkit.getScheduler().runTask(WaffleControllerBlockSpeak.getInstance(), () -> player.getWorld().spawn(player.getLocation().add(0, 2, 0), Creeper.class));
            }

            ComponentBuilder componentBuilder = formattedName(user);
            componentBuilder.append(" said" + lowerCase.split("boom")[0]).color(ChatColor.WHITE).append("boom").color(ChatColor.RED).append(lowerCase.split("boom")[1]).color(ChatColor.WHITE);

            BaseComponent message = componentBuilder.build();

            Bukkit.getServer().spigot().broadcast(message);
        }
        if (lowerCase.contains("shoot")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getGameMode() != GameMode.SPECTATOR) Bukkit.getScheduler().runTask(WaffleControllerBlockSpeak.getInstance(), () -> player.getWorld().spawn(player.getLocation().add(0, 2, 0), Skeleton.class));
            }

            ComponentBuilder componentBuilder = formattedName(user);
            componentBuilder.append(" said" + lowerCase.split("shoot")[0]).color(ChatColor.WHITE).append("shoot").color(ChatColor.RED).append(lowerCase.split("shoot")[1]).color(ChatColor.WHITE);

            BaseComponent message = componentBuilder.build();

            Bukkit.getServer().spigot().broadcast(message);
        }
//        if (lowerCase.contains("um")) {
//            for (Player player : Bukkit.getOnlinePlayers()) {
//                final int iterations = 2;
//                Bukkit.getScheduler().runTask(WaffleControllerBlockSpeak.getInstance(), () -> {
//                    for (int i = 0; i < iterations; i++) {
//                        for (int x = -1*i; x <= i; x++) {
//                            for (int y = -1*i; y <= i; y++) {
//                                for (int z = -1*i; z <= i; z++) {
//                                    player.getLocation().add(x, y, z).getBlock().setType(Material.GLASS);
//                                }
//                            }
//                        }
//                    }
//                });
//
//            }
//
//            ComponentBuilder componentBuilder = new ComponentBuilder();
//            componentBuilder.append(user.getName() + " said UM.");
//
//            BaseComponent message = componentBuilder.build();
//
//            Bukkit.getServer().spigot().broadcast(message);
//        }
//        if (lowerCase.contains("uh")) {
//            for (Player player : Bukkit.getOnlinePlayers()) {
//                final int iterations = 2;
//                Bukkit.getScheduler().runTask(WaffleControllerBlockSpeak.getInstance(), () -> {
//                    for (int i = 0; i < iterations; i++) {
//                        for (int x = -1*i; x <= i; x++) {
//                            for (int y = -1*i; y <= i; y++) {
//                                for (int z = -1*i; z <= i; z++) {
//                                    player.getLocation().add(x, y, z).getBlock().setType(Material.ICE);
//                                }
//                            }
//                        }
//                    }
//                });
//
//            }
//
//            ComponentBuilder componentBuilder = new ComponentBuilder();
//            componentBuilder.append(user.getName() + " said UH.");
//
//            BaseComponent message = componentBuilder.build();
//
//            Bukkit.getServer().spigot().broadcast(message);
//        }
        if (lowerCase.contains("like")) {

            ComponentBuilder componentBuilder = formattedName(user);
            componentBuilder.append(" said" + lowerCase.split("like")[0]).color(ChatColor.WHITE).append("like").color(ChatColor.RED).append(lowerCase.split("like")[1]).color(ChatColor.WHITE);

            Player player = Bukkit.getPlayer(discordToMinecraft.get(user.getName()));
            if (player != null) {
                if (player.getGameMode() == GameMode.SPECTATOR) return;
                PlayerInventory inventory = player.getInventory();
                ItemStack[] contents = inventory.getContents();
                List<ItemStack> filteredList = new ArrayList<>();
                for (ItemStack itemStack : contents) {
                    if (itemStack != null) filteredList.add(itemStack);
                }
                int count = filteredList.size();
                if (count == 0) {
                    componentBuilder.append("\nNormally I would take one of your item stacks, but you don't seem to have any, so I'll take your health instead.");
                    double health = player.getHealth();
                    double healthToDeplete = health - 1;
                    Bukkit.getScheduler().runTask(WaffleControllerBlockSpeak.getInstance(), () -> player.damage(healthToDeplete));
                } else {
                    int i = new Random().nextInt(0, count);
                    ItemStack deletedStack = filteredList.get(i);
                    inventory.remove(deletedStack);
                    componentBuilder.append("\nYour ").append(deletedStack.getAmount() + " " + deletedStack.getType().toString().toUpperCase()).color(ChatColor.YELLOW).append(" is now mine.").color(ChatColor.WHITE);
                }

            }
            else componentBuilder.append("\nHold on, you aren't in the game or your usernames don't match. That's cheating!");

            BaseComponent message = componentBuilder.build();

            Bukkit.getServer().spigot().broadcast(message);
        }
        if (lowerCase.contains("help")) {
            if (!Bukkit.getOnlinePlayers().isEmpty()) {

                ComponentBuilder componentBuilder = formattedName(user);
                componentBuilder.append(" said" + lowerCase.split("help")[0]).color(ChatColor.WHITE).append("help").color(ChatColor.RED).append(lowerCase.split("help")[1] +  ".\nDeploying help...").color(ChatColor.WHITE);
                Bukkit.getScheduler().runTaskLater(WaffleControllerBlockSpeak.getInstance(), () -> {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        for (int i = 0; i < 4; i++) {
                            Bukkit.getScheduler().runTask(WaffleControllerBlockSpeak.getInstance(), () -> player.getWorld().spawn(player.getLocation().add(0, 2, 0), Snowman.class));
                            Bukkit.getScheduler().runTask(WaffleControllerBlockSpeak.getInstance(), () -> player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 10 * 20, 0, false, false)));
                        }
                    }
                }, 60);

                BaseComponent message = componentBuilder.build();

                Bukkit.getServer().spigot().broadcast(message);
            }
        }
        for (String badWord : badWords) {
            if (lowerCase.contains(badWord)) {
                ComponentBuilder componentBuilder = formattedName(user);
                componentBuilder.append(" said" + lowerCase.split(badWord)[0]).color(ChatColor.WHITE).append(badWord).color(ChatColor.RED).append(lowerCase.split(badWord)[1]).color(ChatColor.WHITE);

                Player player = Bukkit.getPlayer(discordToMinecraft.get(user.getName()));
                if (player != null) {
                    Bukkit.getScheduler().runTask(WaffleControllerBlockSpeak.getInstance(), () -> player.getWorld().spawn(player.getLocation(), LightningStrike.class));
                }
                else componentBuilder.append("\nHold on, you aren't in the game or your usernames don't match. That's cheating!");

                BaseComponent message = componentBuilder.build();

                Bukkit.getServer().spigot().broadcast(message);

            }
        }
    }

//    @EventHandler
//    public void onPlayerChangeWorld(@NotNull PlayerChangedWorldEvent event) {
//        World end = Bukkit.getWorld("world_the_end");
//        if (event.getPlayer().getWorld().equals(end) && !inEnd) {
//            inEnd = true;
//
//            ComponentBuilder componentBuilder = new ComponentBuilder();
//            componentBuilder.append("LIKE was said " + likeCount + " time(s). Spawning " + likeCount + " End Crystals.");
//
//            BaseComponent message = componentBuilder.build();
//
//            Bukkit.getServer().spigot().broadcast(message);
//
//            Bukkit.getScheduler().runTask(WaffleControllerBlockSpeak.getInstance(), () -> {
//                Random random = new Random();
//                for (int i = 0; i < likeCount; i++) {
//                    int x = random.nextInt(-50, 50);
//                    int y = random.nextInt(80, 110);
//                    int z = random.nextInt(-50, 50);
//                    end.spawn(new Location(end, x, y, z), EnderCrystal.class);
//                }
//            });
//
//        }
//    }

}
