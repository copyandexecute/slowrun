package de.hglabor.slowrun.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HostileEntity.class)
abstract class HostileEntityMixin extends PathAwareEntity {
    protected HostileEntityMixin(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "shouldDropLoot", at = @At("RETURN"), cancellable = true)
    private void injected(CallbackInfoReturnable<Boolean> cir) {
        // jajaja konnte irgendwie nicht blaze überschreiben
        if (getType() == EntityType.BLAZE) {
            cir.setReturnValue(random.nextInt(100) + 1 <= 10);
        }
    }
}
