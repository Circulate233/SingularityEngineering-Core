package com.circulation.singularity_engineering_core.proxy;

import com.circulation.singularity_engineering_core.SingularityEngineeringCore;
import com.circulation.singularity_engineering_core.material.MaterialSystem;
import com.circulation.singularity_engineering_core.material.model.MaterialModelLoader;
import com.circulation.singularity_engineering_core.material.model.MaterialResourcePack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

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

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void postInit() {
        super.postInit();
    }

}