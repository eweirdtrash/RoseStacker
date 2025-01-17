package dev.rosewood.rosestacker.hook;

import de.diddiz.LogBlock.Actor;
import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;
import dev.rosewood.rosestacker.manager.ConfigurationManager.Setting;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BlockLoggingHook {

    private static Boolean coreProtectEnabled;
    private static CoreProtectAPI coreProtectAPI;

    private static Boolean logBlockEnabled;
    private static Consumer logBlockConsumer;

    /**
     * @return true if CoreProtect is enabled, false otherwise
     */
    public static boolean coreProtectEnabled() {
        if (!Setting.MISC_COREPROTECT_LOGGING.getBoolean())
            return false;

        if (coreProtectEnabled != null)
            return coreProtectEnabled;

        Plugin plugin = Bukkit.getPluginManager().getPlugin("CoreProtect");
        if (plugin != null) {
            coreProtectAPI = ((CoreProtect) plugin).getAPI();
            return coreProtectEnabled = coreProtectAPI.isEnabled();
        } else {
            return coreProtectEnabled = false;
        }
    }

    /**
     * @return true if LogBlock is enabled, false otherwise
     */
    public static boolean logBlockEnabled() {
        if (!Setting.MISC_LOGBLOCK_LOGGING.getBoolean())
            return false;

        if (logBlockEnabled != null)
            return logBlockEnabled;

        Plugin plugin = Bukkit.getPluginManager().getPlugin("LogBlock");
        if (plugin != null) {
            logBlockConsumer = ((LogBlock) plugin).getConsumer();
            return logBlockEnabled = true;
        } else {
            return logBlockEnabled = false;
        }
    }

    /**
     * Records a block place
     *
     * @param player The Player that placed the block
     * @param block The Block that was placed
     */
    public static void recordBlockPlace(Player player, Block block) {
        if (coreProtectEnabled()) {
            Material type = block.getType();
            BlockData blockData = null;

            if (type == Material.SPAWNER)
                blockData = block.getBlockData();

            coreProtectAPI.logPlacement(player.getName(), block.getLocation(), type, blockData);
        }

        if (logBlockEnabled())
            logBlockConsumer.queueBlockPlace(new Actor(player.getName(), player.getUniqueId()), block.getState());
    }

    /**
     * Records a block break
     *
     * @param player The Player that broke the block
     * @param block The Block that was broken
     */
    public static void recordBlockBreak(Player player, Block block) {
        if (coreProtectEnabled()) {
            Material type = block.getType();
            BlockData blockData = null;

            if (type == Material.SPAWNER)
                blockData = block.getBlockData();

            coreProtectAPI.logRemoval(player.getName(), block.getLocation(), type, blockData);
        }

        if (logBlockEnabled())
            logBlockConsumer.queueBlockBreak(new Actor(player.getName(), player.getUniqueId()), block.getState());
    }

}
