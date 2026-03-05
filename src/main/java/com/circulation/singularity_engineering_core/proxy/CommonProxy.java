package com.circulation.singularity_engineering_core.proxy;

import com.circulation.singularity_engineering_core.SingularityEngineeringCore;
import com.circulation.singularity_engineering_core.handler.ThermodynamicsHandler;
import com.circulation.singularity_engineering_core.management.GlobalManagement;
import com.circulation.singularity_engineering_core.material.IMaterial;
import com.circulation.singularity_engineering_core.material.Material;
import com.circulation.singularity_engineering_core.material.MaterialRegistry;
import com.circulation.singularity_engineering_core.material.MaterialSystem;
import com.circulation.singularity_engineering_core.material.item.MaterialItem;
import com.circulation.singularity_engineering_core.material.part.AbstractItemPart;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import youyihj.zenutils.api.reload.ScriptReloadEvent;

public class CommonProxy {

    public void preInit() {
        if (Loader.isModLoaded("zenutils")) {
            MinecraftForge.EVENT_BUS.register(this);
        }
        MaterialRegistry.registerMaterial(Material.builder("air").build());
        MaterialRegistry.registerMaterial(Material.builder("wool").build());
        MaterialRegistry.registerPart(new AbstractItemPart("item") {
            @Override
            protected MaterialItem createItem(IMaterial material) {
                return new MaterialItem(SingularityEngineeringCore.MOD_ID, material, this);
            }
        });
        MaterialRegistry.registerPart(new AbstractItemPart("zzz") {
            @Override
            protected MaterialItem createItem(IMaterial material) {
                return new MaterialItem(SingularityEngineeringCore.MOD_ID, material, this);
            }
        });
        MaterialSystem.registerFluids();
    }

    public void init() {
        ThermodynamicsHandler.register();
    }

    public void postInit() {

    }

    @SubscribeEvent
    @Optional.Method(modid = "zenutils")
    public void onScriptsReloading(ScriptReloadEvent.Pre event) {
        GlobalManagement.clear();
        ThermodynamicsHandler.preload();
    }

    @SubscribeEvent()
    @Optional.Method(modid = "zenutils")
    public void onScriptsReloadedPre(ScriptReloadEvent.Post event) {
        ThermodynamicsHandler.load();
    }
}