package com.pezapp.relicbuild.Admins;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.pezapp.relicbuild.CommandManager;
import com.pezapp.relicbuild.Plugin;

public class CounselorLightning implements Listener {
	
	public Plugin plugin;
    public CounselorLightning(Plugin instance)
    {
        plugin = instance;
    }
    
    @EventHandler
	public void PlayerInteractEvent(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player eventPlayer = event.getPlayer();
			    ItemStack attackItem = eventPlayer.getInventory().getItemInMainHand();
			    
			if (attackItem.getType() == Material.GHAST_TEAR) {
				if (event.getPlayer().isOp() || CommandManager.grantedOps.containsKey(event.getPlayer().getName())) { // Still need to add perm levels
	    			SpectralArrow s = event.getPlayer().launchProjectile(SpectralArrow.class);
	    			s.setFireTicks(20);
	    			//s.setGravity(false); // Not sure about this
				}
			}
		}
	}
    
    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event) {
	    if (event.getDamager() instanceof SpectralArrow) {
			//shooter = ((Arrow)event.getDamager()).getShooter(); // shooter
			event.getEntity().getWorld().strikeLightning(event.getEntity().getLocation());
		}
    }
}
