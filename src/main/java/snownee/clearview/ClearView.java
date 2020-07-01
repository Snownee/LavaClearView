package snownee.clearview;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod("clearview")
@EventBusSubscriber
public final class ClearView {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void fogDensity(EntityViewRenderEvent.FogDensity event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (player.isCreative()) {
            event.setDensity(0f);
            event.setCanceled(true);
        } else if (canApply(player)) {
            event.setDensity(0.03f);
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

    public static boolean canApply(PlayerEntity player) {
        return player.areEyesInFluid(FluidTags.LAVA) && player.isPotionActive(Effects.FIRE_RESISTANCE);
    }

}
