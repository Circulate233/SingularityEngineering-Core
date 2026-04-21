package com.circulation.singularity_engineering_core.material.part;

import com.circulation.singularity_engineering_core.SingularityEngineeringCore;
import com.circulation.singularity_engineering_core.material.IMaterial;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

/**
 * 产生 {@link Gas} 的部件基类。
 * <p>
 * 气体在材料系统中按 {@code material_part} 命名，并在早期生命周期中直接注册到 Mekanism 的
 * {@link GasRegistry}。同名气体会被复用，以支持脚本热重载。
 */
public abstract class AbstractGasPart extends AbstractPart {

    private final ResourceLocation iconLocation;
    private final int baseColor;
    private final boolean visible;
    private final boolean radiation;

    protected AbstractGasPart(
        String id,
        boolean usesMaterialColor,
        ResourceLocation iconLocation,
        int baseColor,
        boolean visible,
        boolean radiation
    ) {
        super(id, usesMaterialColor);
        this.iconLocation = iconLocation;
        this.baseColor = baseColor;
        this.visible = visible;
        this.radiation = radiation;
    }

    protected Gas createGas(IMaterial material) {
        String gasName = getItemId(material);
        Gas gas = new Gas(gasName, resolveIconLocation(material)) {
            @Override
            public String getLocalizedName() {
                String matName = I18n.format(material.getTranslationKey());
                String partName = I18n.format(AbstractGasPart.this.getTranslationKey());
                return matName + " " + partName;
            }
        };
        applyMutableProperties(gas, material);
        return gas;
    }

    @Override
    public void registerGases(IMaterial material) {
        String gasName = getItemId(material);
        Gas existing = GasRegistry.getGas(gasName);
        if (existing != null) {
            updateExistingGas(existing, material);
            return;
        }

        Gas gas = createGas(material);
        GasRegistry.register(gas);
    }

    private void updateExistingGas(Gas gas, IMaterial material) {
        ResourceLocation expectedIcon = resolveIconLocation(material);
        ResourceLocation existingIcon = gas.getIcon();
        if (expectedIcon != null && existingIcon != null && !expectedIcon.equals(existingIcon)) {
            SingularityEngineeringCore.LOGGER.warn(
                "[MaterialSystem] Gas {} already exists with icon {}, keeping it instead of {}.",
                gas.getName(), existingIcon, expectedIcon);
        }
        applyMutableProperties(gas, material);
    }

    private void applyMutableProperties(Gas gas, IMaterial material) {
        gas.setTranslationKey("gas." + getItemId(material));
        gas.setTint(resolveGasTint(material));
        gas.setVisible(visible);
        gas.setRadiation(radiation);
    }

    private int resolveGasTint(IMaterial material) {
        if (usesMaterialColor()) {
            return material.getColor() == IMaterial.NO_COLOR ? 0xFFFFFF : material.getColor();
        }
        return baseColor & 0xFFFFFF;
    }

    private ResourceLocation resolveIconLocation(IMaterial material) {
        if (iconLocation != null) {
            return iconLocation;
        }
        return new ResourceLocation(SingularityEngineeringCore.MOD_ID, "blocks/" + getItemId(material) + "_gas");
    }
}
