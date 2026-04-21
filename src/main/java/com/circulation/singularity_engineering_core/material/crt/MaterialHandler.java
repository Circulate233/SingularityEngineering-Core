package com.circulation.singularity_engineering_core.material.crt;

import com.circulation.singularity_engineering_core.material.IMaterial;
import com.circulation.singularity_engineering_core.material.MaterialRegistry;
import com.circulation.singularity_engineering_core.material.part.IPart;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static com.circulation.singularity_engineering_core.crt.CrtAPI.CrtName;

/**
 * 材料系统的 CraftTweaker 入口点。
 * <p>
 * 所有方法均为 {@code static}，调用方式：{@code mods.singularity.MaterialHelper.method(...)}。
 * <p>
 * 典型 preInit 脚本流程：
 * <pre>
 *   val gear  = mods.singularity.MaterialHelper.createItemPart("gear").build();
 *   val molten = mods.singularity.MaterialHelper.createFluidPart("molten").build();
 *
 *   mods.singularity.MaterialHelper.createMaterial("iron", "Iron")
 *       .whitelist(gear, molten)
 *       .build();
 * </pre>
 */
@ZenRegister
@ZenClass(CrtName + "MaterialHelper")
public final class MaterialHandler {

    private MaterialHandler() {
    }

    @ZenMethod
    public static CrtItemPartBuilder createItemPart(String id) {
        return new CrtItemPartBuilder(id);
    }

    @ZenMethod
    public static CrtBlockPartBuilder createBlockPart(String id) {
        return new CrtBlockPartBuilder(id);
    }

    @ZenMethod
    public static CrtFluidPartBuilder createFluidPart(String id) {
        return new CrtFluidPartBuilder(id);
    }

    @ZenMethod
    public static CrtGasPartBuilder createGasPart(String id) {
        return new CrtGasPartBuilder(id);
    }

    @ZenMethod
    public static CrtMaterialBuilder createMaterial(String id) {
        return new CrtMaterialBuilder(id);
    }

    @ZenMethod
    public static CrtCreativeTabBuilder createCreativeTab(String id) {
        return new CrtCreativeTabBuilder(id);
    }

    @ZenMethod
    public static IPart getPart(String id) {
        return MaterialRegistry.getPart(id);
    }

    @ZenMethod
    public static IMaterial getMaterial(String id) {
        return MaterialRegistry.getMaterial(id);
    }
}
