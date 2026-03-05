package com.circulation.singularity_engineering_core.material.model;

import com.circulation.singularity_engineering_core.material.IMaterial;
import com.circulation.singularity_engineering_core.material.part.AbstractBlockPart;
import com.circulation.singularity_engineering_core.material.part.AbstractItemPart;
import com.circulation.singularity_engineering_core.material.part.IPart;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Forge {@link ICustomModelLoader} 实现，为资源包中没有对应模型文件的
 * {@link com.circulation.singularity_engineering_core.material.IMaterial} × {@link IPart} 组合
 * 自动生成扁平物品模型和 cube_all 方块模型。
 * <p>
 * <b>物品模型</b>以材质 {@code modId:items/material_part} 作为 {@link ItemLayerModel} 提供，
 * 对应路径 {@code assets/<modId>/models/item/<name>.json}。<br>
 * <b>方块物品模型</b>指向方块模型 {@code modId:block/material_part}，
 * 对应路径 {@code assets/<modId>/models/item/<name>.json}。<br>
 * <b>方块模型</b>以重贴图的 {@code block/cube_all}（材质 {@code modId:blocks/material_part}）提供，
 * 对应路径 {@code assets/<modId>/models/block/<name>.json}。
 * <p>
 * 若资源包中已存在某路径对应的真实 JSON 模型文件，Forge 内置加载器会作为降级选项处理；
 * 本加载器对不在 {@link #managedModels} 中的路径返回 {@code false}（{@link #accepts}），
 * 因此手工制作的模型文件始终优先。
 * <p>
 * 在 {@code ClientProxy.preInit()} 中通过 {@link #INSTANCE} 注册，需在任何模型注册调用之前完成。
 */
@SideOnly(Side.CLIENT)
public enum MaterialModelLoader implements ICustomModelLoader {
    INSTANCE;

    /**
     * 需要自动生成的模型路径，格式为 {@code modId:models/item/material_part} 或 {@code modId:models/block/material_part}，
     * 对应资源包路径 {@code assets/<modId>/models/item|block/<name>.json}。
     */
    private final Set<ResourceLocation> managedModels = Collections.synchronizedSet(new LinkedHashSet<>());

    /**
     * 注册物品模型并设置物品的 {@link ModelResourceLocation}，
     * 纹理路径应为 {@code assets/<modId>/textures/items/<material_part>.png}。
     */
    public void registerItem(String modId, IMaterial material, AbstractItemPart part) {
        Item item = Item.REGISTRY.getObject(
            new ResourceLocation(modId, part.getItemId(material)));
        if (item == null) return;

        String itemId = part.getItemId(material);

        // 向虚拟资源包写入物品模型 JSON（扁平 item/generated），作为 ICustomModelLoader 的兜底
        MaterialResourcePack.INSTANCE.put(modId, "models/item/" + itemId + ".json",
            MaterialModelTemplates.itemGenerated(modId + ":items/" + itemId));

        managedModels.add(new ResourceLocation(modId, "models/item/" + itemId));

        ModelLoader.setCustomModelResourceLocation(
            item, 0,
            new ModelResourceLocation(new ResourceLocation(modId, itemId), "inventory"));
    }

    /**
     * 注册方块物品模型并设置 {@link net.minecraft.item.ItemBlock} 的 {@link ModelResourceLocation}，
     * 纹理路径应为 {@code assets/<modId>/textures/blocks/<material_part>.png}。
     * <p>
     * 同时向虚拟资源包写入 blockstate JSON，使方块在世界中的渲染得以正常工作：
     * Forge 通过 blockstate 文件确定方块模型，没有此文件则显示为缺失模型（紫黑格）。
     */
    public void registerBlock(String modId, IMaterial material, AbstractBlockPart part) {
        Item itemBlock = Item.getItemFromBlock(
            Block.REGISTRY.getObject(new ResourceLocation(modId, part.getItemId(material))));

        String blockId = part.getItemId(material);

        // blockstate JSON：将 "normal" 变体映射到方块模型，供方块世界渲染使用
        MaterialResourcePack.INSTANCE.put(modId, "blockstates/" + blockId + ".json",
            "{\"variants\":{\"normal\":{\"model\":\"" + modId + ":block/" + blockId + "\"}}}");

        // 方块模型 JSON（cube_all），作为 ICustomModelLoader 的兜底
        MaterialResourcePack.INSTANCE.put(modId, "models/block/" + blockId + ".json",
            MaterialModelTemplates.blockCubeAll(modId + ":blocks/" + blockId));

        // 物品栏方块物品 JSON：引用父方块模型，与 ICustomModelLoader 保持一致
        MaterialResourcePack.INSTANCE.put(modId, "models/item/" + blockId + ".json",
            "{\"parent\":\"" + modId + ":block/" + blockId + "\"}");

        managedModels.add(new ResourceLocation(modId, "models/item/" + blockId));
        managedModels.add(new ResourceLocation(modId, "models/block/" + blockId));

        ModelLoader.setCustomModelResourceLocation(
            itemBlock, 0,
            new ModelResourceLocation(new ResourceLocation(modId, blockId), "inventory"));
    }

    @Override
    public boolean accepts(@NotNull ResourceLocation modelLocation) {
        return managedModels.contains(modelLocation);
    }

    @Override
    public @NotNull IModel loadModel(ResourceLocation modelLocation) throws Exception {
        String path = modelLocation.getPath();
        String modId = modelLocation.getNamespace();

        if (path.startsWith("models/item/")) {
            String itemId = path.substring("models/item/".length());
            ResourceLocation textureLoc = new ResourceLocation(modId, "items/" + itemId);
            return new ItemLayerModel(ImmutableList.of(textureLoc));

        } else if (path.startsWith("models/block/")) {
            String blockId = path.substring("models/block/".length());
            ResourceLocation textureLoc = new ResourceLocation(modId, "blocks/" + blockId);
            IModel cubeAll = ModelLoaderRegistry.getModel(new ResourceLocation("block/cube_all"));
            return cubeAll.retexture(ImmutableMap.of("all", textureLoc.toString()));
        }

        throw new IllegalArgumentException("MaterialModelLoader: unexpected managed path: " + modelLocation);
    }

    @Override
    public void onResourceManagerReload(@NotNull IResourceManager resourceManager) {
        managedModels.clear();
        MaterialResourcePack.INSTANCE.clear();
    }
}
