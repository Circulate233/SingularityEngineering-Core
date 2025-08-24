package com.circulation.singularity_engineering_core.handler;

import com.circulation.singularity_engineering_core.management.GlobalManagement;
import com.circulation.singularity_engineering_core.management.Thermodynamics;
import com.circulation.singularity_engineering_core.system.ThermodynamicsSystem;
import com.circulation.singularity_engineering_core.type.ThermodynamicsType;
import crafttweaker.annotations.ZenRegister;
import github.kasuminova.mmce.common.event.client.ControllerGUIRenderEvent;
import github.kasuminova.mmce.common.event.machine.MachineTickEvent;
import hellfirepvp.modularmachinery.ModularMachinery;
import hellfirepvp.modularmachinery.common.machine.DynamicMachine;
import hellfirepvp.modularmachinery.common.machine.factory.FactoryRecipeThread;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

import static com.circulation.singularity_engineering_core.SingularityEngineeringCore.isClient;
import static com.circulation.singularity_engineering_core.crt.CrtAPI.CrtName;

@ZenRegister
@ZenClass(CrtName + "ThermodynamicsHelper")
public class ThermodynamicsHandler {

    public static boolean isRegisterAll = true;
    public static ObjectSet<ResourceLocation> exceptionalMachines = new ObjectOpenHashSet<>();

    @ZenMethod
    public static void isRegisterAll(boolean a) {
        isRegisterAll = a;
    }

    @ZenMethod
    public static void addExceptionalMachines(String... name) {
        for (String s : name) {
            exceptionalMachines.add(new ResourceLocation(ModularMachinery.MODID, s));
        }
    }

    @ZenMethod
    public static void setMachinePunish(String name, int level) {
        Thermodynamics.addMachinePunish(new ResourceLocation(ModularMachinery.MODID, name), level);
    }

    public static boolean canRegister(String name) {
        return canRegister(new ResourceLocation(ModularMachinery.MODID, name));
    }

    public static boolean canRegister(ResourceLocation name) {
        return isRegisterAll != exceptionalMachines.contains(name);
    }

    private static final Object2FloatMap<ResourceLocation> maxMap = new Object2FloatOpenHashMap<>();
    private static final Object2FloatMap<ResourceLocation> minMap = new Object2FloatOpenHashMap<>();
    private static final Object2FloatMap<ResourceLocation> defMap = new Object2FloatOpenHashMap<>();
    private static final Int2FloatMap defWorldMap = new Int2FloatOpenHashMap();
    private static final Object2FloatMap<String> defBiomeMap = new Object2FloatOpenHashMap<>();
    private static final Object2FloatMap<ResourceLocation> heatingMap = new Object2FloatOpenHashMap<>();
    private static final Object2FloatMap<ResourceLocation> cooldownMap = new Object2FloatOpenHashMap<>();

    static {
        defWorldMap.defaultReturnValue(-1.0f);
        defBiomeMap.defaultReturnValue(-1.0f);
    }

    @ZenMethod
    public static float getDefWorldTemperature(int dimID) {
        return defWorldMap.get(dimID);
    }

    @ZenMethod
    public static float getDefBiomeTemperature(String biomeID) {
        return defBiomeMap.get(biomeID);
    }

    @ZenMethod
    public static void setDefWorldTemperature(int dimID, float maxTemperature) {
        defWorldMap.put(dimID, maxTemperature);
    }

    @ZenMethod
    public static void setDefBiomeTemperature(String biomeName, float minTemperature) {
        defBiomeMap.put(biomeName, minTemperature);
    }

    @ZenMethod
    public static void setMachineMaxTemperature(String machineName, float maxTemperature) {
        maxMap.put(new ResourceLocation(ModularMachinery.MODID, machineName), maxTemperature);
    }

    @ZenMethod
    public static void setMachineMinTemperature(String machineName, float minTemperature) {
        minMap.put(new ResourceLocation(ModularMachinery.MODID, machineName), minTemperature);
    }

    @ZenMethod
    public static void setMachineDefTemperature(String machineName, float defTemperature) {
        defMap.put(new ResourceLocation(ModularMachinery.MODID, machineName), defTemperature);
    }

    @ZenMethod
    public static void setMachineHeatingSpeed(String machineName, float speed) {
        heatingMap.put(new ResourceLocation(ModularMachinery.MODID, machineName), speed);
    }

    @ZenMethod
    public static void setMachineCoolDownSpeed(String machineName, float speed) {
        cooldownMap.put(new ResourceLocation(ModularMachinery.MODID, machineName), speed);
    }

    public static final FactoryRecipeThread thread = FactoryRecipeThread.createCoreThread("thread.thermodynamics");

    public static void register() {
        GlobalManagement.addPreExecution(machine -> {
            if (canRegister(machine.getRegistryName())) {
                Thermodynamics.addType(machine.getRegistryName(), new ThermodynamicsType());
                if (!thread.getRecipeSet().isEmpty()) {
                    machine.addCoreThread(thread);
                }
                addInfo(machine);
                addNaturalChanges(machine);
            }
        });
    }

    public static void preload() {
        maxMap.clear();
        minMap.clear();
        defMap.clear();
        heatingMap.clear();
        cooldownMap.clear();
        defBiomeMap.clear();
        defWorldMap.clear();
    }

    public static void load() {
        maxMap.object2FloatEntrySet()
                .forEach(e -> Thermodynamics.getType(e.getKey()).setMaxValue(e.getFloatValue()));
        minMap.object2FloatEntrySet()
                .forEach(e -> Thermodynamics.getType(e.getKey()).setMinValue(e.getFloatValue()));
        defMap.object2FloatEntrySet()
                .forEach(e -> Thermodynamics.getType(e.getKey()).setDefValue(e.getFloatValue()));
        heatingMap.object2FloatEntrySet()
                .forEach(e -> Thermodynamics.getType(e.getKey()).setHeatingSpeed(e.getFloatValue()));
        cooldownMap.object2FloatEntrySet()
                .forEach(e -> Thermodynamics.getType(e.getKey()).setCoolDownSpeed(e.getFloatValue()));
    }

    private static void addInfo(DynamicMachine machine) {
        if (!isClient) return;

        machine.addMachineEventHandler(ControllerGUIRenderEvent.class, event -> {
            TileMultiblockMachineController ctrl = event.getController();
            ThermodynamicsSystem system = Thermodynamics.getSystem(ctrl);
            if (system == null) {
                return;
            }

            if (ctrl.getTicksExisted() % 10 == 0) {
                system.readNBT(ctrl.getCustomDataTag());
            }

            List<String> tips = new ObjectArrayList<>();

            tips.add("最大温度:" + system.getMaxValue() + "K");
            tips.add("当前温度:" + system.getValue() + "K");
            tips.add("最低温度:" + system.getMinValue() + "K");

            event.setExtraInfo(tips.toArray(new String[0]));
        });
    }

    private static void addNaturalChanges(DynamicMachine machine) {
        machine.addMachineEventHandler(MachineTickEvent.class, event -> {
            var ctrl = event.getController();
            var system = Thermodynamics.getSystem(ctrl);
            if (system == null) return;
            float value = system.getValue();
            float defValue = system.getDefValue();
            switch (system.getStatus()) {
                case LOW -> system.addValue(system.getHeatingSpeed());
                case HIGH -> system.reduceValue(system.getCoolDownSpeed());
            }
        });
    }

}