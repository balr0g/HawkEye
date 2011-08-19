package uk.co.oliwali.HawkEye;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Player;

import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.ContainerEntry;
import uk.co.oliwali.HawkEye.util.InventoryUtil;
import uk.co.oliwali.HawkEye.util.Util;

public class ContainerAccessManager {
	
	private List<ContainerAccess> accessList = new ArrayList<ContainerAccess>();
	
	public void checkInventoryClose(Player player) {
		
		//Get access from list
		ContainerAccess access = null;
		for (ContainerAccess acc : accessList) {
			if (acc.player == player) access = acc;
		}
		
		//If no access, return
		if (access == null) return;
		
		//Get current inventory, create diff string and add the database
		HashMap<String,Integer> after = InventoryUtil.compressInventory(InventoryUtil.getContainerContents(access.container));
		String diff = InventoryUtil.createDifferenceString(access.beforeInv, after);
		Util.info(diff);
		if (diff.length() > 1) DataManager.addEntry(new ContainerEntry(player, access.loc, diff));
		accessList.remove(access);
	}
	
	public void checkInventoryOpen(Player player, Block block) {
		if (!(block.getState() instanceof ContainerBlock)) return;
		ContainerBlock container = (ContainerBlock)(block.getState());
		accessList.add(new ContainerAccess(container, player, InventoryUtil.compressInventory(InventoryUtil.getContainerContents(container)), block.getLocation()));
	}

	public class ContainerAccess {
		public ContainerBlock container;
		public Player player;
		public HashMap<String,Integer> beforeInv;
		public Location loc;
		public ContainerAccess(ContainerBlock container, Player player, HashMap<String,Integer> beforeInv, Location loc) {
			this.container = container;
			this.player = player;
			this.beforeInv = beforeInv;
			this.loc = loc;
		}
	}

}
