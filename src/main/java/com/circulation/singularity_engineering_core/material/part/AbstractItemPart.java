package com.circulation.singularity_engineering_core.material.part;

import com.circulation.singularity_engineering_core.SingularityEngineeringCore;
import com.circulation.singularity_engineering_core.material.IMaterial;
import com.circulation.singularity_engineering_core.material.item.MaterialItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * 产生 {@link Item} 的部件基类。
 * <p>
 * 子类实现 {@link #createItem(IMaterial)} 提供具体的 {@link MaterialItem} 实例，注册与模型绑定均在此处理。
 */
public abstract class AbstractItemPart extends AbstractPart {

    protected AbstractItemPart(String id) {
        super(id);
    }

    /**
     * 工厂方法：为给定材料创建 {@link MaterialItem}。
     * 返回的物品不得已被注册，注册由 {@link #registerItems} 完成。
     */
    protected abstract MaterialItem createItem(IMaterial material);

    @Override
    public void registerItems(IMaterial material, IForgeRegistry<Item> registry) {
        MaterialItem item = createItem(material);
        registry.register(item);
    }

    @Override
    public void registerModels(IMaterial material) {
        com.circulation.singularity_engineering_core.material.model.MaterialModelLoader
            .INSTANCE.registerItem(SingularityEngineeringCore.MOD_ID, material, this);
    }
}
