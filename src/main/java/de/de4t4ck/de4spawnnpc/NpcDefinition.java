package de.de4t4ck.de4spawnnpc;

import org.bukkit.Location;
import org.bukkit.entity.Villager;

public record NpcDefinition(String id, String name, String command, Location location,
                            Villager.Profession profession, Villager.Type type) {
}
