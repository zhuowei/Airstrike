package airstrike;

import java.util.List;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftTNTPrimed;

import airstrike.AirstrikeSpawner.SpawnException;

import net.minecraft.server.EntityTNTPrimed;
import net.minecraft.server.World;



public class AirstrikePlayerListener extends PlayerListener {
    private final Airstrike plugin;
    private static int area;
	private static int height;
	private int amount, camount;
    private PluginProperties config;
    
    
    public AirstrikePlayerListener(Airstrike instance) {
        plugin = instance;
    }

   public void setConfig(PluginProperties config) {
    	this.config = config;
    }
}
