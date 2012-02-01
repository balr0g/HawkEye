package uk.co.oliwali.HawkEye.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.HawkEye;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.DataEntry;
import uk.co.oliwali.HawkEye.util.Config;

public class PlayerCommandListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

		if (event.isCancelled()) return;
		Player player = event.getPlayer();
		//Check for inventory close
		HawkEye.containerManager.checkInventoryClose(player);
		//Check command filter
		if (Config.CommandFilter.contains(event.getMessage().split(" ")[0])) return;
		DataManager.addEntry(new DataEntry(player, DataType.COMMAND, player.getLocation(), event.getMessage()));
	}



}