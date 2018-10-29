package org.maxgamer.quickshop.Util;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.maxgamer.quickshop.QuickShop;

public abstract class ItemNMS {
    private static final String nmsVersion;
    
    private static Method craftItemStack_asNMSCopyMethod;
    private static Class<?> nbtTagCompoundClass;
    private static Method itemStack_saveMethod;
    
    static {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        nmsVersion = name.substring(name.lastIndexOf('.') + 1);
        
        try {
            craftItemStack_asNMSCopyMethod = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".inventory.CraftItemStack")
                    .getDeclaredMethod("asNMSCopy", ItemStack.class);
            
            nbtTagCompoundClass = Class.forName("net.minecraft.server." + nmsVersion + ".NBTTagCompound");
            
            itemStack_saveMethod = Class.forName("net.minecraft.server." + nmsVersion + ".ItemStack")
                    .getDeclaredMethod("save", nbtTagCompoundClass);
            
        } catch (Throwable t) {
            QuickShop.instance.getLogger().info("A error happend:");
            t.printStackTrace();
        }
    }
	
	public static String saveJsonfromNMS(ItemStack bStack) throws Throwable {
        Object mcStack = craftItemStack_asNMSCopyMethod.invoke(null, bStack);
        Object nbtTagCompound = nbtTagCompoundClass.newInstance();
        
        itemStack_saveMethod.invoke(mcStack, nbtTagCompound);
        return nbtTagCompound.toString();
	}
}
