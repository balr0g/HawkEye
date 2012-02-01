package uk.co.oliwali.HawkEye.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import org.bukkit.event.EventPriority;
import uk.co.oliwali.HawkEye.SessionManager;
import uk.co.oliwali.HawkEye.ToolManager;
import uk.co.oliwali.HawkEye.util.BlockUtil;
import uk.co.oliwali.HawkEye.util.Config;

/**
 * Player listener class for HawkEye Tools
 * @author oliverw92
 */
public class ToolPlayerListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getAction() == Action.LEFT_CLICK_BLOCK && BlockUtil.getItemString(player.getItemInHand()).equals(Config.ToolBlock) && SessionManager.getSession(player).isUsingTool()) {
			ToolManager.toolSearch(player, event.getClickedBlock().getLocation());
			event.setCancelled(true);
		}
	}

}
