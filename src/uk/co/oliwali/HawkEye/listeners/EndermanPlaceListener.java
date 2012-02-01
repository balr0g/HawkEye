package uk.co.oliwali.HawkEye.listeners;


import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Enderman;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EndermanPlaceEvent;
import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.BlockChangeEntry;

public class EndermanPlaceListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEndermanPlace(EndermanPlaceEvent event) {
		if (event.isCancelled()) return;
		
		//Get the enderman and the block being replaced
		Enderman enderman = (Enderman) event.getEntity();
		Block block = enderman.getWorld().getBlockAt(event.getLocation());
		
		//Create a new state for the block
		BlockState newState = block.getState();
		if (enderman.getCarriedMaterial() != null) {
			try {
				newState.setData(enderman.getCarriedMaterial());
			} catch (Exception e) { }
			newState.setType(enderman.getCarriedMaterial().getItemType());
		}
		
		DataManager.addEntry(new BlockChangeEntry("Environment", DataType.ENDERMAN_PLACE, event.getLocation(), block.getState(), newState));
	}




}