package de.de4t4ck.de4spawnnpc;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class DE4SpawnNPC extends JavaPlugin {
    private NpcManager manager;
    @Override public void onEnable(){saveDefaultConfig();manager=new NpcManager(this);manager.load();NpcCommand commandExecutor=new NpcCommand(manager);PluginCommand command=getCommand("de4npc");if(command==null)throw new IllegalStateException("Befehl de4npc fehlt");command.setExecutor(commandExecutor);command.setTabCompleter(commandExecutor);getServer().getPluginManager().registerEvents(new NpcListener(this,manager),this);getLogger().info("DE4SpawnNPC v"+getPluginMeta().getVersion()+" wurde aktiviert.");}
    @Override public void onDisable(){if(manager!=null)manager.shutdown();}
}
