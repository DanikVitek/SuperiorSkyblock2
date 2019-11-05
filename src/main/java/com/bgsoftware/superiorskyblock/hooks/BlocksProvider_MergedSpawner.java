package com.bgsoftware.superiorskyblock.hooks;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.key.Key;
import com.bgsoftware.superiorskyblock.utils.Pair;
import com.bgsoftware.superiorskyblock.utils.legacy.Materials;
import com.vk2gpz.mergedspawner.api.MergedSpawnerAPI;
import com.vk2gpz.mergedspawner.event.MergedSpawnerBreakEvent;
import com.vk2gpz.mergedspawner.event.MergedSpawnerPlaceEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public final class BlocksProvider_MergedSpawner implements BlocksProvider {

    public BlocksProvider_MergedSpawner(){
        Bukkit.getPluginManager().registerEvents(new BlocksProvider_MergedSpawner.StackerListener(), SuperiorSkyblockPlugin.getPlugin());
    }

    @Override
    public Pair<Integer, EntityType> getSpawner(Location location) {
        int blockCount = -1;
        if(Bukkit.isPrimaryThread()){
            MergedSpawnerAPI spawnerAPI = MergedSpawnerAPI.getInstance();
            blockCount = spawnerAPI.getCountFor(location.getBlock());
        }
        return new Pair<>(blockCount, null);
    }

    @Override
    public Key getSpawnerKey(ItemStack itemStack) {
        return Key.of(Materials.SPAWNER.toBukkitType() + ":" + MergedSpawnerAPI.getInstance().getEntityType(itemStack));
    }

    @SuppressWarnings("unused")
    private static class StackerListener implements Listener {

        private final SuperiorSkyblockPlugin plugin = SuperiorSkyblockPlugin.getPlugin();

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onSpawnerStack(MergedSpawnerPlaceEvent e){
            Island island = plugin.getGrid().getIslandAt(e.getSpawner().getLocation());
            if(island != null && e.getPlayer().isSneaking())
                island.handleBlockPlace(e.getSpawner().getLocation().getBlock(), e.getPlayer().getItemInHand().getAmount() - 1);
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onSpawnerUnstack(MergedSpawnerBreakEvent e){
            Island island = plugin.getGrid().getIslandAt(e.getSpawner().getLocation());
            if(island != null)
                island.handleBlockBreak(e.getSpawner().getLocation().getBlock(), e.getPlayer().isSneaking() ? e.getAmount() : 1);
        }

    }

}
