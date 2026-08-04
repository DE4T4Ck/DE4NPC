package de.de4t4ck.de4spawnnpc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class NpcListener implements Listener {
    private final JavaPlugin plugin;private final NpcManager manager;private final Map<UUID,Long> cooldown=new HashMap<>();
    public NpcListener(JavaPlugin plugin,NpcManager manager){this.plugin=plugin;this.manager=manager;}
    @EventHandler public void interact(PlayerInteractEntityEvent event){
        if(event.getHand()!=EquipmentSlot.HAND||!manager.isNpc(event.getRightClicked()))return;event.setCancelled(true);Player player=event.getPlayer();long now=System.currentTimeMillis();if(cooldown.getOrDefault(player.getUniqueId(),0L)>now)return;cooldown.put(player.getUniqueId(),now+500);
        manager.definition(event.getRightClicked()).ifPresent(npc->{String command=npc.command().replace("{player}",player.getName()).replaceFirst("^/","");Bukkit.getScheduler().runTask(plugin,()->player.performCommand(command));});
    }
    @EventHandler public void damage(EntityDamageEvent event){if(manager.isNpc(event.getEntity()))event.setCancelled(true);}
}
