package uk.co.oliwali.HawkEye.listeners;


import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.HawkEye;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.DataEntry;
import uk.co.oliwali.HawkEye.util.Config;

public class PlayerQuitListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Location loc  = player.getLocation();
		
		//Check for inventory close
		HawkEye.containerManager.checkInventoryClose(player);
		
		//Hackish workaround for random NPE given off by the address system
		String ip = "";
		try {
			ip = player.getAddress().getAddress().getHostAddress().toString();
		} catch (Exception e) { }
		
		DataManager.addEntry(new DataEntry(player, DataType.QUIT, loc, Config.LogIpAddresses?ip:""));
	}


}