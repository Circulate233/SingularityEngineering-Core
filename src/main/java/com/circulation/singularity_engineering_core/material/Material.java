package com.circulation.singularity_engineering_core.material;

import com.circulation.singularity_engineering_core.material.part.IPart;

/**
 * {@link IMaterial} 的不可变数据类实现，通过 {@link Builder} 构建：
 * <pre>{@code
 * IMaterial iron = Material.builder("iron", "Iron")
 *     .whitelist(gearPart, dustPart)
 *     .build();
 * }</pre>
 */
public final class Material implements IMaterial {

    private final String id;
    private final MaterialPartFilter filter;
    private final int color;
    private final boolean enchanted;

    private Material(Builder builder) {
        this.id = builder.id;
        this.filter = builder.filter;
        this.color = builder.color;
        this.enchanted = builder.enchanted;
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public MaterialPartFilter getFilter() {
        return filter;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public boolean isEnchanted() {
        return enchanted;
    }

    public static final class Builder {

        private final String id;
        private MaterialPartFilter filter = MaterialPartFilter.allowAll();
        private int color = IMaterial.NO_COLOR;
        private boolean enchanted = false;

        private Builder(String id) {
            this.id = id;
        }

        /**
         * 仅允许该材料注册列出的部件（白名单模式）。
         */
        public Builder whitelist(IPart... parts) {
            this.filter = new MaterialPartFilter(MaterialFilterMode.WHITELIST, parts);
            return this;
        }

        /**
         * 允许该材料注册所有部件，已列出的除外（黑名单模式）。
         */
        public Builder blacklist(IPart... parts) {
            this.filter = new MaterialPartFilter(MaterialFilterMode.BLACKLIST, parts);
            return this;
        }

        /**
         * 设置材料的渲染颜色，格式为 {@code 0xRRGGBB}。
         * 传入 {@link IMaterial#NO_COLOR} 表示保持默认颜色。
         */
        public Builder color(int color) {
            this.color = color;
            return this;
        }

        /**
         * 启用附魔光效：该材料生成的物品部件及方块 ItemBlock 将显示附魔光效（紫色光晕）。
         */
        public Builder enchanted() {
            this.enchanted = true;
            return this;
        }

        public Material build() {
            return new Material(this);
        }
    }
}
