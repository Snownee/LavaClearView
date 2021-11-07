package snownee.clearview.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

@Mixin(FogRenderer.class)
public class MixinFogRenderer {

    @Redirect(
            method = "setupFog", at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isSpectator()Z", ordinal = 0
            )
    )
    private static boolean clearview$setupFog(Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.isCreative() || player.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                return true;
            }
        }
        return entity.isSpectator();
    }

}
