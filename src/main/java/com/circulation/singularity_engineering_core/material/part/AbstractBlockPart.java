package com.circulation.singularity_engineering_core.material.part;

import com.circulation.singularity_engineering_core.SingularityEngineeringCore;
import com.circulation.singularity_engineering_core.material.IMaterial;
import com.circulation.singularity_engineering_core.material.block.MaterialBlock;
import com.circulation.singularity_engineering_core.material.block.MaterialItemBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

/**
 * 产生 {@link Block}（及配套 {@link ItemBlock}）的部件基类。
 * <p>
 * 子类实现 {@link #createBlock(IMaterial)} 提供具体的 {@link MaterialBlock}，方块及其 {@link ItemBlock} 均在此注册。
 */
public abstract class AbstractBlockPart extends AbstractPart {

    private final Float hardness;
    private final Float resistance;
    private final SoundType soundType;
    private final String harvestTool;
    private final int harvestLevel;
    private final int lightValue;
    private final Integer lightOpacity;
    private final Float slipperiness;
    private final boolean fullBlock;
    private final boolean translucent;
    private final BlockRenderLayer renderLayer;
    private final String creativeTab;

    protected AbstractBlockPart(
        String id,
        boolean usesMaterialColor,
        Float hardness,
        Float resistance,
        SoundType soundType,
        String harvestTool,
        int harvestLevel,
        int lightValue,
        Integer lightOpacity,
        Float slipperiness,
        boolean fullBlock,
        boolean translucent,
        BlockRenderLayer renderLayer,
        String creativeTab
    ) {
        super(id, usesMaterialColor);
        this.hardness = hardness;
        this.resistance = resistance;
        this.soundType = soundType;
        this.harvestTool = harvestTool;
        this.harvestLevel = harvestLevel;
        this.lightValue = lightValue;
        this.lightOpacity = lightOpacity;
        this.slipperiness = slipperiness;
        this.fullBlock = fullBlock;
        this.translucent = translucent;
        this.renderLayer = renderLayer;
        this.creativeTab = creativeTab;
    }

    protected MaterialBlock createBlock(IMaterial material) {
        return new MaterialBlock(SingularityEngineeringCore.MOD_ID, material, this);
    }

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
            String oreDictName = getOreDictName(material);
            if (oreDictName != null) {
                OreDictionary.registerOre(oreDictName, ib);
            }
        }
    }

    @Override
    public void registerModels(IMaterial material) {
        com.circulation.singularity_engineering_core.material.model.MaterialModelLoader
            .INSTANCE.registerBlock(SingularityEngineeringCore.MOD_ID, material, this);
    }

    @Override
    public String getOreDictName(IMaterial material) {
        return getId() + toUpperCamel(material.getId());
    }

    public Float getConfiguredHardness() {
        return hardness;
    }

    public Float getConfiguredResistance() {
        return resistance;
    }

    public SoundType getConfiguredSoundType() {
        return soundType;
    }

    public String getConfiguredHarvestTool() {
        return harvestTool;
    }

    public int getConfiguredHarvestLevel() {
        return harvestLevel;
    }

    public int getConfiguredLightValue() {
        return lightValue;
    }

    public Integer getConfiguredLightOpacity() {
        return lightOpacity;
    }

    public Float getConfiguredSlipperiness() {
        return slipperiness;
    }

    public boolean isConfiguredFullBlock() {
        return fullBlock;
    }

    public boolean isConfiguredTranslucent() {
        return translucent;
    }

    public boolean isConfiguredOpaqueCube() {
        return fullBlock && !translucent && renderLayer == BlockRenderLayer.SOLID;
    }

    public BlockRenderLayer getConfiguredRenderLayer() {
        return renderLayer;
    }

    public String getConfiguredCreativeTab() {
        return creativeTab;
    }
}
