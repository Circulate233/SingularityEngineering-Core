package com.circulation.singularity_engineering_core.type;

import com.circulation.singularity_engineering_core.handler.ThermodynamicsHandler;
import com.circulation.singularity_engineering_core.management.Thermodynamics;
import crafttweaker.annotations.ZenRegister;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;
import lombok.Getter;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

import static com.circulation.singularity_engineering_core.crt.CrtAPI.CrtName;

@ZenRegister
@ZenClass(CrtName + "type.ThermodynamicsType")
public class ThermodynamicsType implements TemplateType {

    @Getter
    private float maxValue = Thermodynamics.maxTemperature;
    @Getter
    private float minValue = Thermodynamics.minTemperature;
    private float defValue = 298.15f;
    private float heatingSpeed = 0.0f;
    private float coolDownSpeed = 0.0f;

    @Override
    public ThermodynamicsType setMaxValue(float value) {
        maxValue = value;
        return this;
    }

    @Override
    public ThermodynamicsType setMinValue(float value) {
        minValue = value;
        return this;
    }

    @Override
    public float getDefValue(TileMultiblockMachineController ctrl) {
        float def;
        if ((def = ThermodynamicsHandler.getDefBiomeTemperature(
            ctrl.getWorld().getBiome(ctrl.getPos()).biomeName)) >= 0) {
            return def;
        } else if ((def = ThermodynamicsHandler.getDefWorldTemperature(
            ctrl.getWorld().provider.getDimension())) >= 0) {
            return def;
        }

        return defValue;
    }

    @Override
    public TemplateType setDefValue(float value) {
        defValue = value;
        return this;
    }

    @ZenGetter("heatingSpeed")
    public float getHeatingSpeed() {
        return heatingSpeed;
    }

    public ThermodynamicsType setHeatingSpeed(float heatingSpeed) {
        this.heatingSpeed = heatingSpeed;
        return this;
    }

    @ZenGetter("coolDownSpeed")
    public float getCoolDownSpeed() {
        return coolDownSpeed;
    }

    public ThermodynamicsType setCoolDownSpeed(float coolDownSpeed) {
        this.coolDownSpeed = coolDownSpeed;
        return this;
    }
}