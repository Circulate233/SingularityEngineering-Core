package com.circulation.singularity_engineering_core.material.item;

import com.circulation.singularity_engineering_core.material.IMaterial;
import com.circulation.singularity_engineering_core.material.MaterialRegistry;
import com.circulation.singularity_engineering_core.material.part.AbstractItemPart;
import com.circulation.singularity_engineering_core.material.part.IPart;
import lombok.Getter;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

/**
 * 代表特定 {@link IMaterial} × {@link IPart} 组合的普通 {@link Item}，注册名与本地化键均自动派生：
 * <pre>
 *   registryName    = modId:material_part   (例如 singularity_engineering_core:iron_gear)
 *   unlocalizedName = material_part         (例如 iron_gear)
 * </pre>
 */
@Getter
public class MaterialItem extends Item {

    private final IMaterial material;
    private final AbstractItemPart part;

    public MaterialItem(String modId, IMaterial material, AbstractItemPart part) {
        this.material = material;
        this.part = part;
        String itemId = part.getItemId(material);
        setRegistryName(modId, itemId);
        setTranslationKey(itemId);
        setCreativeTab(MaterialRegistry.resolveCreativeTab(part.getConfiguredCreativeTab()));
        setMaxStackSize(part.getConfiguredMaxStackSize());
        setMaxDamage(part.getConfiguredMaxDamage());
    }

    /**
     * 在游戏内显示"材料名 部件名"的拼接形式，均通过 I18n 本地化。
     */
    @SideOnly(Side.CLIENT)
    @Override
    public @NotNull String getItemStackDisplayName(@NotNull ItemStack stack) {
        String matName = I18n.format(material.getTranslationKey());
        String partName = I18n.format(part.getTranslationKey());
        return matName + " " + partName;
    }

    /**
     * 当材料开启了附魔光效时，物品始终显示附魔光效（紫色光晕）。
     */
    @Override
    public boolean hasEffect(@NotNull ItemStack stack) {
        return material.isEnchanted();
    }

}
