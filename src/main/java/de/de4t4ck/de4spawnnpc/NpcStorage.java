package de.de4t4ck.de4spawnnpc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class NpcStorage {
    private final File file; private YamlConfiguration yaml;
    public NpcStorage(JavaPlugin plugin){this.file=new File(plugin.getDataFolder(),"npcs.yml");reload();}
    public void reload(){this.yaml=YamlConfiguration.loadConfiguration(file);}
    public List<NpcDefinition> loadAll(){
        List<NpcDefinition> result=new ArrayList<>();ConfigurationSection root=yaml.getConfigurationSection("npcs");if(root==null)return result;
        for(String id:root.getKeys(false)){String base="npcs."+id+".";World world=Bukkit.getWorld(yaml.getString(base+"world",""));if(world==null)continue;
            Location location=new Location(world,yaml.getDouble(base+"x"),yaml.getDouble(base+"y"),yaml.getDouble(base+"z"),(float)yaml.getDouble(base+"yaw"),(float)yaml.getDouble(base+"pitch"));
            try{Villager.Profession profession=Villager.Profession.valueOf(yaml.getString(base+"profession","NONE").toUpperCase(Locale.ROOT));Villager.Type type=Villager.Type.valueOf(yaml.getString(base+"type","PLAINS").toUpperCase(Locale.ROOT));result.add(new NpcDefinition(id,yaml.getString(base+"name","&e"+id),yaml.getString(base+"command",id),location,profession,type));}catch(IllegalArgumentException ignored){}
        }return result;
    }
    public void save(NpcDefinition npc)throws IOException{String base="npcs."+npc.id()+".";yaml.set(base+"name",npc.name());yaml.set(base+"command",npc.command());yaml.set(base+"world",npc.location().getWorld().getName());yaml.set(base+"x",npc.location().getX());yaml.set(base+"y",npc.location().getY());yaml.set(base+"z",npc.location().getZ());yaml.set(base+"yaw",npc.location().getYaw());yaml.set(base+"pitch",npc.location().getPitch());yaml.set(base+"profession",npc.profession().name());yaml.set(base+"type",npc.type().name());yaml.save(file);}
    public boolean delete(String id)throws IOException{if(!yaml.contains("npcs."+id))return false;yaml.set("npcs."+id,null);yaml.save(file);return true;}
}
