package uk.co.oliwali.HawkEye.listeners;


import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.HawkEye;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.DataEntry;
import uk.co.oliwali.HawkEye.entry.SimpleRollbackEntry;

public class PlayerInteractListener implements Listener {
	/**
	 * Handles several actions: 
	 * OPEN_CHEST, DOOR_INTERACT, LEVER, STONE_BUTTON, FLINT_AND_STEEL, LAVA_BUCKET, WATER_BUCKET
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		
		//Check for inventory close
		HawkEye.containerManager.checkInventoryClose(player);
		
		if (block != null) {
			
			Location loc = block.getLocation();

			switch (block.getType()) {
				case FURNACE:
				case DISPENSER:
				case CHEST:
					if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
						//Call container manager for inventory open
						HawkEye.containerManager.checkInventoryOpen(player, block);
						DataManager.addEntry(new DataEntry(player, DataType.OPEN_CONTAINER, loc, Integer.toString(block.getTypeId())));
					}
					break;
				case WOODEN_DOOR:
					DataManager.addEntry(new DataEntry(player, DataType.DOOR_INTERACT, loc, ""));
					break;
				case LEVER:
					DataManager.addEntry(new DataEntry(player, DataType.LEVER, loc, ""));
					break;
				case STONE_BUTTON:
					DataManager.addEntry(new DataEntry(player, DataType.STONE_BUTTON, loc, ""));
					break;
			}
			
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				loc = block.getRelative(event.getBlockFace()).getLocation();
				switch (player.getItemInHand().getType()) {
					case FLINT_AND_STEEL:
						DataManager.addEntry(new SimpleRollbackEntry(player, DataType.FLINT_AND_STEEL, loc, ""));
						break;
					case LAVA_BUCKET:
						DataManager.addEntry(new SimpleRollbackEntry(player, DataType.LAVA_BUCKET, loc, ""));
						break;
					case WATER_BUCKET:
						DataManager.addEntry(new SimpleRollbackEntry(player, DataType.WATER_BUCKET, loc, ""));
						break;
				}
			}
		
		}
		
	}


}