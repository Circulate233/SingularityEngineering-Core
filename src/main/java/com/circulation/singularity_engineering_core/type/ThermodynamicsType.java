package com.circulation.singularity_engineering_core.type;

import com.circulation.singularity_engineering_core.handler.ThermodynamicsHandler;
import com.circulation.singularity_engineering_core.management.Thermodynamics;
import crafttweaker.annotations.ZenRegister;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

import static com.circulation.singularity_engineering_core.crt.CrtAPI.CrtName;

@ZenRegister
@ZenClass(CrtName + "type.ThermodynamicsType")
public class ThermodynamicsType implements TemplateType {

    private float maxTemperature = Thermodynamics.maxTemperature;
    private float minTemperature = Thermodynamics.minTemperature;
    private float defTemperature = 298.15f;
    private float heatingSpeed = 0.0f;
    private float coolDownSpeed = 0.0f;

    @Override
    public float getMaxValue() {
        return maxTemperature;
    }

    @Override
    public ThermodynamicsType setMaxValue(float value) {
        maxTemperature = value;
        return this;
    }

    @Override
    public float getMinValue() {
        return minTemperature;
    }

    @Override
    public ThermodynamicsType setMinValue(float value) {
        minTemperature = value;
        return this;
    }

    @Override
    public float getDefValue(TileMultiblockMachineController ctrl) {
        float def;
        if ((def = ThermodynamicsHandler.getDefBiomeTemperature(
                ctrl.getWorld().getBiome(ctrl.getPos()).getBiomeName())) >= 0) {
            return def;
        } else if ((def = ThermodynamicsHandler.getDefWorldTemperature(
                ctrl.getWorld().provider.getDimension())) >= 0) {
            return def;
        }

        return defTemperature;
    }

    @Override
    public TemplateType setDefValue(float value) {
        defTemperature = value;
        return this;
    }

    @ZenGetter("heatingSpeed")
    public float getHeatingSpeed() {
        return heatingSpeed;
    }

    @ZenGetter("coolDownSpeed")
    public float getCoolDownSpeed() {
        return coolDownSpeed;
    }

    public ThermodynamicsType setHeatingSpeed(float heatingSpeed) {
        this.heatingSpeed = heatingSpeed;
        return this;
    }

    public ThermodynamicsType setCoolDownSpeed(float coolDownSpeed) {
        this.coolDownSpeed = coolDownSpeed;
        return this;
    }
}