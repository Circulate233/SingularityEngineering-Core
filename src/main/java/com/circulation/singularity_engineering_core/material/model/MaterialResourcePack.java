package com.circulation.singularity_engineering_core.material.model;

import com.circulation.singularity_engineering_core.SingularityEngineeringCore;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存虚拟资源包，为材料×部件组合在运行时动态提供 blockstate、方块模型和物品模型的 JSON 内容，
 * 无需在磁盘上写出物理文件即可让 Forge 模型系统正常加载。
 * <p>
 * <b>生命周期：</b>
 * <ol>
 *   <li>{@code ClientProxy.preInit()} 中注册到 {@code Minecraft.defaultResourcePacks}，
 *       此时资源包内容为空，但已预置正确的 domain，确保 {@code refreshResources()} 时
 *       Forge 会将本包加入对应 domain 的资源通道。</li>
 *   <li>{@code ModelRegistryEvent} 触发时，{@link MaterialModelLoader} 调用
 *       {@link #put(String, String, String)} 为每个材料×部件组合填充 JSON。</li>
 *   <li>Forge 模型烘焙阶段通过 {@link #resourceExists}/{@link #getInputStream}
 *       惰性读取所注册的资源。由于步骤 2 先于实际烘焙，此时包内容已就绪。</li>
 * </ol>
 * <p>
 * 资源键格式（均含 {@code .json} 后缀）：
 * <ul>
 *   <li>blockstate：{@code ResourceLocation(modId, "blockstates/<blockId>.json")}</li>
 *   <li>方块模型：{@code ResourceLocation(modId, "models/block/<blockId>.json")}</li>
 *   <li>物品模型：{@code ResourceLocation(modId, "models/item/<itemId>.json")}</li>
 * </ul>
 */
@SideOnly(Side.CLIENT)
public enum MaterialResourcePack implements IResourcePack {
    INSTANCE;

    /**
     * ResourceLocation（含 .json 后缀）→ JSON 字符串内容。
     */
    private final Map<ResourceLocation, String> resources = new ConcurrentHashMap<>();

    /**
     * 已注册资源的 modId 集合。预置本 mod 的域名，保证 {@code refreshResources()} 时
     * 资源管理器已将本包加入 {@code singularity_engineering_core} domain 的通道。
     */
    private final Set<String> domains = Collections.newSetFromMap(new ConcurrentHashMap<>());

    MaterialResourcePack() {
        domains.add(SingularityEngineeringCore.MOD_ID);
    }

    /**
     * 向资源包注册一条合成 JSON 资源，若该路径已存在则跳过（资源包文件优先级更高）。
     *
     * @param domain modId，例如 {@code "singularity_engineering_core"}
     * @param path   相对于 {@code assets/<domain>/} 的文件路径（含 .json 后缀），
     *               例如 {@code "blockstates/iron_ore.json"}
     * @param json   JSON 字符串内容
     */
    public void put(String domain, String path, String json) {
        resources.putIfAbsent(new ResourceLocation(domain, path), json);
        domains.add(domain);
    }

    /**
     * 清空所有已注册的合成资源，供资源重载时（{@link MaterialModelLoader#onResourceManagerReload}）调用。
     * 清空后保留预置的 mod domain，避免下次 {@code refreshResources()} 时丢失注册。
     */
    public void clear() {
        resources.clear();
        domains.clear();
        domains.add(SingularityEngineeringCore.MOD_ID);
    }

    @Override
    public @NotNull InputStream getInputStream(@NotNull ResourceLocation location) throws IOException {
        String json = resources.get(location);
        if (json == null) {
            throw new FileNotFoundException("MaterialResourcePack: " + location);
        }
        return new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean resourceExists(@NotNull ResourceLocation location) {
        return resources.containsKey(location);
    }

    @Override
    public @NotNull Set<String> getResourceDomains() {
        return Collections.unmodifiableSet(domains);
    }

    @Override
    public <T extends IMetadataSection> T getPackMetadata(@NotNull MetadataSerializer serializer, @NotNull String sectionName) {
        return null;
    }

    @Override
    public @NotNull BufferedImage getPackImage() throws IOException {
        throw new FileNotFoundException("MaterialResourcePack has no pack.png");
    }

    @Override
    public @NotNull String getPackName() {
        return "SingularityEngineering MaterialSystem [Generated]";
    }
}
