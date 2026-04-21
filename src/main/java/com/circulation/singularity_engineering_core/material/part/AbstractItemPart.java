package com.circulation.singularity_engineering_core.material.part;

import com.circulation.singularity_engineering_core.SingularityEngineeringCore;
import com.circulation.singularity_engineering_core.material.IMaterial;
import com.circulation.singularity_engineering_core.material.item.MaterialItem;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * 产生 {@link Item} 的部件基类。
 * <p>
 * 子类实现 {@link #createItem(IMaterial)} 提供具体的 {@link MaterialItem} 实例，注册与模型绑定均在此处理。
 */
public abstract class AbstractItemPart extends AbstractPart {

    private final int maxStackSize;
    private final int maxDamage;
    private final boolean oreDict;
    private final String creativeTab;

    protected AbstractItemPart(String id, boolean usesMaterialColor, int maxStackSize, int maxDamage, boolean oreDict, String creativeTab) {
        super(id, usesMaterialColor);
        this.maxStackSize = maxStackSize;
        this.maxDamage = maxDamage;
        this.oreDict = oreDict;
        this.creativeTab = creativeTab;
    }

    protected MaterialItem createItem(IMaterial material) {
        return new MaterialItem(SingularityEngineeringCore.MOD_ID, material, this);
    }

    @Override
    public void registerItems(IMaterial material, IForgeRegistry<Item> registry) {
        MaterialItem item = createItem(material);
        registry.register(item);
        String oreDictName = getOreDictName(material);
        if (oreDictName != null) {
            OreDictionary.registerOre(oreDictName, item);
        }
    }

    @Override
    public void registerModels(IMaterial material) {
        com.circulation.singularity_engineering_core.material.model.MaterialModelLoader
            .INSTANCE.registerItem(SingularityEngineeringCore.MOD_ID, material, this);
    }

    @Override
    public String getOreDictName(IMaterial material) {
        return oreDict ? getId() + toUpperCamel(material.getId()) : null;
    }

    public int getConfiguredMaxStackSize() {
        return maxStackSize;
    }

    public int getConfiguredMaxDamage() {
        return maxDamage;
    }

    public String getConfiguredCreativeTab() {
        return creativeTab;
    }
}
