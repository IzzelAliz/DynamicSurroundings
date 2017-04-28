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

package org.blockartistry.mod.DynSurround.client.fx.particle;

import javax.annotation.Nonnull;

import org.blockartistry.mod.DynSurround.util.WorldUtils;

import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class MoteMotionBase extends MoteBase {

	protected double motionX;
	protected double motionY;
	protected double motionZ;
	protected double gravity;

	protected double prevX;
	protected double prevY;
	protected double prevZ;

	protected MoteMotionBase(@Nonnull final World world, final double x, final double y, final double z,
			final double dX, final double dY, final double dZ) {
		super(world, x, y, z);
		this.prevX = this.posX;
		this.prevY = this.posY;
		this.prevZ = this.posZ;
		this.motionX = dX;
		this.motionY = dY;
		this.motionZ = dZ;
		this.gravity = 0.06D;
	}

	@Override
	protected void update() {

		this.prevX = this.posX;
		this.prevY = this.posY;
		this.prevZ = this.posZ;
		this.motionY -= this.gravity;

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;

		this.position.setPos(this.posX, this.posY, this.posZ);

		if (WorldUtils.isSolidBlock(this.world, this.position)) {
			this.isAlive = false;
		} else {
			this.motionX *= 0.9800000190734863D;
			this.motionY *= 0.9800000190734863D;
			this.motionZ *= 0.9800000190734863D;
		}
	}

}