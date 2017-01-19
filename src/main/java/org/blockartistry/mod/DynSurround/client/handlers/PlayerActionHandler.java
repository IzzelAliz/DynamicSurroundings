package org.blockartistry.mod.DynSurround.client.handlers;

import javax.annotation.Nonnull;

import org.blockartistry.mod.DynSurround.ModOptions;
import org.blockartistry.mod.DynSurround.client.handlers.EnvironStateHandler.EnvironState;
import org.blockartistry.mod.DynSurround.client.sound.SoundEffect;
import org.blockartistry.mod.DynSurround.registry.ItemRegistry;
import org.blockartistry.mod.DynSurround.registry.RegistryManager;
import org.blockartistry.mod.DynSurround.registry.RegistryManager.RegistryType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerActionHandler extends EffectHandlerBase {

	private static final SoundEffect JUMP = new SoundEffect("jump", SoundCategory.PLAYERS).setVariable(true);
	private static final SoundEffect CRAFTING = new SoundEffect("crafting", SoundCategory.PLAYERS);
	private static final SoundEffect SWORD = new SoundEffect("swoosh", SoundCategory.PLAYERS);
	private static final SoundEffect AXE = new SoundEffect("swoosh", SoundCategory.PLAYERS).setPitch(0.5F);
	private static final SoundEffect BOW_PULL = new SoundEffect("bowpull", SoundCategory.PLAYERS);

	private ItemRegistry itemRegistry;

	@Override
	public String getHandlerName() {
		return "PlayerActionHandler";
	}

	@Override
	public void process(@Nonnull final World world, @Nonnull final EntityPlayer player) {

	}

	@Override
	public void onConnect() {
		this.itemRegistry = RegistryManager.get(RegistryType.ITEMS);
	}

	@Override
	public void onDisconnect() {
		this.itemRegistry = null;
	}

	@SubscribeEvent
	public void onJump(@Nonnull final LivingJumpEvent event) {
		if (!ModOptions.enableJumpSound)
			return;

		if (event.getEntity() == null || event.getEntity().world == null)
			return;

		if (event.getEntity().world.isRemote && EnvironState.isPlayer(event.getEntity()))
			SoundEffectHandler.INSTANCE.playSoundAtPlayer(EnvironState.getPlayer(), JUMP);
	}

	@SubscribeEvent
	public void onItemSwing(@Nonnull final PlayerInteractEvent.LeftClickEmpty event) {
		if (!ModOptions.enableSwingSound)
			return;

		if (event.getEntityPlayer() == null || event.getEntityPlayer().world == null)
			return;

		if (event.getEntityPlayer().world.isRemote && EnvironState.isPlayer(event.getEntityPlayer())) {
			final ItemStack currentItem = event.getEntityPlayer().getHeldItemMainhand();
			if (currentItem != null) {
				SoundEffect sound = null;
				if (this.itemRegistry.doSwordSound(currentItem))
					sound = SWORD;
				else if (this.itemRegistry.doAxeSound(currentItem))
					sound = AXE;

				if (sound != null)
					SoundEffectHandler.INSTANCE.playSoundAtPlayer(EnvironState.getPlayer(), sound);
			}
		}
	}

	private int craftSoundThrottle = 0;

	@SubscribeEvent
	public void onCrafting(@Nonnull final ItemCraftedEvent event) {
		if (!ModOptions.enableCraftingSound)
			return;

		if (this.craftSoundThrottle >= (EnvironState.getTickCounter() - 30))
			return;

		if (event.player == null || event.player.world == null)
			return;

		if (event.player.world.isRemote && EnvironState.isPlayer(event.player)) {
			craftSoundThrottle = EnvironState.getTickCounter();
			SoundEffectHandler.INSTANCE.playSoundAtPlayer(EnvironState.getPlayer(), CRAFTING);
		}

	}

	@SubscribeEvent
	public void onItemUse(@Nonnull final PlayerInteractEvent.RightClickItem event) {
		if (!ModOptions.enableBowPullSound)
			return;

		if (event.getEntityPlayer() == null || event.getEntityPlayer().world == null || event.getItemStack() == null)
			return;

		if (event.getEntityPlayer().world.isRemote && this.itemRegistry.doBowSound(event.getItemStack())) {
			SoundEffectHandler.INSTANCE.playSoundAtPlayer(EnvironState.getPlayer(), BOW_PULL);
		}
	}

}
