package airstrike;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Airstrike
 * @author muCkk
 */

public class Airstrike extends JavaPlugin {
	private final AirstrikeEntityListener entityListener = new AirstrikeEntityListener(this);
	private String dir = "plugins/Airstrike/";
	public static String propertiesFile = "plugins/Airstrike/Airstrike.properties";
	static PluginProperties config;
	
	@Override
	public void onDisable() {
		config.save();
	}

	@Override
	public void onEnable() {
		config = new PluginProperties(propertiesFile, dir);
		config.load();
		PluginManager pm = getServer().getPluginManager();
		if (!config.getBoolean("destroyBlocks", false)) pm.registerEvent(Event.Type.ENTITY_EXPLODE, this.entityListener, Priority.Normal, this);
		PluginDescriptionFile pdfFile = this.getDescription();
		new CommandHandler(this, config);        
	}


}
