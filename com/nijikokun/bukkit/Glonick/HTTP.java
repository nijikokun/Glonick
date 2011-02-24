package com.nijikokun.bukkit.Glonick;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
 * HTTP.java
 * <br /><br />
 * Currently controls any outside url connections. Currently it only checks for the latest version.
 * If a version is newer we tell the user.
 *
 * @author Nijikokun <nijikokun@gmail.com>
 */
public class HTTP {

    public HTTP() {

    }

    public String MD5(String plaintext) {
	try {
	    MessageDigest m = MessageDigest.getInstance("MD5");
	    m.reset();
	    m.update(plaintext.getBytes());
	    byte[] digest = m.digest();
	    BigInteger bigInt = new BigInteger(1, digest);
	    String hashtext = bigInt.toString(16);

	    while (hashtext.length() < 32) {
		hashtext = "0" + hashtext;
	    }

	    return hashtext;
	} catch (NoSuchAlgorithmException ex) {
	   return "";
	}
    }

    public String Fetch(String from) {
	String content = null;

	try {
	    URL url = new URL(from);
	    URLConnection urlConnection = url.openConnection();
	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

	    String line;

	    // read from the urlconnection via the bufferedreader
	    while ((line = bufferedReader.readLine()) != null) {
		content = line;
	    }

	    bufferedReader.close();
	} catch (Exception e) {
	    return null;
	}

	return content;
    }

    public boolean hasNickname(String player) {
	String content = Fetch("http://gn.nexua.org/has-nickname/" + player + "/");
	return Boolean.valueOf(content);
    }
    
    public String getNickname(String player) {
	return Fetch("http://gn.nexua.org/get-nickname/" + player + "/");
    }

    public Object setNickname(String player, String nickname, String password) {
	// Password comes from the player, no where in the plugin.
	// If you want check iListen.java for proof.
	// Secured password sending.
	String content = Fetch("http://gn.nexua.org/set-nickname/" + player + "/" + nickname + "/" + MD5(password) + "/");

	if(Boolean.valueOf(content)) {
	    return "true";
	} else {
	    return content;
	}
    }

    public Object getName(String player) {
	String content = Fetch("http://gn.nexua.org/get-name/" + player + "/");

	if(Boolean.valueOf(content)) {
	    return false;
	} else {
	    return content;
	}
    }

    public Object getHistory(String player, int amount) {
	String content = Fetch("http://gn.nexua.org/get-history/" + player + "/" + amount + "/");

	if(Boolean.valueOf(content)) {
	    return false;
	} else {
	    return content;
	}
    }

    public Object register(String player, String password, String nickname) {
	// Password comes from the player, no where in the plugin.
	// If you want check iListen.java for proof.
	// Secured password sending.
	String content = Fetch("http://gn.nexua.org/gn-register/" + player + "/" + nickname + "/" + MD5(password) + "/");

	if(Boolean.valueOf(content)) {
	    return "true";
	} else {
	    return content;
	}
    }
}
