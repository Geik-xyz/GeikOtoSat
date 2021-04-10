package me.geik.auto.sell.rigel.others;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SellWand {
	
	public static ItemStack sellWand(List<String> lore, Material itemmat, String name, boolean breakable) {
		List<String> newList = new ArrayList<String>();
    	for (String string : lore) {
    		newList.add(string.replace("&", "§"));}
    	ItemStack item = new ItemStack(itemmat);
    	ItemMeta meta = item.getItemMeta();
    	meta.setDisplayName(name.replace("&", "§"));
    	meta.setLore(newList);
    	if (breakable == true) meta.setUnbreakable(true);
    	item.setItemMeta(meta);
    	return item;}
}
