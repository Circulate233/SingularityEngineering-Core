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
    private static final Map<String, CreativeTabs> CREATIVE_TABS = new LinkedHashMap<>();
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

    public static String registerCreativeTab(String id, String iconItemString) {
        CREATIVE_TABS.put(id, new MaterialCreativeTab(id, iconItemString));
        return id;
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

    public static CreativeTabs resolveCreativeTab(String id) {
        if (id == null || id.isEmpty()) {
            return creativeTabs;
        }
        CreativeTabs customTab = CREATIVE_TABS.get(id);
        if (customTab != null) {
            return customTab;
        }
        return switch (id.toUpperCase()) {
            case "BUILDING_BLOCKS" -> CreativeTabs.BUILDING_BLOCKS;
            case "DECORATIONS" -> CreativeTabs.DECORATIONS;
            case "REDSTONE" -> CreativeTabs.REDSTONE;
            case "TRANSPORTATION" -> CreativeTabs.TRANSPORTATION;
            case "MISC" -> CreativeTabs.MISC;
            case "SEARCH" -> CreativeTabs.SEARCH;
            case "FOOD" -> CreativeTabs.FOOD;
            case "TOOLS" -> CreativeTabs.TOOLS;
            case "COMBAT" -> CreativeTabs.COMBAT;
            case "BREWING" -> CreativeTabs.BREWING;
            case "MATERIALS" -> CreativeTabs.MATERIALS;
            case "INVENTORY" -> CreativeTabs.INVENTORY;
            case "HOTBAR" -> CreativeTabs.HOTBAR;
            default -> creativeTabs;
        };
    }

    /**
     * 清除所有注册项，用于支持 ZenUtils 脚本热重载。
     */
    public static void clear() {
        MATERIALS.clear();
        PARTS.clear();
        CREATIVE_TABS.clear();
    }
}
