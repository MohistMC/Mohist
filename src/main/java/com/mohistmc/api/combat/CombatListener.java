package com.mohistmc.api.combat;

import com.mohistmc.api.combat.events.PlayerEnterCombatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.ProjectileSource;

public class CombatListener {

	public static void callPlayerEnterCombatEvent(Player player) {
		PlayerEnterCombatEvent playerEnterCombatEvent = new PlayerEnterCombatEvent(player);
		Bukkit.getPluginManager().callEvent(playerEnterCombatEvent);
		if (!playerEnterCombatEvent.isCancelled()) {
			Cooldown.cache.put(player.getUniqueId(), System.currentTimeMillis());
		}
	}

	public static void register(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player player) {
			callPlayerEnterCombatEvent(player);
		} else if (event.getCause().equals(DamageCause.PROJECTILE) || event.getCause().equals(DamageCause.MAGIC)) {
			ProjectileSource source = ((Projectile) event.getDamager()).getShooter();
			if (source instanceof Player player) {
				callPlayerEnterCombatEvent(player);
			} else if (event.getEntity() instanceof Player player) {
				callPlayerEnterCombatEvent(player);
			}
		} else if (!(event.getDamager() instanceof Player)) {
			if (event.getEntity() instanceof Player player) {
				callPlayerEnterCombatEvent(player);
			}
		}
	}

	public static void register(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player player) {
			callPlayerEnterCombatEvent(player);
		}
	}

	public static void register(LingeringPotionSplashEvent event) {
		ProjectileSource source = event.getEntity().getShooter();
		if (source instanceof Player player) {
			if (Variables.isPotionFighting) {
				if (Variables.isLingeringFighting) {
					callPlayerEnterCombatEvent(player);
				}
			}
		}
	}

	public static void register(PotionSplashEvent event) {
		ProjectileSource source = event.getEntity().getShooter();
		if (source instanceof Player player) {
			if (Variables.isPotionFighting) {
				if (Variables.isSplashFighting) {
					callPlayerEnterCombatEvent(player);
				}
			}
		}
	}

	public static void register(PlayerItemConsumeEvent event) {
		if (Variables.isPotionFighting) {
			if (event.getItem() != null && event.getItem().hasItemMeta()) {
				if (event.getItem().getItemMeta() instanceof PotionMeta) {
					if (Variables.anyPotion) {
						Player player = event.getPlayer();
						callPlayerEnterCombatEvent(player);
					} else {
						PotionMeta potionmeta = (PotionMeta) event.getItem().getItemMeta();
						PotionType type = potionmeta.getBasePotionData().getType();

						for (String typeName : Variables.potionList) {
							PotionType checkedType = PotionType.valueOf(typeName);
							if (type == checkedType) {
								Player player = event.getPlayer();
								callPlayerEnterCombatEvent(player);
								return;
							}
						}
					}
				}
			}
		}
	}
}
