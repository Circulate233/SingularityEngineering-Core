package com.circulation.singularity_engineering_core.material.crt;

import com.circulation.singularity_engineering_core.SingularityEngineeringCore;
import com.circulation.singularity_engineering_core.material.IMaterial;
import com.circulation.singularity_engineering_core.material.MaterialRegistry;
import com.circulation.singularity_engineering_core.material.block.MaterialBlock;
import com.circulation.singularity_engineering_core.material.item.MaterialItem;
import com.circulation.singularity_engineering_core.material.part.AbstractBlockPart;
import com.circulation.singularity_engineering_core.material.part.AbstractFluidPart;
import com.circulation.singularity_engineering_core.material.part.AbstractItemPart;
import com.circulation.singularity_engineering_core.material.part.IPart;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static com.circulation.singularity_engineering_core.crt.CrtAPI.CrtName;
import static com.circulation.singularity_engineering_core.material.crt.MaterialHandler.TYPE_BLOCK;
import static com.circulation.singularity_engineering_core.material.crt.MaterialHandler.TYPE_FLUID;

/**
 * 用于创建 {@link IPart} 实例的 CraftTweaker 构建器。
 * <p>
 * 用法（ZenScript preInit）：
 * <pre>
 *   val gear = mods.singularity.MaterialHelper.createItemPart("gear").build();
 * </pre>
 * {@link #build()} 将部件注册到 {@link MaterialRegistry} 并返回实例。
 */
@ZenRegister
@ZenClass(CrtName + "PartBuilder")
public class CrtPartBuilder {

    private final String id;
    private final String type;

    CrtPartBuilder(String id, String type) {
        this.id = id;
        this.type = type;
    }

    /**
     * 构建部件，将其注册并返回 {@link IPart} 实例。
     */
    @ZenMethod
    public IPart build() {
        final IPart part;
        if (TYPE_BLOCK.equals(type)) {
            part = new AbstractBlockPart(id) {
                @Override
                protected MaterialBlock createBlock(IMaterial material) {
                    return new MaterialBlock(SingularityEngineeringCore.MOD_ID, material, this);
                }
            };
        } else if (TYPE_FLUID.equals(type)) {
            part = new AbstractFluidPart(id) {
                @Override
                protected Fluid createFluid(IMaterial material) {
                    String fluidName = getItemId(material);
                    return new Fluid(fluidName,
                        new ResourceLocation(SingularityEngineeringCore.MOD_ID, "blocks/" + fluidName + "_still"),
                        new ResourceLocation(SingularityEngineeringCore.MOD_ID, "blocks/" + fluidName + "_flow"));
                }
            };
        } else {
            part = new AbstractItemPart(id) {
                @Override
                protected MaterialItem createItem(IMaterial material) {
                    return new MaterialItem(SingularityEngineeringCore.MOD_ID, material, this);
                }
            };
        }
        MaterialRegistry.registerPart(part);
        return part;
    }
}
