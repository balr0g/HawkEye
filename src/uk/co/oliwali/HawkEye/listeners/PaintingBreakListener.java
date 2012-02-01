package uk.co.oliwali.HawkEye.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import org.bukkit.event.EventPriority;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingBreakEvent.RemoveCause;
import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.DataEntry;

public class PaintingBreakListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPaintingBreak(PaintingBreakEvent event) {
		if (event.isCancelled() || event.getCause() != RemoveCause.ENTITY) return;
		PaintingBreakByEntityEvent e = (PaintingBreakByEntityEvent)event;
		if (e.getRemover() instanceof Player)
			DataManager.addEntry(new DataEntry((Player)e.getRemover(), DataType.PAINTING_BREAK, e.getPainting().getLocation(), ""));
	}




}