package com.circulation.singularity_engineering_core.material.part;

import com.circulation.singularity_engineering_core.SingularityEngineeringCore;
import com.circulation.singularity_engineering_core.material.IMaterial;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;
import net.minecraft.util.ResourceLocation;

/**
 * 产生 {@link Fluid}（及可选流体方块）的部件基类。
 * <p>
 * {@link #registerFluids(IMaterial)} 在 {@code preInit} 阶段调用（在 Forge {@code RegistryEvent} 之前），
 * 确保流体在方块/物品注册时已可用。
 * <p>
 * 子类实现 {@link #createFluid(IMaterial)} 提供 {@link Fluid} 实例；
 * 重写 {@link #createFluidBlock(IMaterial, Fluid)} 提供自定义流体方块（默认为 {@link net.minecraftforge.fluids.BlockFluidClassic}）。
 */
public abstract class AbstractFluidPart extends AbstractPart {

    private final String stillTexture;
    private final String flowingTexture;
    private final int baseColor;
    private final int density;
    private final int temperature;
    private final int viscosity;
    private final int luminosity;
    private final boolean gaseous;
    private final boolean bucket;

    protected AbstractFluidPart(
        String id,
        boolean usesMaterialColor,
        String stillTexture,
        String flowingTexture,
        int baseColor,
        int density,
        int temperature,
        int viscosity,
        int luminosity,
        boolean gaseous,
        boolean bucket
    ) {
        super(id, usesMaterialColor);
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.baseColor = baseColor;
        this.density = density;
        this.temperature = temperature;
        this.viscosity = viscosity;
        this.luminosity = luminosity;
        this.gaseous = gaseous;
        this.bucket = bucket;
    }

    protected Fluid createFluid(IMaterial material) {
        String fluidName = getItemId(material);
        Fluid fluid = new Fluid(fluidName, getStillTexture(material), getFlowingTexture(material), resolveFluidColor(material)) {
            @Override
            public String getLocalizedName(FluidStack stack) {
                String matName = I18n.format(material.getTranslationKey());
                String partName = I18n.format(AbstractFluidPart.this.getTranslationKey());
                return matName + " " + partName;
            }
        };
        applyFluidProperties(fluid, material);
        return fluid;
    }

    /**
     * 工厂方法：为给定材料和已注册流体创建流体方块。
     * 默认实现创建 {@link net.minecraftforge.fluids.BlockFluidClassic}，返回 {@code null} 则跳过流体方块注册。
     */
    protected Block createFluidBlock(IMaterial fmaterial, Fluid fluid) {
        BlockFluidClassic block =
            new BlockFluidClassic(fluid, net.minecraft.block.material.Material.WATER) {
                @SideOnly(Side.CLIENT)
                @Override
                public @NotNull String getLocalizedName() {
                    String matName = I18n.format(fmaterial.getTranslationKey());
                    String partName = I18n.format(AbstractFluidPart.this.getTranslationKey());
                    return matName + " " + partName;
                }
            };
        String blockId = getItemId(fmaterial) + "_block";
        block.setRegistryName(SingularityEngineeringCore.MOD_ID, blockId);
        block.setTranslationKey(blockId);
        return block;
    }

    /**
     * 在 {@code preInit} 阶段调用，将流体（及其方块，若非 null）注册到 Forge {@link FluidRegistry}。
     */
    @Override
    public void registerFluids(IMaterial material) {
        String fluidName = getItemId(material);
        Fluid fluid = FluidRegistry.getFluid(fluidName);
        if (fluid == null) {
            fluid = createFluid(material);
            FluidRegistry.registerFluid(fluid);
        } else {
            applyFluidProperties(fluid, material);
        }
        if (bucket) {
            FluidRegistry.addBucketForFluid(fluid);
        }
        if (fluid.getBlock() == null) {
            Block fluidBlock = createFluidBlock(material, fluid);
            if (fluidBlock != null) {
                fluid.setBlock(fluidBlock);
            }
        }
    }

    /**
     * 将流体方块注册到 Forge 方块注册表，由 {@link com.circulation.singularity_engineering_core.material.MaterialSystem}
     * 在 {@code RegistryEvent.Register<Block>} 阶段调用。
     */
    @Override
    public void registerBlocks(IMaterial material, IForgeRegistry<Block> registry) {
        Fluid fluid = FluidRegistry.getFluid(material.getId() + "_" + getId());
        if (fluid != null && fluid.getBlock() != null) {
            registry.register(fluid.getBlock());
        }
    }

    /**
     * 注册流体对应的桶 {@link Item}，由 {@link com.circulation.singularity_engineering_core.material.MaterialSystem}
     * 在 {@code RegistryEvent.Register<Item>} 阶段调用。
     */
    @Override
    public void registerItems(IMaterial material, IForgeRegistry<Item> registry) {
    }

    private void applyFluidProperties(Fluid fluid, IMaterial material) {
        fluid.setColor(resolveFluidColor(material));
        fluid.setDensity(density);
        fluid.setTemperature(temperature);
        fluid.setViscosity(viscosity);
        fluid.setLuminosity(luminosity);
        fluid.setGaseous(gaseous);
    }

    private ResourceLocation getStillTexture(IMaterial material) {
        if (stillTexture != null) {
            return new ResourceLocation(stillTexture);
        }
        return new ResourceLocation(SingularityEngineeringCore.MOD_ID, "blocks/" + getItemId(material) + "_still");
    }

    private ResourceLocation getFlowingTexture(IMaterial material) {
        if (flowingTexture != null) {
            return new ResourceLocation(flowingTexture);
        }
        return new ResourceLocation(SingularityEngineeringCore.MOD_ID, "blocks/" + getItemId(material) + "_flow");
    }

    private int resolveFluidColor(IMaterial material) {
        if (usesMaterialColor()) {
            return material.getColor() == IMaterial.NO_COLOR ? 0xFFFFFFFF : 0xFF000000 | material.getColor();
        }
        return normalizeFluidColor(baseColor);
    }

    private static int normalizeFluidColor(int color) {
        return (color & 0xFF000000) == 0 ? 0xFF000000 | color : color;
    }
}
