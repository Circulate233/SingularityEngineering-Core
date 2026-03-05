package com.circulation.singularity_engineering_core.material.block;

import com.circulation.singularity_engineering_core.material.IMaterial;
import com.circulation.singularity_engineering_core.material.MaterialRegistry;
import com.circulation.singularity_engineering_core.material.part.IPart;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
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
    private final IPart part;

    public MaterialBlock(String modId, IMaterial gameMaterial, IPart part) {
        super(Material.IRON);
        this.gameMaterial = gameMaterial;
        this.part = part;
        String blockId = part.getItemId(gameMaterial);
        setRegistryName(modId, blockId);
        setTranslationKey(blockId);
        setCreativeTab(MaterialRegistry.creativeTabs);
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
}
