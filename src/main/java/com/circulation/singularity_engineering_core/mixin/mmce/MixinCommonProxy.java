package com.circulation.singularity_engineering_core.mixin.mmce;

import com.circulation.singularity_engineering_core.handler.ThermodynamicsHandler;
import com.circulation.singularity_engineering_core.management.GlobalManagement;
import hellfirepvp.modularmachinery.common.CommonProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("MethodMayBeStatic")
@Mixin(CommonProxy.class)
public class MixinCommonProxy {

    @Inject(
            method = "postInit",
            at = @At(
                    value = "INVOKE",
                    target = "Lhellfirepvp/modularmachinery/common/machine/MachineRegistry;registerMachines(Ljava/util/Collection;)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    private void hookAfterRegisterMachine(final CallbackInfo ci) {
        GlobalManagement.preInit();
        ThermodynamicsHandler.load();
    }

}