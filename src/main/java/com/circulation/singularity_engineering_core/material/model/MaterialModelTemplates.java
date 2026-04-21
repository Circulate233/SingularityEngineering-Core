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

    /**
     * 生成带有 {@code tintindex: 0} 的立方体方块模型 JSON。
     *
     * @param texturePath 完整资源路径，例如 {@code "singularity_engineering_core:blocks/iron_ore"}
     */
    public static String blockCubeAllTinted(String texturePath) {
        return "{"
            + "\"parent\":\"block/block\","
            + "\"textures\":{"
            + "\"particle\":\"" + texturePath + "\","
            + "\"down\":\"" + texturePath + "\","
            + "\"up\":\"" + texturePath + "\","
            + "\"north\":\"" + texturePath + "\","
            + "\"south\":\"" + texturePath + "\","
            + "\"west\":\"" + texturePath + "\","
            + "\"east\":\"" + texturePath + "\""
            + "},"
            + "\"elements\":[{"
            + "\"from\":[0,0,0],"
            + "\"to\":[16,16,16],"
            + "\"faces\":{"
            + "\"down\":{\"texture\":\"#down\",\"cullface\":\"down\",\"tintindex\":0},"
            + "\"up\":{\"texture\":\"#up\",\"cullface\":\"up\",\"tintindex\":0},"
            + "\"north\":{\"texture\":\"#north\",\"cullface\":\"north\",\"tintindex\":0},"
            + "\"south\":{\"texture\":\"#south\",\"cullface\":\"south\",\"tintindex\":0},"
            + "\"west\":{\"texture\":\"#west\",\"cullface\":\"west\",\"tintindex\":0},"
            + "\"east\":{\"texture\":\"#east\",\"cullface\":\"east\",\"tintindex\":0}"
            + "}"
            + "}]"
            + "}";
    }
}
