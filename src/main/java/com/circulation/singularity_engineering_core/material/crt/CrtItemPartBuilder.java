package com.circulation.singularity_engineering_core.material.crt;

import com.circulation.singularity_engineering_core.material.MaterialRegistry;
import com.circulation.singularity_engineering_core.material.part.AbstractItemPart;
import com.circulation.singularity_engineering_core.material.part.IPart;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static com.circulation.singularity_engineering_core.crt.CrtAPI.CrtName;

@ZenRegister
@ZenClass(CrtName + "ItemPartBuilder")
public class CrtItemPartBuilder {

    private final String id;
    private boolean usesMaterialColor = false;
    private int maxStackSize = 64;
    private int maxDamage = 0;
    private boolean oreDict = true;
    private String creativeTab;

    CrtItemPartBuilder(String id) {
        this.id = id;
    }

    @ZenMethod
    public CrtItemPartBuilder tinted() {
        this.usesMaterialColor = true;
        return this;
    }

    @ZenMethod
    public CrtItemPartBuilder maxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
        return this;
    }

    @ZenMethod
    public CrtItemPartBuilder maxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
        return this;
    }

    @ZenMethod
    public CrtItemPartBuilder oreDict(boolean oreDict) {
        this.oreDict = oreDict;
        return this;
    }

    @ZenMethod
    public CrtItemPartBuilder creativeTab(String creativeTab) {
        this.creativeTab = creativeTab;
        return this;
    }

    @ZenMethod
    public IPart build() {
        IPart part = new AbstractItemPart(id, usesMaterialColor, maxStackSize, maxDamage, oreDict, creativeTab) {
        };
        MaterialRegistry.registerPart(part);
        return part;
    }
}
