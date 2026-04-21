package com.circulation.singularity_engineering_core.material;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

final class MaterialCreativeTab extends CreativeTabs {

    private final String iconItemString;

    MaterialCreativeTab(String id, String iconItemString) {
        super(id);
        this.iconItemString = iconItemString;
    }

    @Override
    public @NotNull ItemStack createIcon() {
        ItemStack resolved = resolveIconItemStack();
        if (!resolved.isEmpty()) {
            return resolved;
        }
        return new ItemStack(Items.IRON_INGOT);
    }

    private ItemStack resolveIconItemStack() {
        if (iconItemString == null || iconItemString.isEmpty()) {
            return ItemStack.EMPTY;
        }
        int meta = 0;
        String itemRegistryName = iconItemString;
        int lastColon = iconItemString.lastIndexOf(':');
        if (lastColon > 0 && lastColon < iconItemString.length() - 1) {
            String tail = iconItemString.substring(lastColon + 1);
            if (isInteger(tail)) {
                itemRegistryName = iconItemString.substring(0, lastColon);
                meta = Integer.parseInt(tail);
            }
        }
        Item item = Item.getByNameOrId(itemRegistryName);
        return item == null ? ItemStack.EMPTY : new ItemStack(item, 1, meta);
    }

    private static boolean isInteger(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return !value.isEmpty();
    }
}
