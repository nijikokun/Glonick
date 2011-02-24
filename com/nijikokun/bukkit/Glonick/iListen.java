package com.nijikokun.bukkit.Glonick;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * iConomy v1.x
 * Copyright (C) 2010  Nijikokun <nijikokun@gmail.com>
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

/**
 * iListen.java
 * <br /><br />
 * Listens for calls from hMod, and reacts accordingly.
 * 
 * @author Nijikokun <nijikokun@gmail.com>
 */
public class iListen extends PlayerListener {
    /**
     * Miscellaneous object for various functions that don't belong anywhere else
     */
    public Misc Misc = new Misc();

    public static Glonick plugin;

    public iListen(Glonick instance) {
        plugin = instance;
    }

    /**
     * Sends simple condensed help lines to the current player
     */
    private void showSimpleHelp() {
	Messaging.send("&e-----------------------------------------------------");
	Messaging.send("&f Glonick (&c"+Glonick.codename+"&f)                  ");
	Messaging.send("&e-----------------------------------------------------");
	Messaging.send("&f [] Required, () Optional                            ");
	Messaging.send("&e-----------------------------------------------------");
	Messaging.send("&f /nick (help|?) &6- &eShows this information.        ");
	Messaging.send("&f /nick [nickname] &6- &eReturns players real name.   ");
	Messaging.send("&f /nick [nickname] [password] &6- &eChange nicknames. ");
	Messaging.send("&f /nick register [nickname] [password]                ");
	Messaging.send("&6  - &eRegisters you an account at http://gn.nexua.org");
	Messaging.send("&6  - &eand sets your initial nickname up.             ");
	Messaging.send("&f /nick history [player]                              ");
	Messaging.send("&6  - &eReturn player nickname history.                ");
	Messaging.send("&6  - &eand sets your initial nickname up.             ");
	Messaging.send("&e-----------------------------------------------------");
    }

    private boolean register(Player player, String nickname, String password) {
	if(!Misc.validate(nickname) || nickname.equalsIgnoreCase("true") || nickname.equalsIgnoreCase("false") || nickname.equalsIgnoreCase("notch")) {
	    Messaging.send("&cNickname contains invalid characters!");
	    Messaging.send("&cAllowed characters:&f A-z0-9_.-");
	    return false;
	}

	if(plugin.Server.hasNickname(player.getName())) {
	    Messaging.send("&cYou are already registered!");
	    Messaging.send("&cTo change your name:&f /nickname [nickname] [password]");
	    return false;
	}

	if(password.isEmpty()) {
	    Messaging.send("&cPassword cannot be empty!");
	    return false;
	}

	Object result = plugin.Server.register(player.getName(), password, nickname);

	if(String.valueOf(result).equals("true")) {
	    Messaging.send("&5Successfully Registered with Glonick:&f " + nickname);
	    Messaging.send("&5Offline nickname managing:&f http://gn.nexua.org");
	    player.setDisplayName(nickname);
	    return true;
	} else {
	    Messaging.send("&c" + result);
	    return false;
	}
    }

    private boolean alter(Player player, String nickname, String password) {
	if(!Misc.validate(nickname)) {
	    Messaging.send("&cNickname contains invalid characters!");
	    Messaging.send("&cAllowed characters:&f A-z0-9_.-");
	    return false;
	}

	if(!plugin.Server.hasNickname(player.getName())) {
	    Messaging.send("&cYou must register to do that!");
	    Messaging.send("&cUsage:&f /register [nickname] [password]");
	    return false;
	}

	Object result = plugin.Server.setNickname(player.getName(), nickname, password);

	if(String.valueOf(result).equals("true")) {
	    Messaging.send("&5Updated Nickname to:&f " + nickname);
	    player.setDisplayName(nickname);
	    return true;
	} else {
	    Messaging.send("&c" + result);
	    return false;
	}
    }

    private boolean history(String name) {
	if(!Misc.validate(name)) {
	    Messaging.send("&cNickname contains invalid characters!");
	    Messaging.send("&cAllowed characters:&f A-z0-9_.-");
	    return false;
	}
	
	if(!plugin.Server.hasNickname(name)) {
	    Messaging.send("&cUser is not registered with Glonick!");
	    return false;
	}
	
	Object result = plugin.Server.getHistory(name, 5);
	
	if(result.getClass().equals(Boolean.TYPE)) {
	    Messaging.send("&cNo past nicknames!");
	    return false;
	} else {
	    Messaging.send(" ");
	    Messaging.send("&dGlonick History On: &f" + name + " &d[&f"+plugin.Server.getNickname(name)+"&d]");

	    String results = "";
	    String[] data = ((String) result).split(",");
	    int count = 1;

	    for(String nick : data) {
		results += nick + ((count == data.length) ? "" : "&d, &f");
		++count;
	    }


	    Messaging.send("&dPast 5 Nicknames: &f" + results);
	    Messaging.send(" ");

	    return false;
	}
    }

    private boolean check(String name) {
	if(!Misc.validate(name)) {
	    Messaging.send("&cName contains invalid characters!");
	    Messaging.send("&cAllowed characters:&f A-z0-9_.-");
	    return false;
	}

	Object result = plugin.Server.getName(name);

	if(result.equals("false")) {
	    Messaging.send("&cUser is not registered with Glonick!");
	    return false;
	} else {
	    Messaging.send("&dReal name for &f"+ name +" &dis &f"+ result + "&d.");

	    return false;
	}
    }

    /**
     * Commands sent from in game to us.
     *
     * @param player The player who sent the command.
     * @param split The input line split by spaces.
     * @return <code>boolean</code> - True denotes that the command existed, false the command doesn't.
     */
    @Override
    public void onPlayerCommand(PlayerChatEvent event) {
        String[] split = event.getMessage().split(" ");
        Player player = event.getPlayer();
	Messaging.save(player);
	String base = split[0];

	if(Misc.isEither(base, "/nick", "/gn")) {
	    if(split.length < 2) {
		showSimpleHelp();
		event.setCancelled(true);
		return;
	    }

	    String command = split[1];

	    if (Misc.isEither(command, "history", "-h") && split.length > 2) {
		history(split[2]); event.setCancelled(true);
		return;
	    }

	    if (Misc.isEither(command, "update", "-u") && split.length > 3) {
		alter(player, split[2], split[3]); event.setCancelled(true);
		return;
	    }

	    if (Misc.isEither(command, "register", "-r") && split.length > 3) {
		register(player, split[2], split[3]); event.setCancelled(true);
		return;
	    }

	    if (Misc.isEither(command, "help", "?") && split.length > 3) {
		showSimpleHelp(); event.setCancelled(true); return;
	    }

	    if(split.length > 2) {
		alter(player, split[1], split[2]); event.setCancelled(true);
		return;
	    }

	    if(split.length == 2) {
		check(split[1]); event.setCancelled(true);
		return;
	    }
	}
    }

    @Override
    public void onPlayerQuit(PlayerEvent event) {
	Player player = event.getPlayer();
	String name = player.getDisplayName();

	//Messaging.broadcast("&e"+name+" has left the server.");
    }

    @Override
    public void onPlayerJoin(PlayerEvent event) {
	Player player = event.getPlayer();
	String name = player.getName();

	if(plugin.Server.hasNickname(name)) {
	    player.setDisplayName(plugin.Server.getNickname(name));
	}

	//Messaging.broadcast("&e"+name+" has joined the server.");
    }
}
