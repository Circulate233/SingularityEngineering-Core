package com.circulation.singularity_engineering_core.mixin.mmce;

import com.circulation.singularity_engineering_core.management.Thermodynamics;
import hellfirepvp.modularmachinery.common.machine.DynamicMachine;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(TileMultiblockMachineController.class)
public abstract class MixinTileMultiblockMachineController extends TileEntity {

    @Nullable
    @Shadow(remap = false)
    public abstract DynamicMachine getFoundMachine();

    @Shadow(remap = false)
    public abstract TileMultiblockMachineController getController();

    @Inject(method = "resetMachine", at = @At("HEAD"), remap = false)
    private void onResetMachine(final boolean clearData, final CallbackInfo ci) {
        if (clearData) {
            sec$removeCache();
        }
    }

    @Inject(method = "invalidate", at = @At("HEAD"))
    private void onInvalidate(final CallbackInfo ci) {
        sec$removeCache();
    }

    @Inject(method = "onChunkUnload", at = @At("RETURN"), remap = false)
    public void injectOnChunkUnload(final CallbackInfo ci) {
        sec$removeCache();
    }

    @Unique
    private void sec$removeCache() {
        TileMultiblockMachineController controller = getController();
        Thermodynamics.removeSystem(controller);
    }
}