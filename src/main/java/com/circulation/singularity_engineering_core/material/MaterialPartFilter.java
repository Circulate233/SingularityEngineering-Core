package com.circulation.singularity_engineering_core.material;

import com.circulation.singularity_engineering_core.material.part.IPart;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.circulation.singularity_engineering_core.crt.CrtAPI.CrtName;

/**
 * 持有一组 {@link IPart} 引用和一个 {@link MaterialFilterMode}，共同决定哪些部件可以为给定材料注册。
 * <p>
 * 白名单模式：仅允许集合中的部件。<br>
 * 黑名单模式：允许所有部件，集合中的除外。
 */
@ZenRegister
@ZenClass(CrtName + "MaterialPartFilter")
public class MaterialPartFilter {

    private final MaterialFilterMode mode;
    private final Set<IPart> parts;

    public MaterialPartFilter(MaterialFilterMode mode, IPart... parts) {
        this.mode = mode;
        this.parts = Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(parts)));
    }

    /**
     * 返回一个允许所有部件的过滤器（空集黑名单）。
     */
    public static MaterialPartFilter allowAll() {
        return new MaterialPartFilter(MaterialFilterMode.BLACKLIST);
    }

    /**
     * 若给定部件被该过滤器允许则返回 {@code true}。
     */
    public boolean allows(IPart part) {
        boolean inSet = parts.contains(part);
        return (mode == MaterialFilterMode.WHITELIST) == inSet;
    }

    @ZenGetter("mode")
    public MaterialFilterMode getMode() {
        return mode;
    }

    @ZenGetter("parts")
    public IPart[] getParts() {
        return parts.toArray(new IPart[0]);
    }
}
