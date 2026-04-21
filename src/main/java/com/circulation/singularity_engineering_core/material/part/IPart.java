package com.circulation.singularity_engineering_core.material.part;

import com.circulation.singularity_engineering_core.material.IMaterial;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

import static com.circulation.singularity_engineering_core.crt.CrtAPI.CrtName;

/**
 * 代表一种部件模板（如齿轮、粉末、燕融流体），可与 {@link IMaterial} 组合注册为游戏对象。
 * <p>
 * 各个 {@code register*} 方法由 {@link com.circulation.singularity_engineering_core.material.MaterialSystem}
 * 在对应的 Forge 生命周期阶段调用，实现类仅需重写与自身类型（物品、方块、流体或气体）相关的方法。
 */
@ZenRegister
@ZenClass(CrtName + "IPart")
public interface IPart {

    /**
     * 部件的唯一 snake_case 标识符，例如 {@code "gear"}。
     */
    @ZenGetter("id")
    String getId();

    /**
     * 该部件的 I18n 翻译键，格式为 {@code part.<id>.name}，
     * 例如 {@code part.gear.name}。
     */
    @ZenGetter("translationKey")
    default String getTranslationKey() {
        return "part." + getId() + ".name";
    }

    /**
     * 返回该部件与 {@code material} 组合的物品/方块/流体 id 段，例如 {@code "iron_gear"}。
     */
    @ZenMethod
    default String getItemId(IMaterial material) {
        return material.getId() + "_" + getId();
    }

    /**
     * 返回完整的 {@code modId:material_part} 注册名字符串。
     */
    @ZenMethod
    default String getRegistryName(String modId, IMaterial material) {
        return modId + ":" + getItemId(material);
    }

    /**
     * 若该部件适用于给定材料则返回 {@code true}。
     * 实现类可以重写此方法以添加材料侧的前提条件（如燕融流体部件可能要求材料具有 {@code hasMoltenForm}）。
     */
    @ZenMethod
    default boolean isApplicableTo(IMaterial material) {
        return true;
    }

    @ZenMethod
    default boolean usesMaterialColor() {
        return false;
    }

    @ZenMethod
    default String getOreDictName(IMaterial material) {
        return null;
    }

    default void registerItems(IMaterial material, IForgeRegistry<Item> registry) {
    }

    default void registerBlocks(IMaterial material, IForgeRegistry<Block> registry) {
    }

    /**
     * 在 preInit 阶段调用（在注册事件之前）。在 {@link AbstractFluidPart} 中重写。
     */
    default void registerFluids(IMaterial material) {
    }

    /**
     * 在 preInit 阶段调用（在注册事件之前）。在独立气体部件中重写。
     */
    default void registerGases(IMaterial material) {
    }

    /**
     * 在客户端调用以注册模型，在物品/方块部件中重写。
     */
    default void registerModels(IMaterial material) {
    }
}
