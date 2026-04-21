package com.circulation.singularity_engineering_core.material.crt;

import com.circulation.singularity_engineering_core.material.MaterialRegistry;
import com.circulation.singularity_engineering_core.material.part.AbstractGasPart;
import com.circulation.singularity_engineering_core.material.part.IPart;
import crafttweaker.annotations.ZenRegister;
import mekanism.api.gas.Gas;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static com.circulation.singularity_engineering_core.crt.CrtAPI.CrtName;

/**
 * 用于创建独立 {@link Gas} 部件的 CraftTweaker 构建器。
 */
@ZenRegister
@ZenClass(CrtName + "GasPartBuilder")
public class CrtGasPartBuilder {

    private final String id;
    private boolean usesMaterialColor = false;
    private String icon;
    private int baseColor = 0xFFFFFF;
    private boolean visible = true;
    private boolean radiation = false;

    CrtGasPartBuilder(String id) {
        this.id = id;
    }

    @ZenMethod
    public CrtGasPartBuilder tinted() {
        this.usesMaterialColor = true;
        return this;
    }

    /**
     * 设置该气体部件共享的图标资源路径，格式为 {@code modid:path}。
     */
    @ZenMethod
    public CrtGasPartBuilder icon(String resourceLocation) {
        this.icon = resourceLocation;
        return this;
    }

    @ZenMethod
    public CrtGasPartBuilder baseColor(int baseColor) {
        this.baseColor = baseColor;
        return this;
    }

    @ZenMethod
    public CrtGasPartBuilder visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    @ZenMethod
    public CrtGasPartBuilder radiation(boolean radiation) {
        this.radiation = radiation;
        return this;
    }

    /**
     * 构建气体部件，将其注册并返回 {@link IPart} 实例。
     */
    @ZenMethod
    public IPart build() {
        final ResourceLocation iconLocation = icon == null ? null : new ResourceLocation(icon);
        IPart part = new AbstractGasPart(id, usesMaterialColor, iconLocation, baseColor, visible, radiation) {
        };
        MaterialRegistry.registerPart(part);
        return part;
    }
}
