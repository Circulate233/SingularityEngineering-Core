package com.circulation.singularity_engineering_core.material.crt;

import com.circulation.singularity_engineering_core.material.MaterialRegistry;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static com.circulation.singularity_engineering_core.crt.CrtAPI.CrtName;

@ZenRegister
@ZenClass(CrtName + "CreativeTabBuilder")
public class CrtCreativeTabBuilder {

    private final String id;
    private String iconItemString;

    CrtCreativeTabBuilder(String id) {
        this.id = id;
    }

    @ZenMethod
    public CrtCreativeTabBuilder icon(String itemString) {
        this.iconItemString = itemString;
        return this;
    }

    @ZenMethod
    public String build() {
        MaterialRegistry.registerCreativeTab(id, iconItemString);
        return id;
    }
}
