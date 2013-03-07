/*
 * This file is part of StarterPack.
 *
 * © 2013 AlmuraDev <http://www.almuradev.com/>
 * StarterPack is licensed under the Spout License Version 1.
 *
 * StarterPack is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * StarterPack is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package com.almuradev.starterpack;

import java.io.File;
import java.util.List;

import org.spout.api.Spout;
import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.player.PlayerJoinEvent;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.material.MaterialRegistry;

import org.spout.vanilla.component.entity.inventory.PlayerInventory;

public class StarterPackListener implements Listener {
	private final StarterPackPlugin plugin;

	public StarterPackListener(StarterPackPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		final String message = plugin.getConfig().getNode("pack_obtained_message").getString();

		try {
			plugin.getConfig().load();
		} catch (ConfigurationException e) {
			plugin.getLogger().severe(plugin.getPrefix() + "Unable to load config.yml\n" + e);
		}

		try {
			plugin.getUsers().load();
		} catch (ConfigurationException e) {
			plugin.getLogger().severe(plugin.getPrefix() + "Unable to load users.yml\n" + e);
		}

		// Get the list of users who have received their pack
		List<String> userList = plugin.getUsers().getNode("users").getStringList();
		if (player.hasPermission("starterpack.pack")) {
			// Checks if the user has received their pack
			for (String user : userList) {
				if (user.equalsIgnoreCase(player.getName())) {
					// If they are on the list, return
					return;
				}
			}

			// If users.yml doesn't exist then create it
			if (!new File(plugin.getDataFolder(), "users.yml").exists()) {
				try {
					plugin.createUsers();
				} catch (ConfigurationException e) {
					plugin.getLogger().severe(plugin.getPrefix() + "Unable to create users.yml\n" + e);
				}
			}

			// If the player isn't on the list, add them to it
			userList.add(player.getName());
			plugin.getUsers().getNode("users.").setValue(userList);
			try {
				plugin.getUsers().save();
			} catch (ConfigurationException e) {
				Spout.getLogger().severe("Unable to save StarterPack's users.yml: " + e);
			}

			// Makes sure the joining player has an inventory
			PlayerInventory inv = player.get(PlayerInventory.class);
			if (inv == null) {
				return;
			}

			// Give them their pack
			for (String itemId : plugin.getConfig().getNode("pack").getKeys(false)) {
				// Try and find the material by name first
				Material mat = MaterialRegistry.get(itemId);
				if (mat == null) {
					// Item not found by either name or id
					Spout.getLogger().severe("'" + itemId + "' was not found. Check your spelling or see if you have a missing a plugin.");
					return;
				}

				// Finally add the item
				int amount = plugin.getConfig().getNode("pack." + itemId + ".amount").getInt();
				player.get(PlayerInventory.class).add(new ItemStack(mat, amount).limitStackSize());
			}

			// Send them the message
			if (message != null && !message.isEmpty()) {
				player.sendMessage(new ChatArguments(plugin.getPrefix(), ChatStyle.CYAN, plugin.getConfig().getNode("pack_obtained_message").getValue()));
			}
		}
	}
}
