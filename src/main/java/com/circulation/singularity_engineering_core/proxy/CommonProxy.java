package com.circulation.singularity_engineering_core.proxy;

import com.circulation.singularity_engineering_core.handler.ThermodynamicsHandler;
import com.circulation.singularity_engineering_core.management.GlobalManagement;
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