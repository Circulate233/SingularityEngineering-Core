package com.circulation.singularity_engineering_core.management;

import com.circulation.singularity_engineering_core.type.TemplateType;
import hellfirepvp.modularmachinery.common.machine.DynamicMachine;
import hellfirepvp.modularmachinery.common.machine.MachineRegistry;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class GlobalManagement {

    private static final Map<Class<? extends TemplateType>, Map<ResourceLocation, TemplateType>> systemMap = new ConcurrentHashMap<>();

    private static final List<Consumer<DynamicMachine>> preExecution = new ObjectArrayList<>();

    public static void clear() {
        systemMap.clear();
    }

    public static void addPreExecution(Consumer<DynamicMachine> c) {
        preExecution.add(c);
    }

    public static void preInit() {
        MachineRegistry.getLoadedMachines()
                .forEach(machine ->
                        preExecution.forEach(c -> c.accept(machine))
                );
    }

    public static <S extends TemplateType> ObjectCollection<S> getAllType(Class<S> sClass) {
        return (ObjectCollection<S>) systemMap
                .getOrDefault(sClass, Object2ObjectMaps.emptyMap())
                .values();
    }

    public static <S extends TemplateType> S getType(Class<S> sClass, ResourceLocation name) {
        return (S) systemMap
                .getOrDefault(sClass, Object2ObjectMaps.emptyMap())
                .get(name);
    }

    public static <S extends TemplateType> S addType(
            Class<S> sClass,
            ResourceLocation name,
            S system
    ) {
        return (S) systemMap
                .computeIfAbsent(sClass, c -> new ConcurrentHashMap<>())
                .put(name, system);
    }
}