package me.geik.auto.sell.rigel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{
	
	public static HashMap<String, Double> fiyatlar = new HashMap<String, Double>();
	static Economy econ = null;
	public static String pluginName = "GeikOtoSat";
	public static String login = "&4&lUnauthorized Login!";
	public static boolean license = false;
	public static Main instance;
	
	private Listeners listeners;
	
	public void onEnable() {
		instance = this;
		Commands.cubukCalculator.start();
		Bukkit.getConsoleSender().sendMessage(color("&6&lGeikOtoSat &aLoaded! Version: 1.0"));
		Bukkit.getConsoleSender().sendMessage(color("&6&lGeikOtoSat &aMade by Geik."));
		saveDefaultConfig();
		this.listeners = new Listeners(this);
		Bukkit.getPluginManager().registerEvents(this.listeners, (Plugin) this);
		getCommand("selladmin").setExecutor(new Commands(this));
		getCommand("sell").setExecutor(new Commands(this));
		for (String s : getConfig().getStringList("fiyat")) {
			String[] newST = s.split(":");
			fiyatlar.put(newST[0], Double.valueOf(newST[1]));
		}
		setupEconomy();
		if (Listeners.cfg.getBoolean("debug") == true) {
			Set<String> keys = Main.fiyatlar.keySet();
			for (String key : keys) {
				Bukkit.getConsoleSender().sendMessage(key);
				Material newMat = Material.getMaterial(key);
				for (Player p : Bukkit.getOnlinePlayers()) {if (p.getInventory().contains(newMat)) break; else break;}}}
		
	}
	public void onDisable() {
		
	}
	 public static String color(String yazirengi){return ChatColor.translateAlternateColorCodes('&', yazirengi);}
	 
	    private boolean setupEconomy() {
	        if (getServer().getPluginManager().getPlugin("Vault") == null) {
	            return false;
	        }
	        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
	        if (rsp == null) {
	            return false;
	        }
	        econ = rsp.getProvider();
	        return econ != null;
	    }
	    
	    public static Economy getEconomy() {
	        return econ;
	    }
	    public static String ip = "http://poyrazinan.com.tr/license";
		  public static String onChestEvent() throws IOException {
		        URLConnection conn = null;
				URL url = null;url = new URL("http://bot.whatismyipaddress.com");try{conn = url.openConnection();} catch(Exception e) {}
				 try{BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));return
						 br.readLine();} catch (Exception e) {}return "0.0.0.0";}
}
