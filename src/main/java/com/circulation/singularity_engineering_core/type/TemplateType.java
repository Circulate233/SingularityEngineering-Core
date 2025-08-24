package com.circulation.singularity_engineering_core.type;

import crafttweaker.annotations.ZenRegister;
import github.kasuminova.mmce.common.helper.IMachineController;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

import static com.circulation.singularity_engineering_core.crt.CrtAPI.CrtName;

@ZenRegister
@ZenClass(CrtName + "TemplateType")
public interface TemplateType {

    @ZenGetter("maxValue")
    float getMaxValue();

    TemplateType setMaxValue(float value);

    @ZenGetter("minValue")
    float getMinValue();

    TemplateType setMinValue(float value);

    @ZenMethod
    float getDefValue(TileMultiblockMachineController ctrl);

    @ZenMethod
    default float getDefValue(IMachineController ctrl) {
        return getDefValue(ctrl.getController());
    }

    TemplateType setDefValue(float value);
}