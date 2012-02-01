package uk.co.oliwali.HawkEye.listeners;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Enderman;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EndermanPickupEvent;
import org.bukkit.event.entity.EndermanPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;
import org.bukkit.event.painting.PaintingBreakEvent.RemoveCause;
import org.bukkit.inventory.ItemStack;

import uk.co.oliwali.HawkEye.HawkEye;
import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.PlayerSession;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.BlockChangeEntry;
import uk.co.oliwali.HawkEye.entry.BlockEntry;
import uk.co.oliwali.HawkEye.entry.DataEntry;
import uk.co.oliwali.HawkEye.entry.SignEntry;
import uk.co.oliwali.HawkEye.util.Config;
import uk.co.oliwali.HawkEye.util.Util;
import uk.co.oliwali.HawkEye.util.Config;

/**
 * Entity listener class for HawkEye
 * Contains system for managing player deaths
 * @author oliverw92
 */
public class MonitorEntityListener implements Listener {
	
	public HawkEye plugin;

	public MonitorEntityListener(HawkEye HawkEye) {
		plugin = HawkEye;
	}
	
	/**
	 * Uses the lastAttacker field in the players {@link PlayerSession} to log the death and cause
	 */
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (!(Config.isLogged(DataType.PVP_DEATH) && Config.isLogged(DataType.MOB_DEATH) && Config.isLogged(DataType.OTHER_DEATH))) return;
		Entity entity = event.getEntity();
		//Only interested if it is a player death
		if (entity instanceof Player) {
			
			Player victim = (Player) entity;
			
			//Mob or PVP death
			if (victim.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
				Entity damager = ((EntityDamageByEntityEvent)(victim.getLastDamageCause())).getDamager();
				if (damager instanceof Player) {
					DataManager.addEntry(new DataEntry(victim, DataType.PVP_DEATH, victim.getLocation(), Util.getEntityName(damager)));
				} else {
					DataManager.addEntry(new DataEntry(victim, DataType.MOB_DEATH, victim.getLocation(), Util.getEntityName(damager)));
				}
			//Other death
			} else {
				EntityDamageEvent dEvent = victim.getLastDamageCause();
				String cause = dEvent == null?"Unknown":victim.getLastDamageCause().getCause().name();
				String[] words = cause.split("_");
				for (int i = 0; i < words.length; i++)
					words[i] = words[i].substring(0,1).toUpperCase() + words[i].substring(1).toLowerCase();
				cause = Util.join(Arrays.asList(words), " ");
				DataManager.addEntry(new DataEntry(victim, DataType.OTHER_DEATH, victim.getLocation(), cause));
			}
            
			//Log item drops
			if (Config.LogDeathDrops) {
				String data = null;
				for (ItemStack stack : event.getDrops()) {
					if (stack.getData() != null)
						data = stack.getAmount() + "x " + stack.getTypeId() + ":" + stack.getData().getData();
				    else
				    	data = stack.getAmount() + "x " + stack.getTypeId();
				    DataManager.addEntry(new DataEntry(victim, DataType.ITEM_DROP, victim.getLocation(), data));                           
				}
			}
	
		}
	}
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		if (event.isCancelled() || !(Config.isLogged(DataType.EXPLOSION))) return;
		for (Block b : event.blockList().toArray(new Block[0]))
			DataManager.addEntry(new BlockEntry("Environment", DataType.EXPLOSION, b));
	}
	@EventHandler
	public void onPaintingBreak(PaintingBreakEvent event) {
		if (event.isCancelled() || !(Config.isLogged(DataType.EXPLOSION)) || event.getCause() != RemoveCause.ENTITY) return;
		PaintingBreakByEntityEvent e = (PaintingBreakByEntityEvent)event;
		if (e.getRemover() instanceof Player)
			DataManager.addEntry(new DataEntry((Player)e.getRemover(), DataType.PAINTING_BREAK, e.getPainting().getLocation(), ""));
	}
	@EventHandler
	public void onPaintingPlace(PaintingPlaceEvent event) {
		if (event.isCancelled() || !(Config.isLogged(DataType.PAINTING_PLACE))) return;
		DataManager.addEntry(new DataEntry(event.getPlayer(), DataType.PAINTING_PLACE, event.getPainting().getLocation(), ""));
	}
	@EventHandler
	public void onEndermanPickup(EndermanPickupEvent event) {
		if (event.isCancelled() || !(Config.isLogged(DataType.ENDERMAN_PICKUP))) return;
		Block block = event.getBlock();
		if (block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)
			DataManager.addEntry(new SignEntry("Environment", DataType.SIGN_BREAK, event.getBlock()));
		DataManager.addEntry(new BlockEntry("Environment", DataType.ENDERMAN_PICKUP, block));
	}
	@EventHandler
	public void onEndermanPlace(EndermanPlaceEvent event) {
		if (event.isCancelled() || !(Config.isLogged(DataType.ENDERMAN_PLACE))) return;
		
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
