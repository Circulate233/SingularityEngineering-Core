package com.circulation.singularity_engineering_core.crt.thermodynamics;

import com.circulation.singularity_engineering_core.handler.PunishmentHandler;
import com.circulation.singularity_engineering_core.management.Thermodynamics;
import com.circulation.singularity_engineering_core.utils.FormatUtils;
import crafttweaker.annotations.ZenRegister;
import hellfirepvp.modularmachinery.common.integration.crafttweaker.RecipePrimer;
import net.minecraft.util.text.translation.I18n;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

import static com.circulation.singularity_engineering_core.SingularityEngineeringCore.isClient;

@ZenRegister
@ZenExpansion("mods.modularmachinery.RecipePrimer")
public class RecipePrimerT {

    @ZenMethod
    @NotNull
    public static RecipePrimer setHeatDemand(
            @NotNull RecipePrimer primer,
            float min,
            @Optional(valueDouble = Thermodynamics.maxTemperature) float max) {
        primer.addPreCheckHandler(event -> {
            var ctrl = event.getController();
            var system = Thermodynamics.getSystem(ctrl);
            if (system == null) return;
            var heat = system.getValue();
            if (heat < min || heat > max) {
                event.setFailed("温度不符合需求区间");
            }
        }).addPreTickHandler(event -> {
            var ctrl = event.getController();
            var system = Thermodynamics.getSystem(ctrl);
            if (system == null) return;
            var heat = system.getValue();
            if (heat < min || heat > max) {
                event.preventProgressing("温度不符合需求区间");
            }
        }).addFactoryPreTickHandler(event -> {
            var ctrl = event.getController();
            var system = Thermodynamics.getSystem(ctrl);
            if (system == null) return;
            var heat = system.getValue();
            if (heat < min || heat > max) {
                event.preventProgressing("温度不符合需求区间");
            }
        });

        if (isClient)
            primer.addRecipeTooltip(
                    I18n.translateToLocalFormatted(
                            "text.heat_demand",
                            FormatUtils.formatFloat(min, 2),
                            FormatUtils.formatFloat(max, 2)
                    )
            );

        return primer;
    }

    @ZenMethod
    @NotNull
    public static RecipePrimer setPunish(
            @NotNull RecipePrimer primer,
            int level
    ) {
        Thermodynamics.addRecipePunish(primer.getRecipeRegistryName(), level);
        primer.addRecipeTooltip("text.punish.level." + level);
        return primer;
    }

    @ZenMethod
    public static RecipePrimer addHeatChance(
            @NotNull RecipePrimer primer,
            float chance,
            @Optional(valueBoolean = true) boolean isHeating
    ) {
        return primer.addFinishHandler(event -> {
            var ctrl = event.getController();
            var system = Thermodynamics.getSystem(ctrl);
            var parallelism = event.getActiveRecipe().getParallelism();
            if (system == null) return;
            final var success = isHeating ? system.addValue(chance) : system.reduceValue(chance);
            int punishLevel = Thermodynamics.getPunishLevel(primer);

            if (!success) {
                switch (PunishmentHandler.fromLevelGetPunish(punishLevel)) {

                }
            }
        }).addFactoryFinishHandler(event -> {
            var ctrl = event.getController();
            var system = Thermodynamics.getSystem(ctrl);
            var parallelism = event.getActiveRecipe().getParallelism();
            if (system == null) return;
            final var success = isHeating ? system.addValue(chance) : system.reduceValue(chance);
            int punishLevel = Thermodynamics.getPunishLevel(primer);

            if (!success) {
                switch (PunishmentHandler.fromLevelGetPunish(punishLevel)) {

                }
            }
        });
    }

    @ZenMethod
    public static RecipePrimer addTickHeatChance(
            @NotNull RecipePrimer primer,
            float chance,
            @Optional(valueBoolean = true) boolean isHeating
    ) {
        return primer.addPostTickHandler(event -> {
            var ctrl = event.getController();
            var system = Thermodynamics.getSystem(ctrl);
            var parallelism = event.getActiveRecipe().getParallelism();
            if (system == null) return;
            final var success = isHeating ? system.addValue(chance) : system.reduceValue(chance);
            int punishLevel = Thermodynamics.getPunishLevel(primer);

            if (!success) {
                switch (PunishmentHandler.fromLevelGetPunish(punishLevel)) {
                    case PAUSE_RECIPE -> {
                        event.preventProgressing("温度抵达了上/下限!");
                        return;
                    }
                }
            }
        }).addFactoryPostTickHandler(event -> {
            var ctrl = event.getController();
            var system = Thermodynamics.getSystem(ctrl);
            var parallelism = event.getActiveRecipe().getParallelism();
            if (system == null) return;
            final var success = isHeating ? system.addValue(chance) : system.reduceValue(chance);
            int punishLevel = Thermodynamics.getPunishLevel(primer);

            if (!success) {
                switch (PunishmentHandler.fromLevelGetPunish(punishLevel)) {
                    case PAUSE_RECIPE -> {
                        event.preventProgressing("温度抵达了上/下限!");
                        return;
                    }
                }
            }
        });
    }
}