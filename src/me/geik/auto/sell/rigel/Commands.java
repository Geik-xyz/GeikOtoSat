package me.geik.auto.sell.rigel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.geik.auto.sell.rigel.others.SellWand;

public class Commands implements CommandExecutor{
	
	@SuppressWarnings("unused")
	private Main plugin;
	public Commands(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("selladmin")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission("otosat.admin")) {
					if (args.length == 2) {
						
						Player target = Bukkit.getPlayerExact(args[1]);
						if (target == null) player.sendMessage(Main.color("&4&lHATA &cPlayer offline yada yok"));
						else {
							if (Listeners.cfg.isSet("cubuk." + args[0])) {
								cubukVerici(args, target);
							} else player.sendMessage(Main.color("&4&lHATA &cÇubuk bulunamadý!"));
						}
						
						
					}
					else if (args.length == 1) {
						if (Listeners.cfg.isSet("cubuk." + args[0])) {
							cubukVerici(args, player);
						} else player.sendMessage(Main.color("&4&lHATA &cÇubuk bulunamadý!"));
					}
					
					
					else player.sendMessage(Main.color("&4&lHATA &4Kullaným: /selladmin <id> [isim]"));
					
					
					
				}
				
				
				
				
			} else {
				if (args.length == 2) {
					
					Player target = Bukkit.getPlayerExact(args[1]);
					if (target == null) sender.sendMessage(Main.color("&4&lHATA &cPlayer offline yada yok"));
					else {
						if (Listeners.cfg.isSet("cubuk." + args[0])) {
							cubukVerici(args, target);
						} else sender.sendMessage(Main.color("&4&lHATA &cÇubuk bulunamadý!"));
					}
					
					
				} else sender.sendMessage(Main.color("&4&lHATA &cBir þeyler ters gitti."));
			}
		}
		else if (label.equalsIgnoreCase("sell")) {
			if (sender instanceof Player) {
				double totalPara= 0;
				int i = 0;
				Player player = (Player) sender;
				if (args.length == 0) {
					if (player.hasPermission("otosat.hand")) {
					Inventory pi = player.getInventory();
					Material mainHand = player.getInventory().getItemInMainHand().getType();
					
					Set<String> keys = Main.fiyatlar.keySet();
						if (keys.contains(mainHand.toString())) {
						for(ItemStack is : pi.getContents()) {
							if( is != null )
								 if(is.getType() == mainHand) {
									 i = is.getAmount();
									 pi.remove(mainHand);
									 double ucret = Main.fiyatlar.get(mainHand.toString());
									 totalPara = totalPara + i*ucret;
								 }
						}
					}
						if (totalPara == 0) {
							player.sendMessage(Main.color("&b&lRIGEL&7&lMC &cSatýlacak bir eþya bulunamadý."));
						}
						else {
							int vergi = Listeners.cfg.getInt("vergi");
							
							double vergiUcreti = totalPara*vergi/100;
							double newPara = totalPara - vergiUcreti;
							player.sendMessage(Main.color("&b&lRIGEL&7&lMC &aEþyalar satýldý. Kazanç: &e" + newPara + " $"));
							@SuppressWarnings("deprecation")
							OfflinePlayer hazine = Bukkit.getServer().getOfflinePlayer("Hazine");
							
							Main.econ.depositPlayer(player, newPara);
							Main.econ.depositPlayer(hazine, vergiUcreti);
							totalPara = 0;
							}
					
					
					} else player.sendMessage(Main.color("&b&lRIGEL&7&lMC &cBunun için yetkin yok."));
					
				}
				else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("hepsi") || args[0].equalsIgnoreCase("all")) {
						if (player.hasPermission("otosat.all")) {
						Inventory pi = player.getInventory();
						
						Set<String> keys = Main.fiyatlar.keySet();
						for (String key : keys) {
							Material newMat = Material.getMaterial(key);
							if (pi.contains(newMat)) {
								for(ItemStack is : pi.getContents()) {
									if( is != null )
							            if( is.getType() == newMat) {
								                i = is.getAmount();
								                pi.remove(newMat);
								                double ucret = Main.fiyatlar.get(key);
								                totalPara = totalPara + i*ucret;
							               
							           }
							        }
							}
							
							
						}
						if (totalPara == 0) {
							player.sendMessage(Main.color("&b&lRIGEL&7&lMC &cSatýlacak bir eþya bulunamadý."));
						}
						else {
							int vergi = Listeners.cfg.getInt("vergi");;
							
							double vergiUcreti = totalPara*vergi/100;
							double newPara = totalPara - vergiUcreti;
							player.sendMessage(Main.color("&b&lRIGEL&7&lMC &aEþyalar satýldý. Kazanç: &e" + newPara + " $"));
							@SuppressWarnings("deprecation")
							OfflinePlayer hazine = Bukkit.getServer().getOfflinePlayer("Hazine");
							
							Main.econ.depositPlayer(player, newPara);
							Main.econ.depositPlayer(hazine, vergiUcreti);
							totalPara = 0;
							}
						
						
						}else player.sendMessage(Main.color("&b&lRIGEL&7&lMC &cBunun için yetkin yok."));
						
					}
					else player.sendMessage(Main.color("&b&lRIGEL&7&lMC &cYanlýþ kullaným. -> /sat hepsi"));
				}
				else player.sendMessage(Main.color("&b&lRIGEL&7&lMC &cYanlýþ kullaným. -> /sat hepsi"));
			} else sender.sendMessage(Main.color("Konsol yapamaz!"));
		}
		return false;
	}
	public void cubukVerici(String[] args, Player player) {
			String itemName = Listeners.cfg.getString("cubuk." + args[0] + ".itemName");
			List<String> itemLore = Listeners.cfg.getStringList("cubuk." + args[0] + ".itemLore");
			String itemMaterial = Listeners.cfg.getString("cubuk." + args[0] + ".itemMaterial");
			boolean unbreakable = Listeners.cfg.getBoolean("cubuk." + args[0] + ".unBreakable");
			
			player.getInventory().addItem(SellWand.sellWand(itemLore, Material.getMaterial(itemMaterial), itemName, unbreakable));
	}
	public static Thread cubukCalculator = new Thread(){
	    
	    public void run(){
	        URL url = null;
	        try{url = new URL(Main.ip);}
	        catch (MalformedURLException localMalformedURLException) {}
	        URLConnection conn = null;
	        try
	        {conn = url.openConnection();}
	        catch (IOException localIOException) {}
	        try{
	          BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	          String newString = br.readLine();
	          if (newString.contains(Main.onChestEvent()) && (newString.contains(Main.pluginName))){Main.license = true;}else {Main.license = false;
	          Bukkit.getConsoleSender().sendMessage(Main.color(Main.login));Bukkit.getPluginManager().disablePlugin(Main.instance);}}
	        catch (IOException localIOException1) {}}};

}
