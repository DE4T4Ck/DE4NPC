package de.de4t4ck.de4spawnnpc;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.*;

public final class NpcManager {
    private final JavaPlugin plugin;private final NpcStorage storage;private final NamespacedKey npcKey;
    private final Map<String,Villager> entities=new HashMap<>();private final Map<String,NpcDefinition> definitions=new HashMap<>();
    public NpcManager(JavaPlugin plugin){this.plugin=plugin;this.storage=new NpcStorage(plugin);this.npcKey=new NamespacedKey(plugin,"npc-id");}
    public void load(){removeTaggedEntities();entities.clear();definitions.clear();for(NpcDefinition definition:storage.loadAll())spawn(definition);}
    public void reload(){storage.reload();load();}
    public void shutdown(){for(Villager villager:entities.values())if(villager.isValid())villager.remove();entities.clear();}
    public void create(NpcDefinition definition)throws IOException{delete(definition.id());storage.save(definition);spawn(definition);}
    public boolean delete(String id)throws IOException{Villager entity=entities.remove(id.toLowerCase(Locale.ROOT));if(entity!=null&&entity.isValid())entity.remove();definitions.remove(id.toLowerCase(Locale.ROOT));return storage.delete(id.toLowerCase(Locale.ROOT));}
    public Optional<NpcDefinition> definition(String id){return Optional.ofNullable(definitions.get(id.toLowerCase(Locale.ROOT)));}
    public Collection<NpcDefinition> definitions(){return List.copyOf(definitions.values());}
    public boolean isNpc(Entity entity){return entity.getPersistentDataContainer().has(npcKey,PersistentDataType.STRING);}
    public Optional<NpcDefinition> definition(Entity entity){String id=entity.getPersistentDataContainer().get(npcKey,PersistentDataType.STRING);return id==null?Optional.empty():definition(id);}
    private void spawn(NpcDefinition definition){
        Villager villager=definition.location().getWorld().spawn(definition.location(),Villager.class,entity->{entity.setAI(false);entity.setInvulnerable(true);entity.setSilent(true);entity.setCollidable(false);entity.setRemoveWhenFarAway(false);entity.setPersistent(true);entity.setProfession(definition.profession());entity.setVillagerType(definition.type());entity.setCustomName(color(definition.name()));entity.setCustomNameVisible(true);entity.getPersistentDataContainer().set(npcKey,PersistentDataType.STRING,definition.id());});
        String id=definition.id().toLowerCase(Locale.ROOT);entities.put(id,villager);definitions.put(id,definition);
    }
    private void removeTaggedEntities(){for(World world:plugin.getServer().getWorlds())for(Entity entity:world.getEntities())if(isNpc(entity))entity.remove();}
    private String color(String value){return ChatColor.translateAlternateColorCodes('&',value);}
}
