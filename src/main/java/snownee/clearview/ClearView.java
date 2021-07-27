package snownee.clearview;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod("clearview")
@EventBusSubscriber(Dist.CLIENT)
public final class ClearView {

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void fogDensity(EntityViewRenderEvent.FogDensity event) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) {
			return;
		}
		if (event.getRenderer().getMainCamera().getFluidInCamera() == FogType.NONE) {
			return;
		}
		float f = event.getRenderer().getRenderDistance();
		if (player.isCreative() || player.isSpectator()) {
			event.setDensity(f * 2f);
			event.setCanceled(true);
		} else if (canApply(player)) {
			event.setDensity(f);
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void renderOverlay(RenderBlockOverlayEvent event) {
		if (event.getOverlayType() != OverlayType.FIRE) {
			return;
		}
		if (event.getPlayer().isCreative()) {
			event.setCanceled(true);
		} else if (canApply(event.getPlayer())) {
			event.getMatrixStack().translate(0, -0.25, 0);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static boolean canApply(Player player) {
		return player.isEyeInFluid(FluidTags.LAVA) && player.hasEffect(MobEffects.FIRE_RESISTANCE);
	}

}
