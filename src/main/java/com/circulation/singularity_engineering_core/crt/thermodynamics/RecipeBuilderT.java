package com.circulation.singularity_engineering_core.crt.thermodynamics;

import com.circulation.singularity_engineering_core.handler.PunishmentHandler;
import com.circulation.singularity_engineering_core.handler.ThermodynamicsHandler;
import com.circulation.singularity_engineering_core.management.Thermodynamics;
import crafttweaker.annotations.ZenRegister;
import hellfirepvp.modularmachinery.common.integration.crafttweaker.RecipeBuilder;
import hellfirepvp.modularmachinery.common.integration.crafttweaker.RecipePrimer;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethodStatic;

@ZenRegister
@ZenExpansion("mods.modularmachinery.RecipeBuilder")
public class RecipeBuilderT {

    @ZenMethodStatic
    public static RecipePrimer newHeatBuilder(
            final String recipeRegistryName,
            final boolean isHeating,
            final float speed,
            final int punishLevel,
            final String associatedMachineRegistryName,
            final int processingTickTime) {
        return newHeatBuilder(
                recipeRegistryName,
                isHeating,
                speed,
                associatedMachineRegistryName,
                processingTickTime,
                0
        );
    }

    @ZenMethodStatic
    public static RecipePrimer newHeatBuilder(
            final String recipeRegistryName,
            final boolean isHeating,
            final float speed,
            final String associatedMachineRegistryName,
            final int processingTickTime,
            final int sortingPriority) {
        return newHeatBuilder(
                recipeRegistryName,
                isHeating,
                speed,
                associatedMachineRegistryName,
                processingTickTime,
                sortingPriority,
                false
        );
    }

    @ZenMethodStatic
    public static RecipePrimer newHeatBuilder(
            final String recipeRegistryName,
            final boolean isHeating,
            final float speed,
            final String associatedMachineRegistryName,
            final int processingTickTime,
            final int sortingPriority,
            final boolean cancelIfPerTickFails) {

        var p = RecipeBuilder.newBuilder(
                recipeRegistryName,
                associatedMachineRegistryName,
                processingTickTime,
                sortingPriority,
                cancelIfPerTickFails
        );

        ThermodynamicsHandler.thread.addRecipe(recipeRegistryName);

        p.setThreadName("thread.thermodynamics");

        return p.addPostTickHandler(event -> {
            var ctrl = event.getController();
            var system = Thermodynamics.getSystem(ctrl);
            var parallelism = event.getActiveRecipe().getParallelism();
            if (system != null) {
                final var success = isHeating ? system.addValue(speed) : system.reduceValue(speed);
                int punishLevel = Thermodynamics.getPunishLevel(p);

                if (!success) {
                    switch (PunishmentHandler.fromLevelGetPunish(punishLevel)) {
                        case PAUSE_RECIPE -> {
                            event.preventProgressing("温度抵达了上/下限!");
                            return;
                        }
                    }
                }
            }
        }).addFactoryPostTickHandler(event -> {
            var ctrl = event.getController();
            var system = Thermodynamics.getSystem(ctrl);
            var parallelism = event.getActiveRecipe().getParallelism();
            if (system != null) {
                final var success = isHeating ? system.addValue(speed) : system.reduceValue(speed);
                int punishLevel = Thermodynamics.getPunishLevel(p);

                if (!success) {
                    switch (PunishmentHandler.fromLevelGetPunish(punishLevel)) {
                        case PAUSE_RECIPE -> {
                            event.preventProgressing("温度抵达了上/下限!");
                            return;
                        }
                    }
                }
            }
        });
    }
}