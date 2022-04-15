package daniel.sonic_deflect.test;

import com.google.common.collect.ImmutableList;
import daniel.sonic_deflect.SonicDeflect;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.block.Blocks;
import net.minecraft.class_7396;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.WardenBrain;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.event.GameEvent;

public class SonicDeflectionTestSuite implements FabricGameTest {


    @GameTest(structureName = "sonic_deflect:warden_enclosure", tickLimit = 323)
    public void testDeflectionSuite(TestContext context) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                context.setBlockState(x, 1, y, Blocks.STONE);
            }
        }

        ChickenEntity chicken = context.spawnMob(EntityType.CHICKEN, 1, 2, 6);

        HuskEntity husk = context.spawnMob(EntityType.HUSK, 1, 2, 1);

        ItemStack enchantedChestplate = Items.NETHERITE_CHESTPLATE.getDefaultStack();
        enchantedChestplate.addEnchantment(SonicDeflect.SONIC_DEFLECT_ENCHANTMENT, 3);
        enchantedChestplate.addEnchantment(Enchantments.PROTECTION, 5);


        husk.equipStack(EquipmentSlot.HEAD, Items.NETHERITE_HELMET.getDefaultStack());
        husk.equipStack(EquipmentSlot.CHEST, enchantedChestplate);
        husk.equipStack(EquipmentSlot.LEGS, Items.NETHERITE_LEGGINGS.getDefaultStack());
        husk.equipStack(EquipmentSlot.FEET, Items.NETHERITE_BOOTS.getDefaultStack());

        WardenEntity warden = new WardenEntity(EntityType.WARDEN, context.getWorld());
        Vec3d absPos = context.getAbsolute(new Vec3d(6, 2, 5));
        warden.setPos(absPos.x, absPos.y, absPos.z);
        warden.setCustomName(new LiteralText("Subject"));
        warden.initialize(context.getWorld(), context.getWorld().getLocalDifficulty(BlockPos.ORIGIN), SpawnReason.SPAWN_EGG, null, null);
        context.getWorld().spawnEntity(warden);


        //warden.setTarget(husk);
        warden.getAngerManager().increaseAngerAt(husk, 5000);
        //warden.getBrain().setTaskList(Activity.FIGHT, 0, ImmutableList.of(new class_7396()));
        //warden.getBrain().doExclusively(Activity.FIGHT);

        //System.out.println(warden.getTarget().getName());

        context.addFinalTaskWithDuration(323, () -> {
            context.dontExpectEntityAt(EntityType.CHICKEN, 1, 2, 6);
        });
    }
}
