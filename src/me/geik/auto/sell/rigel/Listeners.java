package me.geik.auto.sell.rigel;

import java.io.File;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class Listeners implements Listener{
	
	@SuppressWarnings("unused")
	private Main plugin;
	public Listeners(Main plugin) {
		this.plugin = plugin;
	}
	public static File w = new File("plugins/GeikOtoSat/config.yml");
	public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(w);
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void chestListener(InventoryOpenEvent e) {
		if (e.getInventory().getHolder() instanceof Chest || e.getInventory().getHolder() instanceof DoubleChest){
			if (e.getInventory().getViewers().contains(e.getPlayer())) {
				if (e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {
				boolean check = false;
				String itemName = e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().replace("&", "§");
				double totalPara = 0;
				for (int id = 0; id < 5; id++) {
					if (cfg.isSet("cubuk." + id + ".itemName")) {
						String itemName2 = cfg.getString("cubuk." + id + ".itemName").replace("&", "§");
						if (itemName.equalsIgnoreCase(itemName2)) {
							check = true;
							break;}
					}
					else {
						break;
					}
				}
				if (check == true) {
					Player player = (Player) e.getPlayer();
					e.setCancelled(true);
					Set<String> keys = Main.fiyatlar.keySet();
					for (String key : keys) {
						if (e.getInventory().contains(Material.getMaterial(key))) {
						Material newMat = Material.getMaterial(key);
							int i = 0;
							for(ItemStack is : e.getInventory().getContents()) {
								if( is != null )
						            if( is.getType() == newMat) {
						            	ItemStack main2 = e.getPlayer().getInventory().getItemInMainHand();
						            	if (main2.getType() == null && main2.getType() == Material.AIR) return;
						            	if (main2.getItemMeta().isUnbreakable()) {
						            		i = is.getAmount();
						                    e.getInventory().remove(newMat);
						                    double ucret = Main.fiyatlar.get(key);
						                    totalPara = totalPara + i*ucret;
						            	}else {
							            	if (main2.getType().getMaxDurability() > main2.getMaxItemUseDuration()) {
								                i = is.getAmount();
								                e.getInventory().remove(newMat);
								                double ucret = Main.fiyatlar.get(key);
								                totalPara = totalPara + i*ucret;
								                if (main2.getDurability() >= main2.getType().getMaxDurability()) {
								                	player.setItemInHand(null);
								                	odul(totalPara, e, player);
								                	return;
								                } else {main2.setDurability((short) (main2.getDurability() +1));}
							               }
						            	}
						           
						        }
							}
							
					
						}
						
					}
					odul(totalPara, e, player);
				}
			}
			}
		}
		
	}
	@SuppressWarnings("deprecation")
	public void odul(double totalPara, InventoryOpenEvent e, Player player) {
		if (totalPara == 0) {
			e.getPlayer().sendMessage(Main.color("&b&lRIGEL&7&lMC &cSatýlacak bir eþya bulunamadý."));
		}
		else {
			int vergi = cfg.getInt("vergi");
			double vergiUcreti = totalPara*vergi/100;
			double newPara = totalPara - vergiUcreti;
			if (cfg.getBoolean("debug") == true) Bukkit.getConsoleSender().sendMessage(Main.color("&f&lDEBUG &a" + player.getName() + "&e " + totalPara + "&7 satýþ yaptý. Vergi dahil: &a" + newPara));
			e.getPlayer().sendMessage(Main.color("&b&lRIGEL&7&lMC &aEþyalar satýldý. Kazanç: &e" + newPara + " $"));
			OfflinePlayer hazine = Bukkit.getServer().getOfflinePlayer("Hazine");
			
			Main.econ.depositPlayer(player, newPara);
			Main.econ.depositPlayer(hazine, vergiUcreti);
			totalPara = 0;
			}
	}
	
	
}
