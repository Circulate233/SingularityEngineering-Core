package com.circulation.singularity_engineering_core.material;

/**
 * 控制 {@link MaterialPartFilter} 以白名单还是黑名单模式运行。
 */
public enum MaterialFilterMode {
    /**
     * 仅允许明确列出的部件。
     */
    WHITELIST,
    /**
     * 允许所有部件，已列出的除外。
     */
    BLACKLIST
}
