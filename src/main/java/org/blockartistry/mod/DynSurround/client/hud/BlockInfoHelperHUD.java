/*
 * This file is part of Dynamic Surroundings, licensed under the MIT License (MIT).
 *
 * Copyright (c) OreCruncher
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.blockartistry.mod.DynSurround.client.hud;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.blockartistry.mod.DynSurround.ModOptions;
import org.blockartistry.mod.DynSurround.client.footsteps.implem.BlockMap;
import org.blockartistry.mod.DynSurround.client.fx.BlockEffect;
import org.blockartistry.mod.DynSurround.client.handlers.EnvironStateHandler.EnvironState;
import org.blockartistry.mod.DynSurround.registry.BlockInfo;
import org.blockartistry.mod.DynSurround.registry.BlockRegistry;
import org.blockartistry.mod.DynSurround.registry.FootstepsRegistry;
import org.blockartistry.mod.DynSurround.registry.RegistryManager;
import org.blockartistry.mod.DynSurround.registry.RegistryManager.RegistryType;
import org.blockartistry.mod.DynSurround.util.Color;
import org.blockartistry.mod.DynSurround.util.MCHelper;
import org.blockartistry.mod.DynSurround.util.WorldUtils;
import org.blockartistry.mod.DynSurround.util.gui.TextPanel;
import org.blockartistry.mod.DynSurround.util.gui.TextPanel.Reference;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

@Mod.EventBusSubscriber(Side.CLIENT)
public class BlockInfoHelperHUD extends GuiOverlay {

	private static final Color BACKGROUND_COLOR = Color.DARKSLATEGRAY;
	private static final Color FRAME_COLOR = Color.MC_WHITE;
	private static final Color TEXT_COLOR = Color.GOLD;

	private static List<String> gatherOreNames(final ItemStack stack) {
		final List<String> result = new ArrayList<String>();
		if (stack != null && !stack.isEmpty())
			for (int i : OreDictionary.getOreIDs(stack))
				result.add(OreDictionary.getOreName(i));
		return result;
	}

	private static String getItemName(final ItemStack stack) {
		final Item item = stack.getItem();
		final String itemName = MCHelper.nameOf(item);

		if (itemName != null) {
			final StringBuilder builder = new StringBuilder();
			builder.append(itemName);
			if (stack.getHasSubtypes())
				builder.append(':').append(stack.getItemDamage());
			return builder.toString();
		}

		return null;
	}

	private static List<String> gatherText(final ItemStack stack, final List<String> text, final IBlockState state,
			final BlockPos pos) {

		if (stack != null) {
			final String itemName = getItemName(stack);
			if (itemName != null) {
				text.add("ITEM: " + itemName);
				text.add(TextFormatting.DARK_AQUA + stack.getItem().getClass().getName());
			}
		}

		if (state != null) {
			final BlockInfo block = new BlockInfo(state);
			text.add("BLOCK: " + block.toString());
			text.add(TextFormatting.DARK_AQUA + block.getBlock().getClass().getName());
			text.add("Material: " + MCHelper.getMaterialName(state.getMaterial()));

			final FootstepsRegistry footsteps = RegistryManager.get(RegistryType.FOOTSTEPS);
			final BlockMap bm = footsteps.getBlockMap();
			if (bm != null) {
				final List<String> data = new ArrayList<String>();
				bm.collectData(state, pos, data);
				if (data.size() > 0) {
					text.add(TextFormatting.DARK_PURPLE + "Footstep Accoustics");
					for (final String s : data)
						text.add(TextFormatting.DARK_PURPLE + s);
				}
			}

			final BlockRegistry blocks = RegistryManager.get(RegistryType.BLOCK);
			final BlockEffect[] effects = blocks.getEffects(state);
			if (effects.length > 0) {
				text.add(TextFormatting.DARK_RED + "Block Effects");
				for (final BlockEffect e : effects) {
					text.add(TextFormatting.DARK_RED + e.getEffectType().getName());
				}
			}
		}

		final List<String> oreNames = gatherOreNames(stack);
		if (oreNames.size() > 0) {
			text.add(TextFormatting.DARK_GREEN + "Dictionary Names");
			for (final String ore : oreNames)
				text.add(TextFormatting.DARK_GREEN + ore);
		}

		return text;
	}

	private static final ItemStack tool = new ItemStack(Items.NETHER_STAR, 64);

	private static boolean isHolding() {
		final EntityPlayer player = EnvironState.getPlayer();
		return ItemStack.areItemStacksEqual(tool, player.getHeldItem(EnumHand.MAIN_HAND));
	}

	private final TextPanel textPanel;

	public BlockInfoHelperHUD() {
		this.textPanel = new TextPanel(TEXT_COLOR, BACKGROUND_COLOR, FRAME_COLOR);
	}

	@Override
	public void doRender(@Nonnull final RenderGameOverlayEvent.Pre event) {
		// Only trigger if the player is in creative and is holding a stack of
		// nether stars
		if (event.getType() == ElementType.TEXT && EnvironState.getPlayer().isCreative() && isHolding()) {

			final long tick = EnvironState.getTickCounter();
			if (tick != 0 && tick % 5 == 0) {
				final RayTraceResult current = Minecraft.getMinecraft().objectMouseOver;
				final BlockPos targetBlock = (current == null || current.getBlockPos() == null) ? BlockPos.ORIGIN
						: current.getBlockPos();
				final IBlockState state = EnvironState.getWorld().getBlockState(targetBlock);

				final List<String> data = new ArrayList<String>();
				if (!WorldUtils.isAirBlock(state)) {
					final ItemStack stack = state != null ? state.getBlock().getPickBlock(state, current,
							EnvironState.getWorld(), targetBlock, EnvironState.getPlayer()) : null;

					gatherText(stack, data, state, targetBlock);
					this.textPanel.setText(data);
				} else {
					final List<String> t = ImmutableList.of();
					this.textPanel.setText(t);
				}
			}

			final int height = event.getResolution().getScaledHeight() / 2 - 100;
			this.textPanel.render(10, height, Reference.UPPER_LEFT);
		}
	}

	@SubscribeEvent
	public static void tooltipEvent(@Nonnull final ItemTooltipEvent event) {
		if (ModOptions.enableDebugLogging) {
			final ItemStack stack = event.getItemStack();
			if (stack != null) {
				final String itemName = getItemName(stack);
				event.getToolTip().add(TextFormatting.GOLD + itemName);
				event.getToolTip().add(TextFormatting.GOLD + stack.getItem().getClass().getName());
			}
		}
	}
}