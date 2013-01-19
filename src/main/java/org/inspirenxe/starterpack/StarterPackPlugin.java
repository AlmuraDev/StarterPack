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

import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.player.PlayerJoinEvent;
import org.spout.api.inventory.ItemStack;
import org.spout.api.map.DefaultedKey;
import org.spout.api.map.DefaultedKeyImpl;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.plugin.PluginLogger;

import org.spout.vanilla.plugin.component.inventory.PlayerInventory;
import org.spout.vanilla.plugin.material.VanillaMaterials;

public class StarterPackPlugin extends CommonPlugin implements Listener {
	private static final DefaultedKey<Boolean> JOINED_BEFORE = new DefaultedKeyImpl<Boolean>("starterpack_joined_before", false);

	@Override
	public void onEnable() {
		getEngine().getEventManager().registerEvents(this, this);
		getLogger().info(getDescription().getVersion() + " enabled.");
	}

	@Override
	public void onDisable() {
		getLogger().info("disabled.");
	}

	@Override
	public void onLoad() {
		((PluginLogger) getLogger()).setTag(new ChatArguments("[", ChatStyle.DARK_GREEN, "Starter Pack", ChatStyle.RESET, "] "));
	}

	public ChatArguments getPrefix() {
		return ((PluginLogger) getLogger()).getTag();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		if (player.getData().get(JOINED_BEFORE)) {
			return;
		}

		PlayerInventory inv = player.get(PlayerInventory.class);

		if (inv == null) {
			return;
		}

		player.getData().put(JOINED_BEFORE, true);

		if (!inv.getMain().contains(VanillaMaterials.WOODEN_SWORD) && !inv.getQuickbar().contains(VanillaMaterials.WOODEN_SWORD)) {
			inv.add(new ItemStack(VanillaMaterials.WOODEN_SWORD, 1));
		}
		if (!inv.getMain().contains(VanillaMaterials.WOODEN_PICKAXE) && !inv.getQuickbar().contains(VanillaMaterials.WOODEN_PICKAXE)) {
			inv.add(new ItemStack(VanillaMaterials.WOODEN_PICKAXE, 1));
		}
		if (!inv.getMain().contains(VanillaMaterials.WOODEN_AXE) && !inv.getQuickbar().contains(VanillaMaterials.WOODEN_AXE)) {
			inv.add(new ItemStack(VanillaMaterials.WOODEN_AXE, 1));
		}
		if (!inv.getMain().contains(VanillaMaterials.WOODEN_HOE) && !inv.getQuickbar().contains(VanillaMaterials.WOODEN_HOE)) {
			inv.add(new ItemStack(VanillaMaterials.WOODEN_HOE, 1));
		}

		player.sendMessage(new ChatArguments(getPrefix(), ChatStyle.CYAN, "Enjoy your starter pack!"));
	}
}
