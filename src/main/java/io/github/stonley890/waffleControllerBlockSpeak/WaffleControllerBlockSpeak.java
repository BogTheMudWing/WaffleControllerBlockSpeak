package io.github.stonley890.waffleControllerBlockSpeak;

import net.dv8tion.jda.api.managers.AudioManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class WaffleControllerBlockSpeak extends JavaPlugin {

    private static WaffleControllerBlockSpeak PLUGIN;

    public static final Map<String, String> discordToMinecraft = new HashMap<>();
    public static final Map<String, String> discordToName = new HashMap<>();
    public static final Map<String, ChatColor> discordToColor = new HashMap<>();

    @Override
    public void onEnable() {

        PLUGIN = this;

        saveDefaultConfig();

        discordToMinecraft.put("stonleyfx", "BogTheMudWing");
        discordToMinecraft.put("tetchytick", "tetchytick");
        discordToMinecraft.put("iheron", "iHeron_");
        discordToMinecraft.put("jauari", "Jauari");
        discordToMinecraft.put("ratteee", "ratttatoullie");
        discordToMinecraft.put("silverkitsuneowo", "_SilverRain");
        discordToMinecraft.put("synchrosky", "SynchroSky");
        discordToMinecraft.put("i_aint_here", "Just_Blizzard");
        discordToMinecraft.put("pyridinederg", "Piccolo");
        discordToMinecraft.put("laniebug.", "Laniebug_");
        discordToMinecraft.put("seaviper", "fffsgdfd");
        discordToMinecraft.put("over4247", "Over4247");
        discordToMinecraft.put("just_meadows", "Just_Meadows");
        discordToMinecraft.put("godzilla1005", "Godzilla1005");
        discordToMinecraft.put("thedazzlingphoenix", "PhenTheFireGirl");
        discordToMinecraft.put("peekaplay", "PeekaPlay");
        discordToMinecraft.put("fog_deity", "fog_deity");

        discordToName.put("stonleyfx", "Bog");
        discordToName.put("tetchytick", "tetchy");
        discordToName.put("iheron", "Heron");
        discordToName.put("jauari", "Jauari");
        discordToName.put("ratteee", "Goober");
        discordToName.put("silverkitsuneowo", "Silver");
        discordToName.put("synchrosky", "Sky");
        discordToName.put("i_aint_here", "Blizzard");
        discordToName.put("pyridinederg", "Piccolo");
        discordToName.put("laniebug.", "Lanie");
        discordToName.put("seaviper", "Seaviper");
        discordToName.put("over4247", "Shiny");
        discordToName.put("just_meadows", "Viper");
        discordToName.put("godzilla1005", "Zilla");
        discordToName.put("thedazzlingphoenix", "Phen");
        discordToName.put("peekaplay", "Raina");
        discordToName.put("fog_deity", "Fog");

        discordToColor.put("stonleyfx", ChatColor.of("#269FE4"));
        discordToColor.put("tetchytick", ChatColor.of("#eca138"));
        discordToColor.put("iheron", ChatColor.of("#386e6e"));
        discordToColor.put("jauari", ChatColor.of("#ffe60f"));
        discordToColor.put("ratteee", ChatColor.of("#05ff2b"));
        discordToColor.put("silverkitsuneowo", ChatColor.of("#A0FFCA"));
        discordToColor.put("synchrosky", ChatColor.of("#ffe194"));
        discordToColor.put("i_aint_here", ChatColor.WHITE);
        discordToColor.put("pyridinederg", ChatColor.of("#38FF11"));
        discordToColor.put("laniebug.", ChatColor.of("#6932c4"));
        discordToColor.put("seaviper", ChatColor.of("#8f1ba6"));
        discordToColor.put("over4247", ChatColor.of("#c096ff"));
        discordToColor.put("just_meadows", ChatColor.of("#17422f"));
        discordToColor.put("godzilla1005", ChatColor.of("#C0FFE3"));
        discordToColor.put("thedazzlingphoenix", ChatColor.of("#40ffd2"));
        discordToColor.put("peekaplay", ChatColor.of("#9b8aff"));
        discordToColor.put("fog_deity", ChatColor.of("#42505C"));

        Collection<? extends Material> storedDeletedMaterials = (Collection<? extends Material>) getConfig().get("deletedMaterials");
        if (storedDeletedMaterials != null && !storedDeletedMaterials.isEmpty()) BlockRemover.deletedMaterials.addAll(storedDeletedMaterials);

        try {
            Bot.startBot(getConfig().getString("botToken"));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            AudioManager audioManager = Bot.getAudioManager();
            if (audioManager == null || audioManager.getReceivingHandler() == null) return;
            Headphones receivingHandler = (Headphones) audioManager.getReceivingHandler();
            receivingHandler.processBufferedAudio();
        }, 0, 20 * 3);

        Bukkit.getScheduler().runTaskTimer(this, BlockRemover::removeBlocksTask, 0, 20);

        Bukkit.getPluginManager().registerEvents(new KeywordActions(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bot.getJDA().shutdownNow();
    }

    public static WaffleControllerBlockSpeak getInstance() {
        return PLUGIN;
    }

}
