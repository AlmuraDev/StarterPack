/*
 * This file is part of StarterPack.
 *
 * © 2013 InspireNXE <http://www.inspirenxe.org/>
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
package org.inspirenxe.starterpack;

import java.io.File;

import org.spout.api.Spout;
import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.plugin.PluginLogger;
import org.spout.api.util.config.yaml.YamlConfiguration;

public class StarterPackPlugin extends CommonPlugin {
	private YamlConfiguration config;
	private YamlConfiguration users;

	@Override
	public void onEnable() {
		config = new YamlConfiguration(new File(getDataFolder(), "config.yml"));
		users = new YamlConfiguration(new File(getDataFolder(), "users.yml"));

		try {
			// If the config.yml doesn't exist then create it
			if (!new File(getDataFolder(), "config.yml").exists()) {
				createConfig();
			} else {
				// Load config.yml
				config.load();
			}

			// If the users.yml doesn't exist then create it
			if (!new File(getDataFolder(), "users.yml").exists()) {
				createUsers();
			} else {
				// Load users.yml
				users.load();
			}
		} catch (ConfigurationException e) {
			getLogger().severe("Go nag Grinch:\n" + e);
		}

		Spout.getEventManager().registerEvents(new StarterPackListener(this), this);
		getLogger().info(getDescription().getVersion() + " enabled.");
	}

	@Override
	public void onDisable() {
		getLogger().info(getDescription().getVersion() + " disabled.");
	}

	@Override
	public void onLoad() {
		((PluginLogger) getLogger()).setTag(new ChatArguments("[", ChatStyle.DARK_GREEN, "Starter Pack", ChatStyle.RESET, "] "));
	}

	public ChatArguments getPrefix() {
		return ((PluginLogger) getLogger()).getTag();
	}

	public YamlConfiguration getUsers() {
		return users;
	}

	public YamlConfiguration getConfig() {
		return config;
	}

	public void createConfig() throws ConfigurationException {
		config.load();
		config.addNode("pack_obtained_message").setValue("Enjoy your starter pack!");
		config.addNode("pack");
		config.save();
	}

	public void createUsers() throws ConfigurationException {
		users.load();
		users.addNode("users");
		users.save();
	}
}
