package com.circulation.singularity_engineering_core.management;

import com.circulation.singularity_engineering_core.system.ThermodynamicsSystem;
import com.circulation.singularity_engineering_core.type.ThermodynamicsType;
import crafttweaker.annotations.ZenRegister;
import github.kasuminova.mmce.common.helper.IMachineController;
import hellfirepvp.modularmachinery.ModularMachinery;
import hellfirepvp.modularmachinery.common.integration.crafttweaker.RecipePrimer;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.circulation.singularity_engineering_core.crt.CrtAPI.CrtName;

@ZenRegister
@ZenClass(CrtName + "management.Thermodynamics")
public class Thermodynamics {
    private static final Map<TileMultiblockMachineController, ThermodynamicsSystem> CACHED = new ConcurrentHashMap<>();
    private static final Object2IntMap<ResourceLocation> punishCACHED = new Object2IntOpenHashMap<>();
    private static final Object2IntMap<ResourceLocation> punishMachineCACHED = new Object2IntOpenHashMap<>();

    public static void addRecipePunish(ResourceLocation name, int level) {
        punishCACHED.put(name, level);
    }

    public static void addMachinePunish(ResourceLocation name, int level) {
        punishMachineCACHED.put(name, level);
    }

    public static int getPunishLevel(RecipePrimer p) {
        var ctrl = p.getRecipeRegistryName();
        var machine = p.getAssociatedMachineName();

        return punishCACHED.containsKey(p.getRecipeRegistryName())
                ? punishCACHED.getInt(p.getRecipeRegistryName())
                : punishMachineCACHED.getInt(machine);
    }

    public static final float maxTemperature = 1.417e+32f;
    public static final float minTemperature = 0;

    @ZenMethod
    public static ThermodynamicsType getType(String name) {
        return getType(new ResourceLocation(ModularMachinery.MODID, name));
    }

    public static ThermodynamicsType getType(ResourceLocation name) {
        return GlobalManagement.getType(ThermodynamicsType.class, name);
    }

    public static void removeSystem(TileMultiblockMachineController ctrl) {
        CACHED.remove(ctrl);
    }

    @ZenMethod
    public static ThermodynamicsSystem getSystem(@NotNull IMachineController ctrl) {
        return getSystem(ctrl.getController());
    }

    @Nullable
    public static ThermodynamicsSystem getSystem(@NotNull TileMultiblockMachineController ctrl) {
        var machine = ctrl.getFoundMachine();
        if (machine == null) {
            return null;
        }
        var type = getType(machine.getRegistryName());
        if (type == null) {
            return null;
        }

        synchronized (ctrl) {
            var system = CACHED.computeIfAbsent(
                    ctrl,
                    c -> new ThermodynamicsSystem(type, ctrl)
            );
            system.readNBT(ctrl.getCustomDataTag());

            return system;
        }
    }

    public static ThermodynamicsType addType(
            ResourceLocation name,
            ThermodynamicsType system
    ) {
        return GlobalManagement.addType(ThermodynamicsType.class, name, system);
    }
}