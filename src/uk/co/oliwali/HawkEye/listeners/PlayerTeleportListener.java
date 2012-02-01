package uk.co.oliwali.HawkEye.listeners;


import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent;
import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.HawkEye;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.DataEntry;
import uk.co.oliwali.HawkEye.util.Util;

public class PlayerTeleportListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.isCancelled()) return;
		//Check for inventory close
		HawkEye.containerManager.checkInventoryClose(event.getPlayer());
		Location from = event.getFrom();
		Location to   = event.getTo();
		if (Util.distance(from, to) > 5)
			DataManager.addEntry(new DataEntry(event.getPlayer(), DataType.TELEPORT, from, to.getWorld().getName() + ": " + to.getX() + ", " + to.getY() + ", " + to.getZ()));
	}



}