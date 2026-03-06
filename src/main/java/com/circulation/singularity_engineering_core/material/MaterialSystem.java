package com.circulation.singularity_engineering_core.material;

import com.circulation.singularity_engineering_core.SingularityEngineeringCore;
import com.circulation.singularity_engineering_core.material.part.AbstractBlockPart;
import com.circulation.singularity_engineering_core.material.part.IPart;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 统笹批量注册所有 {@link IMaterial} × {@link IPart} 组合。
 * <p>
 * 生命周期顺序：
 * <ol>
 *   <li>{@link #registerFluids()} — 订阅 {@code RegistryEvent.Register&lt;Block&gt;}。</li>
 *   <li>{@link #onRegisterBlocks} — 订阅 {@code RegistryEvent.Register&lt;Block&gt;}。</li>
 *   <li>{@link #onRegisterItems} — 订阅 {@code RegistryEvent.Register&lt;Item&gt;}。</li>
 *   <li>{@link #clientRegisterModels()} — 在客户端 {@code ClientProxy.preInit()} 中调用。</li>
 * </ol>
 * 以下情况将跳过某个组合：
 * <ul>
 *   <li>{@code material.getFilter().allows(part)} 返回 {@code false}，或</li>
 *   <li>{@code part.isApplicableTo(material)} 返回 {@code false}。</li>
 * </ul>
 */
@Mod.EventBusSubscriber(modid = SingularityEngineeringCore.MOD_ID)
public final class MaterialSystem {

    private MaterialSystem() {
    }

    public static void registerFluids() {
        for (IMaterial material : MaterialRegistry.getMaterials()) {
            for (IPart part : MaterialRegistry.getParts()) {
                if (!isAllowed(material, part)) continue;
                part.registerFluids(material);
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
        MaterialSystem.registerFluids();
        for (IMaterial material : MaterialRegistry.getMaterials()) {
            for (IPart part : MaterialRegistry.getParts()) {
                if (!isAllowed(material, part)) continue;
                part.registerBlocks(material, event.getRegistry());
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event) {
        for (IMaterial material : MaterialRegistry.getMaterials()) {
            for (IPart part : MaterialRegistry.getParts()) {
                if (!isAllowed(material, part)) continue;
                part.registerItems(material, event.getRegistry());
                if (part instanceof AbstractBlockPart blockPart) {
                    blockPart.registerItemBlock(material, event.getRegistry());
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void clientRegisterModels() {
        for (IMaterial material : MaterialRegistry.getMaterials()) {
            for (IPart part : MaterialRegistry.getParts()) {
                if (!isAllowed(material, part)) continue;
                part.registerModels(material);
            }
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean isAllowed(IMaterial material, IPart part) {
        return material.getFilter().allows(part) && part.isApplicableTo(material);
    }
}
