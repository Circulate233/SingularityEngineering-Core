package com.circulation.singularity_engineering_core.material.crt;

import com.circulation.singularity_engineering_core.material.IMaterial;
import com.circulation.singularity_engineering_core.material.Material;
import com.circulation.singularity_engineering_core.material.MaterialRegistry;
import com.circulation.singularity_engineering_core.material.part.IPart;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static com.circulation.singularity_engineering_core.crt.CrtAPI.CrtName;

/**
 * 用于创建 {@link IMaterial} 实例的 CraftTweaker 构建器。
 * <p>
 * 用法（ZenScript preInit）：
 * <pre>
 *   val iron = mods.singularity.MaterialHelper.createMaterial("iron", "Iron")
 *       .whitelist(gear, dust)
 *       .build();
 * </pre>
 * {@link #build()} 将材料注册到 {@link MaterialRegistry} 并返回实例。
 */
@ZenRegister
@ZenClass(CrtName + "MaterialBuilder")
public class CrtMaterialBuilder {

    private final Material.Builder inner;

    CrtMaterialBuilder(String id) {
        this.inner = Material.builder(id);
    }

    /**
     * 切换为白名单模式：仅允许列出的部件为该材料注册。
     * 接受 {@link CrtPartBuilder#build()} 返回的 {@link IPart} 实例。
     */
    @ZenMethod
    public CrtMaterialBuilder whitelist(IPart... parts) {
        inner.whitelist(parts);
        return this;
    }

    /**
     * 切换为黑名单模式：除列出的部件外，所有部件均可为该材料注册。
     */
    @ZenMethod
    public CrtMaterialBuilder blacklist(IPart... parts) {
        inner.blacklist(parts);
        return this;
    }

    /**
     * 构建材料，将其注册并返回 {@link IMaterial} 实例。
     */
    @ZenMethod
    public IMaterial build() {
        IMaterial material = inner.build();
        MaterialRegistry.registerMaterial(material);
        return material;
    }
}
