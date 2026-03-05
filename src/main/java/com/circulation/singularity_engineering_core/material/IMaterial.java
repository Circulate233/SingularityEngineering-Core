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
}
