package com.bgsoftware.superiorskyblock.api.handlers;

import com.bgsoftware.superiorskyblock.api.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface KeysManager {

    boolean isSpawner(Material type);

    Key getSpawnerKey(ItemStack itemStack);

}
