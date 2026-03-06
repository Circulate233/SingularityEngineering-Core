package com.circulation.singularity_engineering_core.material.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * 方块部件对应的 {@link ItemBlock}，支持附魔光效。
 * <p>
 * 当材料的 {@code enchanted} 属性为 {@code true} 时，持有该物品的物品栏格子会显示
 * 紫色附魔光晕（与附魔书/附魔工具相同效果）。
 */
public class MaterialItemBlock extends ItemBlock {

    private final boolean enchanted;

    public MaterialItemBlock(Block block, boolean enchanted) {
        super(block);
        this.enchanted = enchanted;
    }

    /**
     * 当材料开启了附魔光效时，ItemBlock 始终显示附魔光效（紫色光晕）。
     */
    @Override
    public boolean hasEffect(@NotNull ItemStack stack) {
        return enchanted;
    }
}
