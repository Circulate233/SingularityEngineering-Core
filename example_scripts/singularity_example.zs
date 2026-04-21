// 此脚本应放置在 <实例目录>/scripts/ 下，由 CraftTweaker 在 preInit 阶段执行

#loader preinit

import singularity.MaterialHelper;

// ============================================================
// singularity_example.zs
//
// 材料系统完整 API 示例。
// ============================================================

// ============================================================
// 零、CreativeTab Builder 全方法示例
// ============================================================

// createCreativeTab("singularity_materials")
// 作用：创建自定义创造标签页。
val tabMaterials = MaterialHelper.createCreativeTab("singularity_materials")

    // icon("modid:path[:meta]")
    // 作用：设置创造标签页图标。
    .icon("minecraft:wool:3")

    // build()
    // 作用：注册创造标签页并返回 id。
    .build();

// ============================================================
// 一、Item Part Builder 全方法示例
// ============================================================

// createItemPart("gear")
// 作用：创建 item 部件 builder。
val partGear = MaterialHelper.createItemPart("gear")

    // tinted()
    // 作用：启用材料颜色。
    .tinted()

    // maxStackSize(16)
    // 作用：设置最大堆叠数量。
    .maxStackSize(16)

    // maxDamage(250)
    // 作用：设置最大耐久值。
    .maxDamage(250)

    // oreDict(true)
    // 作用：控制矿辞自动注册。
    .oreDict(true)

    // creativeTab(tabMaterials)
    // 作用：设置创造标签页。
    .creativeTab(tabMaterials)

    // build()
    // 作用：注册部件模板。
    .build();

// 第二个 item part。
val partPlate = MaterialHelper.createItemPart("plate")

    // maxStackSize(64)
    // 作用：设置最大堆叠数量。
    .maxStackSize(64)

    // maxDamage(0)
    // 作用：设置最大耐久值。
    .maxDamage(0)

    // oreDict(false)
    // 作用：关闭矿辞自动注册。
    .oreDict(false)

    // creativeTab("TOOLS")
    // 作用：设置创造标签页。
    .creativeTab("TOOLS")
    .build();

// ============================================================
// 二、Block Part Builder 全方法示例
// ============================================================

val partCrystalOre = MaterialHelper.createBlockPart("crystal_ore")

    // tinted()
    // 作用：启用材料颜色。
    .tinted()

    // hardness(4.5)
    // 作用：设置硬度。
    .hardness(4.5)

    // resistance(8.0)
    // 作用：设置爆炸抗性。
    .resistance(8.0)

    // soundType("STONE")
    // 作用：设置声音类型。
    .soundType("STONE")

    // harvestTool("pickaxe")
    // 作用：设置采掘工具。
    .harvestTool("pickaxe")

    // harvestLevel(2)
    // 作用：设置采掘等级。
    .harvestLevel(2)

    // lightValue(7)
    // 作用：设置发光等级。
    .lightValue(7)

    // lightOpacity(0)
    // 作用：设置遮光值。
    .lightOpacity(0)

    // slipperiness(0.98)
    // 作用：设置滑动系数。
    .slipperiness(0.98)

    // fullBlock(false)
    // 作用：设置是否为完整方块。
    .fullBlock(false)

    // translucent(true)
    // 作用：设置是否半透明。
    .translucent(true)

    // renderLayer("TRANSLUCENT")
    // 作用：设置渲染层。
    .renderLayer("TRANSLUCENT")

    // creativeTab("BUILDING_BLOCKS")
    // 作用：设置创造标签页。
    .creativeTab("BUILDING_BLOCKS")
    .build();

// ============================================================
// 三、Fluid Part Builder 全方法示例
// ============================================================

val partMolten = MaterialHelper.createFluidPart("molten")

    // tinted()
    // 作用：启用材料颜色。
    .tinted()

    // stillTexture("modid:path")
    // 作用：设置静止纹理。
    .stillTexture("singularity_engineering_core:blocks/molten_template_still")

    // flowingTexture("modid:path")
    // 作用：设置流动纹理。
    .flowingTexture("singularity_engineering_core:blocks/molten_template_flow")

    // baseColor(0xFFAA33)
    // 作用：设置基础颜色。
    .baseColor(0xFFAA33)

    // density(3000)
    // 作用：设置密度。
    .density(3000)

    // temperature(1200)
    // 作用：设置温度。
    .temperature(1200)

    // viscosity(6000)
    // 作用：设置粘度。
    .viscosity(6000)

    // luminosity(10)
    // 作用：设置发光等级。
    .luminosity(10)

    // gaseous(false)
    // 作用：设置是否为气态。
    .gaseous(false)

    // bucket(true)
    // 作用：设置是否注册桶。
    .bucket(true)
    .build();

// 第二个 fluid part。
val partEssence = MaterialHelper.createFluidPart("essence")

    // baseColor(0x55FFCC)
    // 作用：设置基础颜色。
    .baseColor(0x55FFCC)

    // density(900)
    // 作用：设置密度。
    .density(900)

    // temperature(290)
    // 作用：设置温度。
    .temperature(290)

    // viscosity(1200)
    // 作用：设置粘度。
    .viscosity(1200)

    // luminosity(0)
    // 作用：设置发光等级。
    .luminosity(0)

    // gaseous(true)
    // 作用：设置是否为气态。
    .gaseous(true)

    // bucket(false)
    // 作用：设置是否注册桶。
    .bucket(false)
    .build();

// ============================================================
// 四、Gas Part Builder 全方法示例
// ============================================================

val partGas = MaterialHelper.createGasPart("gas")

    // tinted()
    // 作用：启用材料颜色。
    .tinted()

    // icon("modid:path")
    // 作用：设置图标。
    .icon("mekanism:blocks/liquid/liquid")

    // baseColor(0xFFFFFF)
    // 作用：设置基础颜色。
    .baseColor(0xFFFFFF)

    // visible(true)
    // 作用：设置是否可见。
    .visible(true)

    // radiation(false)
    // 作用：设置是否带辐射。
    .radiation(false)
    .build();

// 第二个 gas part。
val partVapor = MaterialHelper.createGasPart("vapor")

    // baseColor(0x88FFFF)
    // 作用：设置基础颜色。
    .baseColor(0x88FFFF)

    // visible(true)
    // 作用：设置是否可见。
    .visible(true)

    // radiation(false)
    // 作用：设置是否带辐射。
    .radiation(false)
    .build();

// ============================================================
// 五、Material Builder 全方法示例
// ============================================================

// createMaterial("iron")
// 作用：创建材料 builder。
val iron = MaterialHelper.createMaterial("iron")

    // color(0xD8D8D8)
    // 作用：设置材料颜色。
    .color(0xD8D8D8)

    // whitelist(partA, partB, ...)
    // 作用：设置白名单。
    .whitelist(partGear, partPlate, partCrystalOre, partMolten, partGas)
    .build();

// 第二个材料。
val refinedObsidian = MaterialHelper.createMaterial("refined_obsidian")
    .color(0x5B4B8A)

    // blacklist(partA, partB, ...)
    // 作用：设置黑名单。
    .blacklist(partPlate)
    .build();

// 第三个材料。
val titanium = MaterialHelper.createMaterial("titanium")
    .color(0xD0D7E5)

    // enchanted()
    // 作用：启用附魔光效。
    .enchanted()

    // whitelist(partA, partB, ...)
    // 作用：设置白名单。
    .whitelist(partGear, partCrystalOre, partEssence, partVapor)
    .build();

// ============================================================
// 六、MaterialHelper 查询方法示例
// ============================================================

// getPart("gear")
// 作用：按 id 读取 part。
val fetchedGearPart = MaterialHelper.getPart("gear");

// getMaterial("iron")
// 作用：按 id 读取 material。
val fetchedIronMaterial = MaterialHelper.getMaterial("iron");
