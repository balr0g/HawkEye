package uk.co.oliwali.HawkEye.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

import uk.co.oliwali.HawkEye.HawkEye;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.util.BlockUtil;
import uk.co.oliwali.HawkEye.util.Config;

/**
 * Player listener class for HawkEye Tools
 * @author oliverw92
 */
public class ToolPlayerListener extends PlayerListener {
	
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getAction() == Action.LEFT_CLICK_BLOCK && BlockUtil.getItemString(player.getItemInHand()).equals(Config.ToolBlock) && HawkEye.getSession(player).isUsingTool()) {
			DataManager.toolSearch(player, event.getClickedBlock().getLocation());
			event.setCancelled(true);
		}
	}

}
