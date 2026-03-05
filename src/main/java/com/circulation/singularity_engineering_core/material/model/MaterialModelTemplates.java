package com.circulation.singularity_engineering_core.material.model;

/**
 * 为 {@link MaterialModelLoader} 生成最简 JSON 模型字符串，每个方法输出可被 Forge 模型系统直接解析的 JSON。
 */
public final class MaterialModelTemplates {

    private MaterialModelTemplates() {
    }

    /**
     * 生成扁平 {@code item/generated} 模型 JSON。
     *
     * @param texturePath 完整资源路径，例如 {@code "singularity_engineering_core:items/iron_gear"}
     */
    public static String itemGenerated(String texturePath) {
        return "{\"parent\":\"item/generated\",\"textures\":{\"layer0\":\"" + texturePath + "\"}}";
    }

    /**
     * 生成 {@code cube_all} 方块模型 JSON。
     *
     * @param texturePath 完整资源路径，例如 {@code "singularity_engineering_core:blocks/iron_block_part"}
     */
    public static String blockCubeAll(String texturePath) {
        return "{\"parent\":\"block/cube_all\",\"textures\":{\"all\":\"" + texturePath + "\"}}";
    }
}
