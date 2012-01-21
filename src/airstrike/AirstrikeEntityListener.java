package airstrike;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class AirstrikeEntityListener implements Listener {
	private final Airstrike plugin;
	PluginProperties config;
	
	public AirstrikeEntityListener(Airstrike instance) {
		plugin = instance;
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		if (event.isCancelled()) {
			return;
        	}
		event.setCancelled(true);
		return; 
	}
	public void setConfig(PluginProperties config) {
		this.config = config;
	}
}
