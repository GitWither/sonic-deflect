package daniel.sonic_deflect;

import daniel.sonic_deflect.enchantment.SonicDeflectEnchantment;
import net.fabricmc.api.ModInitializer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
public class SonicDeflect implements ModInitializer {
    public static Enchantment SONIC_DEFLECT_ENCHANTMENT;

    @Override
    public void onInitialize() {
        SONIC_DEFLECT_ENCHANTMENT = Registry.register(Registries.ENCHANTMENT, new Identifier("sonic_deflect", "sonic_deflect"), new SonicDeflectEnchantment());
    }

    public static boolean entityHasSonicDeflect(LivingEntity entity) {
        return EnchantmentHelper.getEquipmentLevel(SonicDeflect.SONIC_DEFLECT_ENCHANTMENT, entity) > 0;
    }
}
