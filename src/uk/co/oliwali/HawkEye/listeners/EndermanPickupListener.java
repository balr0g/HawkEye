package uk.co.oliwali.HawkEye.listeners;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EndermanPickupEvent;
import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.BlockEntry;
import uk.co.oliwali.HawkEye.entry.SignEntry;

public class EndermanPickupListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEndermanPickup(EndermanPickupEvent event) {
		if (event.isCancelled()) return;
		Block block = event.getBlock();
		if (block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)
			DataManager.addEntry(new SignEntry("Environment", DataType.SIGN_BREAK, event.getBlock()));
		DataManager.addEntry(new BlockEntry("Environment", DataType.ENDERMAN_PICKUP, block));
	}



}