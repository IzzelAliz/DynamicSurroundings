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

package org.blockartistry.lib.scanner;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

/**
 * Serves up random blocks in an area around the player. Concentration of block
 * selections are closer to the player.
 */
public abstract class RandomScanner extends Scanner {

	private int playerX;
	private int playerY;
	private int playerZ;

	public RandomScanner(@Nonnull final String name, final int range) {
		super(name, range);
	}

	public RandomScanner(@Nonnull final String name, final int range, final int blocksPerTick) {
		super(name, range, blocksPerTick);
	}

	private static int randomRange(final int range, final Random rand) {
		return rand.nextInt(range) - rand.nextInt(range);
	}

	@Override
	public void preScan() {
		final BlockPos pos = Minecraft.getMinecraft().player.getPosition();
		this.playerX = pos.getX();
		this.playerY = pos.getY();
		this.playerZ = pos.getZ();
	}

	@Override
	@Nonnull
	protected BlockPos nextPos(@Nonnull final BlockPos.MutableBlockPos workingPos, @Nonnull final Random rand) {
		return workingPos.setPos(this.playerX + randomRange(this.xRange, rand),
				this.playerY + randomRange(this.yRange, rand), this.playerZ + randomRange(this.zRange, rand));
	}

}