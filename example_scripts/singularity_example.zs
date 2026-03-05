// ============================================================
// singularity_example.zs
// 示例脚本：注册 2 种物品部件、1 种流体部件、1 种方块部件，以及 3 种材料
// 此脚本应放置在 <实例目录>/scripts/ 下，由 CraftTweaker 在 preInit 阶段执行
// ============================================================

#loader preinit

import singularity.MaterialHelper;

// ── 注册部件 ─────────────────────────────────────────────────

// 物品部件：齿轮
val partGear   = MaterialHelper.createItemPart("gear").build();

// 物品部件：粉末
val partDust   = MaterialHelper.createItemPart("dust").build();

// 流体部件：熔融态
val partMolten = MaterialHelper.createFluidPart("molten").build();

// 方块部件：矿石
val partOre    = MaterialHelper.createBlockPart("ore").build();

// ── 注册材料 ─────────────────────────────────────────────────

// 材料 1：铁（iron）
//   白名单模式：仅允许 gear、dust、ore 三种部件
val iron = MaterialHelper.createMaterial("iron")
    .whitelist(partGear, partDust, partOre)
    .build();

// 材料 2：铜（copper）
//   白名单模式：允许所有四种部件
val copper = MaterialHelper.createMaterial("copper")
    .whitelist(partGear, partDust, partMolten, partOre)
    .build();

// 材料 3：钛（titanium）
//   黑名单模式：除 dust 外的所有部件均可注册
val titanium = MaterialHelper.createMaterial("titanium")
    .blacklist(partDust)
    .build();
