package com.circulation.singularity_engineering_core.material;

import com.circulation.singularity_engineering_core.material.part.IPart;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@link IMaterial} 和 {@link IPart} 的中央静态注册表。
 * <p>
 * 材料与部件均以插入顺序存储，键为各自的 {@code id}。
 * 必须在 Forge {@code RegistryEvent} 触发之前完成注册（即在 preInit 或 CRT preInit 脚本中）。
 */
public final class MaterialRegistry {

    private static final Map<String, IMaterial> MATERIALS = new LinkedHashMap<>();
    private static final Map<String, IPart> PARTS = new LinkedHashMap<>();
    public static final CreativeTabs creativeTabs = new CreativeTabs("materials") {
        @Override
        public @NotNull ItemStack createIcon() {
            return ItemStack.EMPTY;
        }
    };

    private MaterialRegistry() {
    }

    public static IMaterial registerMaterial(IMaterial material) {
        MATERIALS.put(material.getId(), material);
        return material;
    }

    public static IPart registerPart(IPart part) {
        PARTS.put(part.getId(), part);
        return part;
    }

    public static IMaterial getMaterial(String id) {
        return MATERIALS.get(id);
    }

    public static IPart getPart(String id) {
        return PARTS.get(id);
    }

    public static Collection<IMaterial> getMaterials() {
        return Collections.unmodifiableCollection(MATERIALS.values());
    }

    public static Collection<IPart> getParts() {
        return Collections.unmodifiableCollection(PARTS.values());
    }

    /**
     * 清除所有注册项，用于支持 ZenUtils 脚本热重载。
     */
    public static void clear() {
        MATERIALS.clear();
        PARTS.clear();
    }
}
