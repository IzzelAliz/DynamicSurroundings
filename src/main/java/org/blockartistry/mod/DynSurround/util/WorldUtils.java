/* This file is part of Dynamic Surroundings, licensed under the MIT License (MIT).
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

package org.blockartistry.mod.DynSurround.util;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class WorldUtils {

	private static final BlockStateProvider blockProvider = new BlockStateProvider();

	private WorldUtils() {

	}

	@Nullable
	public static Entity locateEntity(@Nonnull final World world, @Nonnull final UUID entityId) {
		for (final Entity e : world.getLoadedEntityList())
			if (e.getUniqueID().equals(entityId))
				return e;
		return null;
	}

	public static boolean isSolidBlock(@Nonnull final World world, @Nonnull final BlockPos pos) {
		return getBlockState(world, pos).getMaterial().isSolid();
	}

	public static boolean isSolidBlock(@Nonnull final World world, final int x, final int y, final int z) {
		return getBlockState(world, x, y, z).getMaterial().isSolid();
	}

	public static boolean isAirBlock(@Nonnull final IBlockState state) {
		return state.getBlock() == Blocks.AIR;
	}

	public static boolean isLeaves(@Nonnull final IBlockState state) {
		return state.getMaterial() == Material.LEAVES;
	}

	public static boolean isAirBlock(@Nonnull final World world, @Nonnull final BlockPos pos) {
		return isAirBlock(getBlockState(world, pos));
	}

	public static boolean isAirBlock(@Nonnull final World world, final int x, final int y, final int z) {
		return isAirBlock(getBlockState(world, x, y, z));
	}

	@Nonnull
	public static IBlockState getBlockState(@Nonnull final World world, @Nonnull final BlockPos pos) {
		return blockProvider.setWorld(world).getBlockState(pos);
	}

	@Nonnull
	public static IBlockState getBlockState(@Nonnull final World world, final int x, final int y, final int z) {
		return blockProvider.setWorld(world).getBlockState(x, y, z);
	}

}
