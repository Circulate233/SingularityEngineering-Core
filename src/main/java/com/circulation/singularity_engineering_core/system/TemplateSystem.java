package com.circulation.singularity_engineering_core.system;

import com.circulation.singularity_engineering_core.type.TemplateType;
import crafttweaker.annotations.ZenRegister;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;
import net.minecraft.nbt.NBTTagCompound;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

import java.lang.ref.WeakReference;

import static com.circulation.singularity_engineering_core.crt.CrtAPI.CrtName;

@ZenRegister
@ZenClass(CrtName + "TemplateSystem")
public abstract class TemplateSystem<T extends TemplateType> {

    protected final T type;
    protected volatile float value;
    protected final WeakReference<TileMultiblockMachineController> ctrl;
    protected volatile TemperatureStatus status;

    public TemplateSystem(T type, TileMultiblockMachineController ctrl) {
        this.type = type;
        this.ctrl = new WeakReference<>(ctrl);
        readNBT(ctrl.getCustomDataTag());
        changeStatus();
    }

    @ZenGetter("value")
    public float getValue() {
        var c = ctrl.get();
        if (c == null) throw new NullPointerException("The controller has been deleted but is still running");
        readNBT(c.getCustomDataTag());
        return value;
    }

    @ZenSetter("value")
    public void setCrtValue(float value) {
        setValue(value);
    }

    @ZenMethod
    public synchronized boolean addValue(float value) {
        if (value == 0) return true;
        boolean success;
        float newValue = this.value + value;
        if (newValue > getMaxValue()) {
            this.value = getMaxValue();
            success = false;
        } else {
            this.value = newValue;
            success = true;
        }
        var c = ctrl.get();
        if (c == null) throw new NullPointerException("The controller has been deleted but is still running");
        final var nbt = c.getCustomDataTag();
        writeNBT(nbt);
        changeStatus();
        return success;
    }

    @ZenMethod
    public synchronized boolean reduceValue(float value) {
        if (value == 0) return true;
        boolean success;
        float newValue = this.value - value;
        if (newValue < getMinValue()) {
            this.value = getMinValue();
            success = false;
        } else {
            this.value = newValue;
            success = true;
        }
        var c = ctrl.get();
        if (c == null) throw new NullPointerException("The controller has been deleted but is still running");
        final var nbt = c.getCustomDataTag();
        writeNBT(nbt);
        changeStatus();
        return success;
    }

    public synchronized TemplateSystem<T> setValue(float value) {
        if (value == this.value) return this;
        if (value > getMaxValue()) {
            this.value = getMaxValue();
        } else if (value < getMinValue()) {
            this.value = getMinValue();
        } else {
            this.value = value;
        }
        var c = ctrl.get();
        if (c == null) throw new NullPointerException("The controller has been deleted but is still running");
        final var nbt = c.getCustomDataTag();
        writeNBT(nbt);
        changeStatus();
        return this;
    }

    public void changeStatus() {
        var def = getDefValue();
        float tolerance = 1e-4f;

        if (Math.abs(value - def) <= tolerance) {
            status = TemperatureStatus.BALANCE;
        } else if (value > def) {
            status = TemperatureStatus.HIGH;
        } else {
            status = TemperatureStatus.LOW;
        }
    }

    public TemperatureStatus getStatus() {
        return status;
    }

    @ZenMethod
    public final float getMaxValue() {
        return type.getMaxValue();
    }

    @ZenMethod
    public final float getMinValue() {
        return type.getMinValue();
    }

    @ZenMethod
    public final float getDefValue() {
        var c = ctrl.get();
        if (c == null) throw new NullPointerException("The controller has been deleted but is still running");
        return type.getDefValue(c);
    }

    public abstract void readNBT(@NotNull final NBTTagCompound customData);

    public abstract void writeNBT(@NotNull final NBTTagCompound tag);

    public enum TemperatureStatus {
        LOW,
        BALANCE,
        HIGH
    }
}