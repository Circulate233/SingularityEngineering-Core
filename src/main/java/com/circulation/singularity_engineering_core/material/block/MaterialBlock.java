package com.circulation.singularity_engineering_core.material.block;

import com.circulation.singularity_engineering_core.material.IMaterial;
import com.circulation.singularity_engineering_core.material.MaterialRegistry;
import com.circulation.singularity_engineering_core.material.part.AbstractBlockPart;
import com.circulation.singularity_engineering_core.material.part.IPart;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

/**
 * 代表特定 {@link IMaterial} × {@link IPart} 组合的普通 {@link Block}，注册名与本地化键均自动派生：
 * <pre>
 *   registryName    = modId:material_part   (例如 singularity_engineering_core:iron_block_part)
 *   unlocalizedName = material_part
 * </pre>
 */
@Getter
public class MaterialBlock extends Block {

    private final IMaterial gameMaterial;
    private final AbstractBlockPart part;

    public MaterialBlock(String modId, IMaterial gameMaterial, AbstractBlockPart part) {
        super(Material.IRON);
        this.gameMaterial = gameMaterial;
        this.part = part;
        String blockId = part.getItemId(gameMaterial);
        setRegistryName(modId, blockId);
        setTranslationKey(blockId);
        setCreativeTab(MaterialRegistry.resolveCreativeTab(part.getConfiguredCreativeTab()));
        if (part.getConfiguredHardness() != null) {
            setHardness(part.getConfiguredHardness());
        }
        if (part.getConfiguredResistance() != null) {
            setResistance(part.getConfiguredResistance());
        }
        setSoundType(part.getConfiguredSoundType());
        if (part.getConfiguredHarvestTool() != null) {
            setHarvestLevel(part.getConfiguredHarvestTool(), part.getConfiguredHarvestLevel());
        }
        setLightLevel(Math.max(0, Math.min(15, part.getConfiguredLightValue())) / 15.0F);
        if (part.getConfiguredLightOpacity() != null) {
            setLightOpacity(part.getConfiguredLightOpacity());
        }
        if (part.getConfiguredSlipperiness() != null) {
            slipperiness = part.getConfiguredSlipperiness();
        }
    }

    /**
     * 在游戏内显示"材料名 部件名"的拼接形式，均通过 I18n 本地化。
     */
    @SideOnly(Side.CLIENT)
    @Override
    public @NotNull String getLocalizedName() {
        String matName = I18n.format(gameMaterial.getTranslationKey());
        String partName = I18n.format(part.getTranslationKey());
        return matName + " " + partName;
    }

    @Override
    public boolean isOpaqueCube(@NotNull IBlockState state) {
        return part.isConfiguredOpaqueCube();
    }

    @Override
    public boolean isFullCube(@NotNull IBlockState state) {
        return part.isConfiguredFullBlock();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public @NotNull BlockRenderLayer getRenderLayer() {
        return part.getConfiguredRenderLayer();
    }
}
