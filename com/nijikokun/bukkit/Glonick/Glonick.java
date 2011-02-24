package com.nijikokun.bukkit.Glonick;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * iConomy v2.0 - Official `LightWeight` Version
 * Copyright (C) 2011  Nijikokun <nijikokun@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class Glonick extends JavaPlugin {
    /*
     * Loggery Foggery
     */
    public static final Logger log = Logger.getLogger("Minecraft");

    /*
     * Central Data pertaining directly to the plugin name & versioning.
     */
    public static String name = "Glonick";
    public static String codename = "Owl";
    public static String version = "1.3";

    /**
     * Listener for the plugin system.
     */
    public iListen l = new iListen(this);

    /**
     * Controller for permissions and security.
     */
    public static iControl Watch = new iControl();

    /**
     * Things the controller needs to watch permissions for
     */
    private final String[] watching = { "enable", "disable" };

    /**
     * Default settings for the permissions
     */
    private final String[] defaults = { "*", "*" };

    /**
     * HTTP Connection, Version Check.
     */
    public static HTTP Server = new HTTP();

    /**
     * Miscellaneous object for various functions that don't belong anywhere else
     */
    public static Misc Misc = new Misc();

    /**
     * Internal Properties controllers
     */
    public static iProperty Settings, Logging;

    /*
     * Variables
     */
    public static String directory = "Glonick" + File.separator;

    private Server S = null;

    public Glonick(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);

        registerEvents();
	this.S = instance;
	log.info(Messaging.bracketize(name) + " version " + Messaging.bracketize(version) + " ("+codename+") loaded");
    }

    public void onDisable() {
	log.info(Messaging.bracketize(name) + " version " + Messaging.bracketize(version) + " ("+codename+") un-loaded");
    }

    public void onEnable() {
	
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, l, Priority.Highest, this);
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_COMMAND, l, Priority.Highest, this);
    }

    public boolean isDebugging(final Player player) {
        return false;
    }

    public void setDebugging(final Player player, final boolean value) { }

    /**
     * Setup variables, directories, and data.
     */
    public void setup() {
	for(Player player : this.S.getOnlinePlayers()) {
	    if(player == null) { continue; }
	    if(Server.hasNickname(player.getName())) {
		player.setDisplayName(Server.getNickname(name));
	    }
	}
    }

    /**
     * Setup Template
     */
    public void setupTemplate() { }

    /**
     * Setup Commands
     */
    public void setupCommands() {

    }

    /**
     * Setup Permissions that need to be watched throughout the listener.
     */
    public void setupPermissions() {
	for(int x = 0; x < watching.length; x++) {
	    Watch.add(watching[x], Settings.getString("can-" + watching[x], defaults[x]));
	}
    }
}
