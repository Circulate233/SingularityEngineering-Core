package com.circulation.singularity_engineering_core.material.crt;

import com.circulation.singularity_engineering_core.material.MaterialRegistry;
import com.circulation.singularity_engineering_core.material.part.AbstractFluidPart;
import com.circulation.singularity_engineering_core.material.part.IPart;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static com.circulation.singularity_engineering_core.crt.CrtAPI.CrtName;

@ZenRegister
@ZenClass(CrtName + "FluidPartBuilder")
public class CrtFluidPartBuilder {

    private final String id;
    private boolean usesMaterialColor = false;
    private String stillTexture;
    private String flowingTexture;
    private int baseColor = 0xFFFFFFFF;
    private int density = 1000;
    private int temperature = 300;
    private int viscosity = 1000;
    private int luminosity = 0;
    private boolean gaseous = false;
    private boolean bucket = true;

    CrtFluidPartBuilder(String id) {
        this.id = id;
    }

    @ZenMethod
    public CrtFluidPartBuilder tinted() {
        this.usesMaterialColor = true;
        return this;
    }

    @ZenMethod
    public CrtFluidPartBuilder stillTexture(String stillTexture) {
        this.stillTexture = stillTexture;
        return this;
    }

    @ZenMethod
    public CrtFluidPartBuilder flowingTexture(String flowingTexture) {
        this.flowingTexture = flowingTexture;
        return this;
    }

    @ZenMethod
    public CrtFluidPartBuilder baseColor(int baseColor) {
        this.baseColor = baseColor;
        return this;
    }

    @ZenMethod
    public CrtFluidPartBuilder density(int density) {
        this.density = density;
        return this;
    }

    @ZenMethod
    public CrtFluidPartBuilder temperature(int temperature) {
        this.temperature = temperature;
        return this;
    }

    @ZenMethod
    public CrtFluidPartBuilder viscosity(int viscosity) {
        this.viscosity = viscosity;
        return this;
    }

    @ZenMethod
    public CrtFluidPartBuilder luminosity(int luminosity) {
        this.luminosity = luminosity;
        return this;
    }

    @ZenMethod
    public CrtFluidPartBuilder gaseous(boolean gaseous) {
        this.gaseous = gaseous;
        return this;
    }

    @ZenMethod
    public CrtFluidPartBuilder bucket(boolean bucket) {
        this.bucket = bucket;
        return this;
    }

    @ZenMethod
    public IPart build() {
        IPart part = new AbstractFluidPart(
            id,
            usesMaterialColor,
            stillTexture,
            flowingTexture,
            baseColor,
            density,
            temperature,
            viscosity,
            luminosity,
            gaseous,
            bucket
        ) {
        };
        MaterialRegistry.registerPart(part);
        return part;
    }
}
