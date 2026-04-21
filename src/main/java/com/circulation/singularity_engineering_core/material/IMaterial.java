package com.circulation.singularity_engineering_core.material;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

import static com.circulation.singularity_engineering_core.crt.CrtAPI.CrtName;

/**
 * 代表一种材料（如铁、铜），可与 {@link com.circulation.singularity_engineering_core.material.part.IPart} 组合注册为游戏对象。
 */
@ZenRegister
@ZenClass(CrtName + "IMaterial")
public interface IMaterial {

    int NO_COLOR = -1;

    /**
     * 唯一 snake_case 标识符，例如 {@code "iron"}，用于注册名。
     */
    @ZenGetter("id")
    String getId();

    /**
     * 该材料的 I18n 翻译键，格式为 {@code material.<id>.name}，
     * 例如 {@code material.iron.name}。
     */
    @ZenGetter("translationKey")
    default String getTranslationKey() {
        return "material." + getId() + ".name";
    }

    /**
     * 控制哪些部件可以为该材料注册的过滤器。
     */
    @ZenGetter("filter")
    MaterialPartFilter getFilter();

    /**
     * 材料的渲染颜色，格式为 {@code 0xRRGGBB}。
     * 返回 {@link #NO_COLOR} 表示保持默认颜色。
     */
    @ZenGetter("color")
    default int getColor() {
        return NO_COLOR;
    }

    /**
     * 若为 {@code true}，该材料生成的物品部件及方块部件对应的 ItemBlock 将显示附魔光效。
     */
    @ZenGetter("enchanted")
    default boolean isEnchanted() {
        return false;
    }
}
