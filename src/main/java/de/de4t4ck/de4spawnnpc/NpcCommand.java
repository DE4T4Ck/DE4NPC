package de.de4t4ck.de4spawnnpc;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class NpcCommand implements CommandExecutor, TabCompleter {
    private final NpcManager manager;
    public NpcCommand(NpcManager manager){this.manager=manager;}
    public boolean onCommand(@NotNull CommandSender sender,@NotNull Command command,@NotNull String label,@NotNull String[] args){
        if(args.length==0){help(sender);return true;}String action=args[0].toLowerCase(Locale.ROOT);
        try{return switch(action){case "create"->create(sender,args);case "delete"->delete(sender,args);case "list"->list(sender);case "reload"->reload(sender);case "tp"->teleport(sender,args);default->{help(sender);yield true;}};}catch(IOException exception){sender.sendMessage(color("&8[&6DE4NPC&8] &cDie NPC-Datei konnte nicht gespeichert werden."));return true;}
    }
    private boolean create(CommandSender sender,String[] args)throws IOException{
        if(!(sender instanceof Player player)){sender.sendMessage(color("&cDieser Befehl ist nur für Spieler."));return true;}
        if(args.length<4){sender.sendMessage(color("&7Verwendung: &e/de4npc create <ID> <Befehl> <Anzeigename>"));return true;}
        String id=args[1].toLowerCase(Locale.ROOT);if(!id.matches("[a-z0-9_-]{1,24}")){sender.sendMessage(color("&cDie ID darf nur a-z, 0-9, _ und - enthalten."));return true;}
        String npcCommand=args[2].replaceFirst("^/","");String name=String.join(" ",java.util.Arrays.copyOfRange(args,3,args.length));
        String rootCommand=npcCommand.split(" ")[0].toLowerCase(Locale.ROOT);Villager.Profession profession=switch(rootCommand){case "rtp"->Villager.Profession.CARTOGRAPHER;case "shop"->Villager.Profession.ARMORER;case "ah"->Villager.Profession.LIBRARIAN;default->Villager.Profession.NONE;};
        NpcDefinition definition=new NpcDefinition(id,name,npcCommand,player.getLocation(),profession,Villager.Type.PLAINS);manager.create(definition);sender.sendMessage(color("&8[&6DE4NPC&8] &aNPC &e"+id+" &awurde erstellt."));return true;
    }
    private boolean delete(CommandSender sender,String[] args)throws IOException{if(args.length!=2){sender.sendMessage(color("&7Verwendung: &e/de4npc delete <ID>"));return true;}if(manager.delete(args[1]))sender.sendMessage(color("&aNPC wurde gelöscht."));else sender.sendMessage(color("&cNPC wurde nicht gefunden."));return true;}
    private boolean list(CommandSender sender){sender.sendMessage(color("&8&m--------&r &6&lSpawn-NPCs &8&m--------"));if(manager.definitions().isEmpty())sender.sendMessage(color("&7Keine NPCs vorhanden."));for(NpcDefinition npc:manager.definitions())sender.sendMessage(color("&e"+npc.id()+" &8- &7/"+npc.command()+" &8- &f"+npc.name()));return true;}
    private boolean reload(CommandSender sender){manager.reload();sender.sendMessage(color("&aAlle NPCs wurden neu geladen."));return true;}
    private boolean teleport(CommandSender sender,String[] args){if(!(sender instanceof Player player)){sender.sendMessage(color("&cDieser Befehl ist nur für Spieler."));return true;}if(args.length!=2){sender.sendMessage(color("&7Verwendung: &e/de4npc tp <ID>"));return true;}NpcDefinition npc=manager.definition(args[1]).orElse(null);if(npc==null){sender.sendMessage(color("&cNPC wurde nicht gefunden."));return true;}player.teleportAsync(npc.location());return true;}
    private void help(CommandSender sender){sender.sendMessage(color("&8&m--------&r &6&lDE4SpawnNPC &8&m--------"));sender.sendMessage(color("&e/de4npc create <ID> <Befehl> <Name>"));sender.sendMessage(color("&e/de4npc delete <ID>"));sender.sendMessage(color("&e/de4npc list"));sender.sendMessage(color("&e/de4npc tp <ID>"));sender.sendMessage(color("&e/de4npc reload"));}
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,@NotNull Command command,@NotNull String alias,@NotNull String[] args){if(args.length==1)return match(List.of("create","delete","list","tp","reload"),args[0]);if(args.length==2&&(args[0].equalsIgnoreCase("delete")||args[0].equalsIgnoreCase("tp")))return match(manager.definitions().stream().map(NpcDefinition::id).toList(),args[1]);if(args.length==3&&args[0].equalsIgnoreCase("create"))return match(List.of("rtp","shop","ah","spawn","baltop","bounties"),args[2]);return List.of();}
    private List<String> match(List<String> values,String input){String start=input.toLowerCase(Locale.ROOT);List<String> result=new ArrayList<>();for(String value:values)if(value.toLowerCase(Locale.ROOT).startsWith(start))result.add(value);return result;}
    private String color(String value){return ChatColor.translateAlternateColorCodes('&',value);}
}
