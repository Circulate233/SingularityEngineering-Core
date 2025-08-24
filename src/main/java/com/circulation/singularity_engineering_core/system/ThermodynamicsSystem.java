package com.circulation.singularity_engineering_core.system;

import com.circulation.singularity_engineering_core.type.ThermodynamicsType;
import crafttweaker.annotations.ZenRegister;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;
import net.minecraft.nbt.NBTTagCompound;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import org.jetbrains.annotations.NotNull;

import static com.circulation.singularity_engineering_core.crt.CrtAPI.CrtName;

@ZenRegister
@ZenClass(CrtName + "system.ThermodynamicsSystem")
public class ThermodynamicsSystem extends TemplateSystem<ThermodynamicsType> {

    private static final String valueName = "heat";

    public ThermodynamicsSystem(ThermodynamicsType type, TileMultiblockMachineController ctrl) {
        super(type, ctrl);
    }

    @ZenMethod
    public float getHeatingSpeed() {
        return type.getHeatingSpeed();
    }

    @ZenMethod
    public float getCoolDownSpeed() {
        return type.getCoolDownSpeed();
    }

    @Override
    public void readNBT(@NotNull NBTTagCompound tag) {
        if (tag.hasKey(valueName)) {
            this.value = tag.getFloat(valueName);
        } else {
            this.value = getDefValue();
        }
    }

    @Override
    public void writeNBT(@NotNull NBTTagCompound tag) {
        tag.setFloat(valueName, this.value);
    }
}