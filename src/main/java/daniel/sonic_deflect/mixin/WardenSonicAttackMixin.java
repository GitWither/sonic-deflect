package daniel.sonic_deflect.mixin;


import daniel.sonic_deflect.SonicDeflect;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.SonicBoomTask;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SonicBoomTask.class)
public class WardenSonicAttackMixin {

    //injecting into lambdas be like
    @Inject(method = "method_43265(Lnet/minecraft/entity/mob/WardenEntity;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)V", at = @At("HEAD"))
    private static void injectIntoAttack(WardenEntity warden, ServerWorld server, LivingEntity damagedEntity, CallbackInfo ci) {
        if (SonicDeflect.entityHasSonicDeflect(damagedEntity)) {
            Vec3d targetLookingVector = damagedEntity.getRotationVector();

            Vec3d startingPos = damagedEntity.getPos();
            Vec3d targetPos = damagedEntity.getPos().add(targetLookingVector.multiply(10));

            Vec3d distanceVector = targetPos.subtract(startingPos);
            int distance = MathHelper.floor(distanceVector.length()) + 3 * EnchantmentHelper.getEquipmentLevel(SonicDeflect.SONIC_DEFLECT_ENCHANTMENT, damagedEntity);
            for (int i = 1; i < distance; i++) {
                Vec3d particlePos = startingPos.add(targetLookingVector.multiply(i));
                server.spawnParticles(ParticleTypes.SONIC_BOOM, particlePos.x, particlePos.y, particlePos.z, 1, 0.0, 0.0, 0.0, 0.0);

                //meh
                List<Entity> entitiesToBeDamaged = server.getOtherEntities(damagedEntity, Box.of(particlePos, 1, 1, 1));
                for (Entity entity : entitiesToBeDamaged) {

                    if (entity instanceof LivingEntity) {
                        entity.damage(DamageSource.mob(warden), (float) warden.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) - 0.5f * i);
                    }
                }
            }
        }
    }

    @Inject(
            cancellable = true,
            method = "method_43265(Lnet/minecraft/entity/mob/WardenEntity;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)V",
            at = @At(value = "INVOKE", target = "net/minecraft/entity/LivingEntity.addVelocity(DDD)V", shift = At.Shift.BEFORE)
    )
    private static void cancelKnockback(WardenEntity wardenEntity, ServerWorld serverWorld, LivingEntity livingEntity, CallbackInfo ci) {
        if (SonicDeflect.entityHasSonicDeflect(livingEntity)) {
            ci.cancel();
        }
    }
}
