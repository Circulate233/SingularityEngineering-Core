package com.circulation.singularity_engineering_core.proxy;

import com.circulation.singularity_engineering_core.SingularityEngineeringCore;
import com.circulation.singularity_engineering_core.material.IMaterial;
import com.circulation.singularity_engineering_core.material.MaterialRegistry;
import com.circulation.singularity_engineering_core.material.MaterialSystem;
import com.circulation.singularity_engineering_core.material.block.MaterialBlock;
import com.circulation.singularity_engineering_core.material.block.MaterialItemBlock;
import com.circulation.singularity_engineering_core.material.item.MaterialItem;
import com.circulation.singularity_engineering_core.material.part.IPart;
import com.circulation.singularity_engineering_core.material.model.MaterialModelLoader;
import com.circulation.singularity_engineering_core.material.model.MaterialResourcePack;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("MethodMayBeStatic")
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        super.preInit();
        ModelLoaderRegistry.registerLoader(MaterialModelLoader.INSTANCE);
        // 将虚拟资源包加入 Minecraft 默认资源包列表，使其参与 refreshResources() 的域注册。
        // 资源内容将在 ModelRegistryEvent（时序更晚，物品/方块已注册完毕）中实际填充。
        try {
            List<IResourcePack> defaultPacks = ReflectionHelper.getPrivateValue(
                Minecraft.class, Minecraft.getMinecraft(),
                "defaultResourcePacks", "field_110449_ao"
            );
            defaultPacks.add(MaterialResourcePack.INSTANCE);
        } catch (Exception e) {
            SingularityEngineeringCore.LOGGER.error(
                "[MaterialSystem] 无法注册虚拟资源包，方块模型将缺失：{}", e.getMessage());
        }
    }

    /**
     * 在 {@code ModelRegistryEvent} 时注册模型：此时 Item/Block 注册事件已经完成，
     * {@code Item.REGISTRY} 和 {@code Block.REGISTRY} 中的条目均已就位，
     * 可安全调用 {@code ModelLoader.setCustomModelResourceLocation} 等 API。
     */
    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        MaterialSystem.clientRegisterModels();
    }

    @SubscribeEvent
    public static void onRegisterBlockColors(ColorHandlerEvent.Block event) {
        Set<Block> blocks = new LinkedHashSet<>();
        for (IMaterial material : MaterialRegistry.getMaterials()) {
            for (IPart part : MaterialRegistry.getParts()) {
                if (!material.getFilter().allows(part) || !part.isApplicableTo(material) || !part.usesMaterialColor()) {
                    continue;
                }
                Block block = Block.REGISTRY.getObject(new ResourceLocation(SingularityEngineeringCore.MOD_ID, part.getItemId(material)));
                if (block instanceof MaterialBlock materialBlock) {
                    blocks.add(materialBlock);
                }
            }
        }

        if (!blocks.isEmpty()) {
            event.getBlockColors().registerBlockColorHandler((state, world, pos, tintIndex) -> {
                if (tintIndex != 0 || !(state.getBlock() instanceof MaterialBlock materialBlock)) {
                    return -1;
                }
                if (!materialBlock.getPart().usesMaterialColor()) {
                    return -1;
                }
                return resolveMaterialColor(materialBlock.getGameMaterial());
            }, blocks.toArray(new Block[0]));
        }
    }

    @SubscribeEvent
    public static void onRegisterItemColors(ColorHandlerEvent.Item event) {
        Set<Item> items = new LinkedHashSet<>();
        for (IMaterial material : MaterialRegistry.getMaterials()) {
            for (IPart part : MaterialRegistry.getParts()) {
                if (!material.getFilter().allows(part) || !part.isApplicableTo(material) || !part.usesMaterialColor()) {
                    continue;
                }
                Item item = Item.REGISTRY.getObject(new ResourceLocation(SingularityEngineeringCore.MOD_ID, part.getItemId(material)));
                if (item != null) {
                    items.add(item);
                }
                Item blockItem = Item.getByNameOrId(SingularityEngineeringCore.MOD_ID + ":" + part.getItemId(material));
                if (blockItem != null) {
                    items.add(blockItem);
                }
            }
        }

        if (!items.isEmpty()) {
            event.getItemColors().registerItemColorHandler(ClientProxy::resolveItemColor, items.toArray(new Item[0]));
        }
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void postInit() {
        super.postInit();
    }

    private static int resolveItemColor(ItemStack stack, int tintIndex) {
        if (tintIndex != 0) {
            return -1;
        }

        Item item = stack.getItem();
        if (item instanceof MaterialItem materialItem) {
            if (!materialItem.getPart().usesMaterialColor()) {
                return -1;
            }
            return resolveMaterialColor(materialItem.getMaterial());
        }
        if (item instanceof MaterialItemBlock itemBlock && itemBlock.getBlock() instanceof MaterialBlock materialBlock) {
            if (!materialBlock.getPart().usesMaterialColor()) {
                return -1;
            }
            return resolveMaterialColor(materialBlock.getGameMaterial());
        }
        return -1;
    }

    private static int resolveMaterialColor(IMaterial fallbackMaterial) {
        IMaterial currentMaterial = MaterialRegistry.getMaterial(fallbackMaterial.getId());
        int color = currentMaterial == null ? fallbackMaterial.getColor() : currentMaterial.getColor();
        return color == IMaterial.NO_COLOR ? -1 : color;
    }

}
