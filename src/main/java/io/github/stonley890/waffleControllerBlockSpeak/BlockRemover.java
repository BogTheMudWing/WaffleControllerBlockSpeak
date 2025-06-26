package io.github.stonley890.waffleControllerBlockSpeak;

import net.dv8tion.jda.api.entities.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static io.github.stonley890.waffleControllerBlockSpeak.WaffleControllerBlockSpeak.discordToColor;
import static io.github.stonley890.waffleControllerBlockSpeak.WaffleControllerBlockSpeak.discordToName;

public class BlockRemover {

    private static final Material[] immuneMaterials = {Material.END_PORTAL_FRAME, Material.END_PORTAL, Material.SPAWNER, Material.BEDROCK, Material.AIR, Material.LIGHT, Material.BARRIER};
    public static final Set<Material> deletedMaterials = new HashSet<>();

    private static final Map<Material, List<Chunk>> materialChuckRemovedList = new HashMap<>();

    @NotNull
    private static ComponentBuilder formattedName(@NotNull User user) {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        String name = user.getName();
        componentBuilder.append(discordToName.get(name)).color(discordToColor.get(name));
        return componentBuilder;
    }

    public static void addToDeletedList(@Nullable Collection<Material> materials) {
        if (materials == null || materials.isEmpty()) return;

        try {
            materials.removeAll(List.of(immuneMaterials));
        } catch (UnsupportedOperationException e) {
            return;
        }

        materials.forEach(material -> materialChuckRemovedList.computeIfAbsent(material, k -> new ArrayList<>()));

        deletedMaterials.addAll(materials);
        WaffleControllerBlockSpeak.getInstance().getConfig().set("deletedMaterials", deletedMaterials);
    }

    public static void removeMentionedBlocks(@NotNull User user, @NotNull String text) {

        List<Material> materials = findMaterials(text);

        if (materials.isEmpty()) return;

        addToDeletedList(materials);

        Headphones headphones = (Headphones) Bot.getAudioManager().getReceivingHandler();
        assert headphones != null;
        headphones.clearTotalBuffer(user);

        ComponentBuilder componentBuilder = formattedName(user);
        componentBuilder.append(" said").color(ChatColor.WHITE).append(text).color(ChatColor.YELLOW)
                .append("\n").color(ChatColor.WHITE).append("Removing ");
        for (int i = 0; i < materials.size(); i++) {
            Material material = materials.get(i);
            componentBuilder.append(material.name()).color(ChatColor.RED);
            if (i < materials.size() - 2) componentBuilder.append(", ").color(ChatColor.WHITE);
            else if (i == materials.size() - 2) componentBuilder.append(", and ").color(ChatColor.WHITE);
            else if (i > materials.size() - 2) componentBuilder.append(".").color(ChatColor.WHITE);
        }
        BaseComponent message = componentBuilder.build();

        Bukkit.getServer().spigot().broadcast(message);
    }



    private static @NotNull List<Material> findMaterials(@NotNull String message) {
        String[] words = message.toLowerCase().split(" ");
        Set<Material> foundMaterials = new HashSet<>(); // Use a set to avoid duplicates

        // Iterate over all possible chain lengths
        for (int chainLength = words.length; chainLength > 0; chainLength--) {
            // Generate all chains of the current length
            for (int start = 0; start <= words.length - chainLength; start++) {
                StringBuilder chain = new StringBuilder();

                // Build chain
                for (int i = start; i < start + chainLength; i++) {
                    if (i > start) {
                        chain.append("_");
                    }
                    chain.append(words[i]);
                }

                // Check for match
                Material material = Material.matchMaterial(chain.toString());
                if (material != null) {
                    foundMaterials.add(material);
                    continue;
                }

                // If no match, try removing a trailing 's'
                if (chain.toString().replace(".", "")
                        .replace("?", "").strip().endsWith("s")) {
                    String singularChain = chain.substring(0, chain.length() - 1);
                    material = Material.matchMaterial(singularChain);
                    if (material != null) {
                        foundMaterials.add(material);
                    }
                }
            }
        }

        return new ArrayList<>(foundMaterials);
    }

    public static void removeBlocksTask() {

        Set<Chunk> chunkqueue = new HashSet<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            final int iterations = WaffleControllerBlockSpeak.getInstance().getConfig().getInt("chunkIterations");
            for (int i = 0; i < iterations; i++) {
                for (int x = -1*i; x <= i; x++) {
                    for (int y = -1*i; y <= i; y++) {
                        for (int z = -1*i; z <= i; z++) {
                            chunkqueue.add(player.getLocation().add(x*16, y*16, z*16).getChunk());
                        }
                    }
                }
            }
        }

        int tick = 0;
        final int tickOffset = WaffleControllerBlockSpeak.getInstance().getConfig().getInt("chunkEditGapTicks");;

        for (Chunk chunk : chunkqueue) {
            for (Material deletedMaterial : deletedMaterials) {
                List<Chunk> chunks = materialChuckRemovedList.get(deletedMaterial);
                if (chunks.contains(chunk)) continue;
                chunks.add(chunk);
                materialChuckRemovedList.put(deletedMaterial, chunks);
                Bukkit.getScheduler().runTaskLater(WaffleControllerBlockSpeak.getInstance(), () -> {
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            for (int y = -64; y < 316; y++) {
                                Block block = chunk.getBlock(x, y, z);
                                if (block.getType() == deletedMaterial) block.setType(Material.AIR, false);
                            }
                        }
                    }
                }, tick);
                tick +=tickOffset;
            }

        }
    }

}
