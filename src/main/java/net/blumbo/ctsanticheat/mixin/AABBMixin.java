package net.blumbo.ctsanticheat.mixin;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AABB.class)
public class AABBMixin {

    @Shadow @Final public double minX;

    @Shadow @Final public double maxX;

    @Shadow @Final public double maxY;

    @Shadow @Final public double minY;

    @Shadow @Final public double minZ;

    @Shadow @Final public double maxZ;

    // Vanilla code is messy and buggy so here's this
    @Inject(method = "getNearestPointTo", cancellable = true, at = @At("HEAD"))
    private void getNearestPointTo(Vec3 vec3, CallbackInfoReturnable<Vec3> cir) {
        double x = Mth.clamp(vec3.x, this.minX, this.maxX);
        double y = Mth.clamp(vec3.y, this.minY, this.maxY);
        double z = Mth.clamp(vec3.z, this.minZ, this.maxZ);

        cir.setReturnValue(new Vec3(x, y, z));
    }

}
