package uk.co.oliwali.HawkEye.listeners;


import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import org.bukkit.event.EventPriority;
import org.bukkit.event.painting.PaintingPlaceEvent;
import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.DataEntry;

public class PaintingPlaceListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPaintingPlace(PaintingPlaceEvent event) {
		if (event.isCancelled()) return;
		DataManager.addEntry(new DataEntry(event.getPlayer(), DataType.PAINTING_PLACE, event.getPainting().getLocation(), ""));
	}



}