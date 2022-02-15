package tk.fan2tao.mc.goprone.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tk.fan2tao.mc.goprone.GoProne;
import tk.fan2tao.mc.goprone.utils.EntityStates;

import java.util.Objects;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> type, Level world) {
        super(type, world);
    }

    @Inject(method = "updatePlayerPose", at = @At("HEAD"), cancellable = true)
    public void updatePlayerPose(CallbackInfo ci) {
        if (EntityStates.isProne(this.getUUID())) {
            org.bukkit.entity.Player craftPlayer = Objects.requireNonNull(Bukkit.getPlayer(this.getUUID()));

            if (!craftPlayer.isFlying() && !this.isFallFlying()) {
                this.setPose(Pose.SWIMMING);
                ci.cancel();
            } else {
                EntityStates.toggleProne(craftPlayer);
            }
        }
    }

    @Inject(method = "jumpFromGround", at = @At("HEAD"))
    public void jumpFromGround(CallbackInfo ci) {
        /*if (this.isOnGround() && this.getPose() == Pose.SWIMMING && !GoProne.isJumpingAllowed) {
            Vec3 motion = this.getDeltaMovement();
            this.setDeltaMovement(motion.add(0, -motion.y, 0)); // set y motion to 0
            ci.cancel();
        }*/
        if (EntityStates.isProne(this.getUUID())) {
            org.bukkit.entity.Player craftPlayer = Objects.requireNonNull(Bukkit.getPlayer(this.getUUID()));
            EntityStates.toggleProne(craftPlayer);
        }
    }
}
