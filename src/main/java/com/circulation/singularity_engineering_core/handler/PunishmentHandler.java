package com.circulation.singularity_engineering_core.handler;

public enum PunishmentHandler {
    PAUSE_RECIPE,
    PAUSE_CHANGE,
    DESTROY_STRUCTURE,
    EXPLOSION;

    public static PunishmentHandler fromLevelGetPunish(int level){
        return PunishmentHandler.values()[level];
    }
}