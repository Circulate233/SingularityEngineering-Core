package com.circulation.singularity_engineering_core.material.part;

import com.circulation.singularity_engineering_core.SingularityEngineeringCore;
import com.circulation.singularity_engineering_core.material.IMaterial;
import com.circulation.singularity_engineering_core.material.block.MaterialBlock;
import com.circulation.singularity_engineering_core.material.block.MaterialItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

/**
 * 产生 {@link Block}（及配套 {@link ItemBlock}）的部件基类。
 * <p>
 * 子类实现 {@link #createBlock(IMaterial)} 提供具体的 {@link MaterialBlock}，方块及其 {@link ItemBlock} 均在此注册。
 */
public abstract class AbstractBlockPart extends AbstractPart {

    protected AbstractBlockPart(String id) {
        super(id);
    }

    /**
     * 工厂方法：为给定材料创建 {@link MaterialBlock}。
     * 返回的方块不得已被注册。
     */
    protected abstract MaterialBlock createBlock(IMaterial material);

    @Override
    public void registerBlocks(IMaterial material, IForgeRegistry<Block> registry) {
        MaterialBlock block = createBlock(material);
        registry.register(block);
    }

    /**
     * 注册配套的 {@link ItemBlock}，由 {@link com.circulation.singularity_engineering_core.material.MaterialSystem}
     * 在 {@code RegistryEvent.Register<Item>} 阶段调用。子类可重写以提供自定义 {@link ItemBlock}。
     */
    public void registerItemBlock(IMaterial material, IForgeRegistry<Item> registry) {
        String blockId = getItemId(material);
        Block block = Block.REGISTRY.getObject(
            new net.minecraft.util.ResourceLocation(SingularityEngineeringCore.MOD_ID, blockId));
        if (block != net.minecraft.init.Blocks.AIR) {
            MaterialItemBlock ib = new MaterialItemBlock(block, material.isEnchanted());
            ib.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
            registry.register(ib);
        }
    }

    @Override
    public void registerModels(IMaterial material) {
        com.circulation.singularity_engineering_core.material.model.MaterialModelLoader
            .INSTANCE.registerBlock(SingularityEngineeringCore.MOD_ID, material, this);
    }
}
