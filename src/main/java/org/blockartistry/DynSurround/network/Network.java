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

package org.blockartistry.DynSurround.network;

import javax.annotation.Nonnull;

import org.blockartistry.DynSurround.DSurround;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class Network {

	private static int discriminator = 0;

	private Network() {
	}

	private static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(DSurround.MOD_ID);

	public static void initialize() {

		NETWORK.registerMessage(PacketWeatherUpdate.PacketHandler.class, PacketWeatherUpdate.class, ++discriminator,
				Side.CLIENT);
		NETWORK.registerMessage(PacketHealthChange.PacketHandler.class, PacketHealthChange.class, ++discriminator,
				Side.CLIENT);
		NETWORK.registerMessage(PacketSpeechBubble.PacketHandler.class, PacketSpeechBubble.class, ++discriminator,
				Side.CLIENT);
		NETWORK.registerMessage(PacketEntityEmote.PacketHandler.class, PacketEntityEmote.class, ++discriminator,
				Side.CLIENT);
		NETWORK.registerMessage(PacketThunder.PacketHandler.class, PacketThunder.class, ++discriminator, Side.CLIENT);
		NETWORK.registerMessage(PacketEnvironment.PacketHandler.class, PacketEnvironment.class, ++discriminator,
				Side.CLIENT);
		NETWORK.registerMessage(PacketServerData.PacketHandler.class, PacketServerData.class, ++discriminator,
				Side.CLIENT);

	}

	@Nonnull
	public static TargetPoint getTargetPoint(@Nonnull final Entity entity, final double range) {
		return new TargetPoint(entity.getEntityWorld().provider.getDimension(), entity.posX, entity.posY, entity.posZ,
				range);
	}

	// Package level helper method to fire events based on incoming packets
	@SideOnly(Side.CLIENT)
	static void postEvent(@Nonnull final Event event) {
		Minecraft.getMinecraft().addScheduledTask(new Runnable() {
			public void run() {
				MinecraftForge.EVENT_BUS.post(event);
			}
		});
	}

	// Basic packet routines
	public static void sendToPlayer(@Nonnull final EntityPlayerMP player, @Nonnull final IMessage msg) {
		NETWORK.sendTo(msg, player);
	}

	public static void sendToEntityViewers(@Nonnull final Entity entity, @Nonnull final IMessage msg) {
		((WorldServer) entity.getEntityWorld()).getEntityTracker().sendToTrackingAndSelf(entity,
				NETWORK.getPacketFrom(msg));
	}

	public static void sendToDimension(final int dimensionId, @Nonnull final IMessage msg) {
		NETWORK.sendToDimension(msg, dimensionId);
	}

	public static void sendToAll(@Nonnull final IMessage msg) {
		NETWORK.sendToAll(msg);
	}

	public static void sendToAllAround(@Nonnull final TargetPoint point, @Nonnull final IMessage msg) {
		NETWORK.sendToAllAround(msg, point);
	}

}
