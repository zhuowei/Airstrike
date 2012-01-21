package airstrike;

import java.util.List;
import java.util.Random;

import java.lang.reflect.Field;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Wolf;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import org.bukkit.craftbukkit.CraftWorld;

import net.minecraft.server.EntityPotion;

public class CommandHandler implements CommandExecutor {
	private Airstrike plugin;
	private PluginProperties config;
	
	private static int area;
	private static int height;
	private int amount, camount;
	
	
	public CommandHandler(Airstrike instance, PluginProperties config) {
		plugin = instance;
		plugin.getCommand("as").setExecutor(this);
		plugin.getCommand("asset").setExecutor(this);
		this.config = config;
	}
	private boolean checkPermission(CommandSender sender, String node) {
		return sender.hasPermission(node);
	}
	private void sendMSG(CommandSender sender, String msg) {
		CommandSender player = sender;
		if(msg.equalsIgnoreCase("help")) {
			sender.sendMessage("For help, type /as.");
			return;
		}
		if(msg.equalsIgnoreCase("notfound")) {
			player.sendMessage(ChatColor.GOLD+"Player not found!");
			return;
		}
		if(msg.equalsIgnoreCase("morefound")) {
			player.sendMessage(ChatColor.GOLD+"Found more than one Player!");
			return;
		}
		if(msg.equalsIgnoreCase("assethelp")) {
			player.sendMessage(ChatColor.LIGHT_PURPLE+"/asset adminsOnly <true/false>");	
	    		player.sendMessage(ChatColor.LIGHT_PURPLE+"/asset destroyBlocks <true/false>");
	    		player.sendMessage(ChatColor.LIGHT_PURPLE+"/asset creeperDistance <number>");
	    		player.sendMessage(ChatColor.LIGHT_PURPLE+"/asset creeperAmount <number>");
	    		player.sendMessage(ChatColor.LIGHT_PURPLE+"/asset TNTAmount <number>");
	    		player.sendMessage(ChatColor.LIGHT_PURPLE+"/asset height <number>");
	    		player.sendMessage(ChatColor.LIGHT_PURPLE+"/asset area <number>");
	    		player.sendMessage(ChatColor.LIGHT_PURPLE+"/asset arrowAmount <number>");
    			return;
		}
		if(msg.equalsIgnoreCase("valuedb")) {
			player.sendMessage(ChatColor.LIGHT_PURPLE+"Value set. Type '/reload' for the change to take effect (will reload all plugins).");
			return;
		}
		if(msg.equalsIgnoreCase("value")) {
			player.sendMessage(ChatColor.LIGHT_PURPLE+"Value set.");
			return;
		}
	}
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

		String cmd = command.getName().toLowerCase();
		final Server server = plugin.getServer();

		if (cmd.equals("as")) {

			final String args0;
			
			if (args.length < 2) {
				return false;
			}

			args0 = args[0];

			if (args0.equals("help")) {
				return false;
			}

    			final List<Player> victims = server.matchPlayer(args[1]);
        		if (victims.size() < 1) {
        			sendMSG(sender, "notfound");
            			return true;
        		}
        		if (victims.size() > 1) {
        			sendMSG(sender, "morefound");
            			return true;
        		}
    			final Player victim  = victims.get(0);
    			final Location loc = victim.getLocation();
    			final Random rg = new Random();
    			
    			// Creeper
    			
    			if (args0.equalsIgnoreCase("cr") && checkPermission(sender, "airstrike.as.cr")) {
    				final int distance = config.getInteger("creeperDistance", PluginProperties.creeperDistance);
    				if(args.length>2) camount = Integer.valueOf(args[2]); 
    				else camount = config.getInteger("creeperAmount", PluginProperties.creeperAmount);
    				new Thread()  {
    					@Override public void run() {
    						setPriority( Thread.MIN_PRIORITY );
    						for (int i=0; i<camount; i++) {  		
    							int roll = (rg.nextBoolean() ? distance : -distance);
	    		    				loc.setX( loc.getX() + (roll) );
	    		    				roll = (rg.nextBoolean() ? distance : -distance);
	    		        			loc.setZ( loc.getZ() + (roll) );
    							Creeper creep = victim.getWorld().spawn(loc, Creeper.class);
    							try {
    		        					sleep(500);
    		        				} catch (InterruptedException e) {
    							}
    		    				}
    					}
    				}.start();
    				return true;         				
    			}
    			
    			// TNT
    			if (args0.equalsIgnoreCase("tnt") && checkPermission(sender, "airstrike.as.tnt")) {
    				if(args.length>2) amount = Integer.valueOf(args[2]); 
    				else amount = config.getInteger("TNTAmount", PluginProperties.TNTAmount);
    				
    				if(args.length>3) height = Integer.valueOf(args[3]);
    				else height = config.getInteger("height", PluginProperties.height);
    				area = config.getInteger("area", PluginProperties.area);
    				new Thread()  {
    					@Override 
    					public void run() {
    						setPriority( Thread.MIN_PRIORITY );
    						for (int i = 0; i < amount; i++) {  
	    		        			Location loc = victims.get(0).getLocation();
	    		        			loc.setY(loc.getY()+height);
	    		        			loc.setX( loc.getX() + (rg.nextInt((2*area)+1)-area) );
	    		        			loc.setZ( loc.getZ() + (rg.nextInt((2*area)+1)-area) );
		    		        		TNTPrimed tntp = victim.getWorld().spawn(loc, TNTPrimed.class);
	    		        			try {
	    		        				sleep(500);
	    		        			} catch (InterruptedException e) {
	    						}
    		    				}
    					}
    				}.start();
    				return true;
    			}

			//arrows
    			if (args0.equalsIgnoreCase("arrow") && checkPermission(sender, "airstrike.as.arrow")) {
    				if(args.length>2) amount = Integer.valueOf(args[2]); 
    				else amount = config.getInteger("arrowAmount", PluginProperties.arrowAmount);
    				area = config.getInteger("area", PluginProperties.area);
    				new Thread()  {
    					@Override 
    					public void run() {
    						setPriority( Thread.MIN_PRIORITY );
    						for (int i = 0; i < amount; i++) {  
	    		        			Location loc = victim.getLocation();
	    		        			loc.setY(loc.getY() + 15);
	    		        			loc.setX( loc.getX() + (rg.nextInt((2*area)+1)-area) );
	    		        			loc.setZ( loc.getZ() + (rg.nextInt((2*area)+1)-area) );
							loc.setPitch(-90);
							Vector vec = new Vector(0, -1, 0);
		    		        		Arrow arrow = victim.getWorld().spawnArrow(loc, vec, 0.6f, 12f);
							arrow.setFireTicks(500);
							if (i % 5 == 0) {
		    		        			try {
		    		        				sleep(300);
		    		        			}
								catch (InterruptedException e) {
		    						}
							}
    		    				}
    					}
    				}.start();
    				return true;
    			}

			//Wolves
    			if (args0.equalsIgnoreCase("wolf") && checkPermission(sender, "airstrike.as.wolf")) {
    				final int distance = config.getInteger("creeperDistance", PluginProperties.creeperDistance);
    				if(args.length>2) camount = Integer.valueOf(args[2]); 
    				else camount = config.getInteger("wolfAmount", PluginProperties.wolfAmount);
    				new Thread()  {
    					@Override public void run() {
    						setPriority( Thread.MIN_PRIORITY );
    						for (int i=0; i<camount; i++) {  		
    							int roll = (rg.nextBoolean() ? distance : -distance);
	    		    				loc.setX( loc.getX() + (roll) );
	    		    				roll = (rg.nextBoolean() ? distance : -distance);
	    		        			loc.setZ( loc.getZ() + (roll) );
							loc.setY(victim.getWorld().getHighestBlockAt(loc).getY() + 0.5);
    							Wolf wolf = victim.getWorld().spawn(loc, Wolf.class);
							wolf.setAngry(true);
							wolf.setTarget(victim);
    							try {
    		        					sleep(500);
    		        				} catch (InterruptedException e) {
    							}
    		    				}
    					}
    				}.start();
    				return true;         				
    			}

			//Reverse Airstrike
    			if (args0.equalsIgnoreCase("reverse") && checkPermission(sender, "airstrike.as.reverse")) {
				double verticalAccel = 3.5;
				if (args.length > 2) {
					verticalAccel = Double.valueOf(args[2]);
				}
				Location victimLoc = victim.getLocation();

				victimLoc.setY(victim.getWorld().getHighestBlockAt(victimLoc).getY() + 0.5);

				victim.teleport(victimLoc);
    				victim.setVelocity(new Vector(0, verticalAccel, 0)); //Spaaaaaaacce
    				return true;         				
    			}

			//Potions
			if (args0.equalsIgnoreCase("potion") && checkPermission(sender, "airstrike.as.potion")) {
				final int pamount;
				if (args.length > 2) {
					pamount = Integer.parseInt(args[2]);
				} else {
					pamount = config.getInteger("potionAmount", PluginProperties.potionAmount);
				}
				final short ptype;
				if (args.length > 3) {
					ptype = Short.parseShort(args[3]);
				} else {
					ptype = (short) 4; //Potion of poison
				}
				server.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public int count = pamount;
					public void run() {
						Location loc = victim.getLocation();
						loc.setY(loc.getY() + 20);
						loc.setX(loc.getX() + (rg.nextInt((2*area)+1)-area));
    		        			loc.setZ(loc.getZ() + (rg.nextInt((2*area)+1)-area));
						try {
							CraftWorld cWorld = (CraftWorld) victim.getWorld();
							EntityPotion nmspotion = new EntityPotion(cWorld.getHandle(), loc.getX(), loc.getY(), loc.getZ(), ptype);
							cWorld.getHandle().addEntity(nmspotion);
						} catch(Throwable t) {
							System.err.println("[MoreAirstrike] Failed to spawn potion: You may want to check for updates for Airstrike.");
							t.printStackTrace();
						}
						count--;
						if (count > 0) {
							server.getScheduler().scheduleSyncDelayedTask(plugin, this, 10);
						}
					}
				}, 0);
			}

			sender.sendMessage(ChatColor.RED + "Error executing Airstrike. Do you have permission?");
    			return false;
    		}
		
	if(cmd.equals("asset")) {
        	if(!checkPermission(sender, "airstrike.asset")) {
        		sender.sendMessage(ChatColor.RED +"Only Admins can use this command!");
        		return true;
        	}
        	if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
        		sendMSG(sender, "assethelp");
        		return true;
        	}
        	
        	String value;
        	try {
        		value = args[1];
        	} catch(ArrayIndexOutOfBoundsException ex) {
        		sender.sendMessage(ChatColor.LIGHT_PURPLE+"Wrong Input");
        		return true;
        	}
        	
        	if(args[0].equalsIgnoreCase("destroyBlocks")) {
        		config.setProperty("destroyBlocks", value);
        		sendMSG(sender, "valuedb");
        		return true;
        	}
        	if(args[0].equalsIgnoreCase("creeperDistance")) {
        		config.setProperty("creeperDistance", value);
        		sendMSG(sender, "value");
        		return true;
        	}
        	if(args[0].equalsIgnoreCase("creeperAmount")) {
        		config.setProperty("creeperAmount", value);
        		sendMSG(sender, "value");
        		return true;
        	}
		if(args[0].equalsIgnoreCase("TNTAmount")) {
			config.setProperty("TNTAmount", value);
			sendMSG(sender, "value");
			return true;
		}
		if(args[0].equalsIgnoreCase("height")) {
			config.setProperty("height", value);
			sendMSG(sender, "value");
			return true;
		}
		if(args[0].equalsIgnoreCase("area")) {
			config.setProperty("area", value);
			sendMSG(sender, "value");
			return true;
		}
		if(args[0].equalsIgnoreCase("adminsOnly")) {
			config.setProperty("adminsOnly", value);
			sendMSG(sender, "value");
			return true;
		}
		if(args[0].equalsIgnoreCase("arrowAmount")) {
			config.setProperty("arrowAmount", value);
			sendMSG(sender, "value");
			return true;
		}
        	if(args[0].equalsIgnoreCase("wolfAmount")) {
        		config.setProperty("wolfAmount", value);
        		sendMSG(sender, "value");
        		return true;
        	}
        }
		return false;      	
    }	
}
