package tk.fan2tao.mc.goprone.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tk.fan2tao.mc.goprone.GoProne;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> type, Level world) {
        super(type, world);
    }

    @Inject(method = "updatePlayerPose", at = @At("HEAD"), cancellable = true)
    public void updatePose(CallbackInfo ci) {
        if (GoProne.entityProneStates.getOrDefault(this.getUUID(), false)) {
            this.setPose(Pose.SWIMMING);
            ci.cancel();
        }
    }
}
