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

    public static final String TYPE_ITEM = "item";
    public static final String TYPE_BLOCK = "block";
    public static final String TYPE_FLUID = "fluid";

    private MaterialHandler() {
    }

    @ZenMethod
    public static CrtPartBuilder createItemPart(String id) {
        return new CrtPartBuilder(id, TYPE_ITEM);
    }

    @ZenMethod
    public static CrtPartBuilder createBlockPart(String id) {
        return new CrtPartBuilder(id, TYPE_BLOCK);
    }

    @ZenMethod
    public static CrtPartBuilder createFluidPart(String id) {
        return new CrtPartBuilder(id, TYPE_FLUID);
    }

    @ZenMethod
    public static CrtMaterialBuilder createMaterial(String id) {
        return new CrtMaterialBuilder(id);
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
