package com.circulation.singularity_engineering_core.material.crt;

import com.circulation.singularity_engineering_core.material.MaterialRegistry;
import com.circulation.singularity_engineering_core.material.part.AbstractBlockPart;
import com.circulation.singularity_engineering_core.material.part.IPart;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.block.SoundType;
import net.minecraft.util.BlockRenderLayer;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Locale;

import static com.circulation.singularity_engineering_core.crt.CrtAPI.CrtName;

@ZenRegister
@ZenClass(CrtName + "BlockPartBuilder")
public class CrtBlockPartBuilder {

    private final String id;
    private boolean usesMaterialColor = false;
    private Float hardness;
    private Float resistance;
    private SoundType soundType = SoundType.METAL;
    private String harvestTool;
    private int harvestLevel = 0;
    private int lightValue = 0;
    private Integer lightOpacity;
    private Float slipperiness;
    private boolean fullBlock = true;
    private boolean translucent = false;
    private BlockRenderLayer renderLayer = BlockRenderLayer.SOLID;
    private String creativeTab;

    CrtBlockPartBuilder(String id) {
        this.id = id;
    }

    @ZenMethod
    public CrtBlockPartBuilder tinted() {
        this.usesMaterialColor = true;
        return this;
    }

    @ZenMethod
    public CrtBlockPartBuilder hardness(float hardness) {
        this.hardness = hardness;
        return this;
    }

    @ZenMethod
    public CrtBlockPartBuilder resistance(float resistance) {
        this.resistance = resistance;
        return this;
    }

    @ZenMethod
    public CrtBlockPartBuilder soundType(String soundType) {
        this.soundType = parseSoundType(soundType);
        return this;
    }

    @ZenMethod
    public CrtBlockPartBuilder harvestTool(String harvestTool) {
        this.harvestTool = harvestTool;
        return this;
    }

    @ZenMethod
    public CrtBlockPartBuilder harvestLevel(int harvestLevel) {
        this.harvestLevel = harvestLevel;
        return this;
    }

    @ZenMethod
    public CrtBlockPartBuilder lightValue(int lightValue) {
        this.lightValue = lightValue;
        return this;
    }

    @ZenMethod
    public CrtBlockPartBuilder lightOpacity(int lightOpacity) {
        this.lightOpacity = lightOpacity;
        return this;
    }

    @ZenMethod
    public CrtBlockPartBuilder slipperiness(float slipperiness) {
        this.slipperiness = slipperiness;
        return this;
    }

    @ZenMethod
    public CrtBlockPartBuilder fullBlock(boolean fullBlock) {
        this.fullBlock = fullBlock;
        return this;
    }

    @ZenMethod
    public CrtBlockPartBuilder translucent(boolean translucent) {
        this.translucent = translucent;
        return this;
    }

    @ZenMethod
    public CrtBlockPartBuilder renderLayer(String renderLayer) {
        this.renderLayer = parseRenderLayer(renderLayer);
        return this;
    }

    @ZenMethod
    public CrtBlockPartBuilder creativeTab(String creativeTab) {
        this.creativeTab = creativeTab;
        return this;
    }

    @ZenMethod
    public IPart build() {
        IPart part = new AbstractBlockPart(
            id,
            usesMaterialColor,
            hardness,
            resistance,
            soundType,
            harvestTool,
            harvestLevel,
            lightValue,
            lightOpacity,
            slipperiness,
            fullBlock,
            translucent,
            renderLayer,
            creativeTab
        ) {
        };
        MaterialRegistry.registerPart(part);
        return part;
    }

    private static SoundType parseSoundType(String soundType) {
        return switch (soundType.toUpperCase(Locale.ROOT)) {
            case "WOOD" -> SoundType.WOOD;
            case "GROUND" -> SoundType.GROUND;
            case "PLANT" -> SoundType.PLANT;
            case "STONE" -> SoundType.STONE;
            case "METAL" -> SoundType.METAL;
            case "GLASS" -> SoundType.GLASS;
            case "CLOTH" -> SoundType.CLOTH;
            case "SAND" -> SoundType.SAND;
            case "SNOW" -> SoundType.SNOW;
            case "LADDER" -> SoundType.LADDER;
            case "ANVIL" -> SoundType.ANVIL;
            case "SLIME" -> SoundType.SLIME;
            default -> throw new IllegalArgumentException("Unknown sound type: " + soundType);
        };
    }

    private static BlockRenderLayer parseRenderLayer(String renderLayer) {
        return switch (renderLayer.toUpperCase(Locale.ROOT)) {
            case "SOLID" -> BlockRenderLayer.SOLID;
            case "CUTOUT_MIPPED" -> BlockRenderLayer.CUTOUT_MIPPED;
            case "CUTOUT" -> BlockRenderLayer.CUTOUT;
            case "TRANSLUCENT" -> BlockRenderLayer.TRANSLUCENT;
            default -> throw new IllegalArgumentException("Unknown render layer: " + renderLayer);
        };
    }
}
