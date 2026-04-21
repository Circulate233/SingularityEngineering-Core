package com.circulation.singularity_engineering_core.material.part;

import com.circulation.singularity_engineering_core.material.IMaterial;

/**
 * 为 {@link IPart} 提供合理的默认实现：
 * <ul>
 *   <li>{@link #getItemId} = {@code material_id + "_" + part_id}</li>
 *   <li>{@link #isApplicableTo} 始终返回 {@code true}</li>
 * </ul>
 * 子类只需重写与默认行为不同的方法。
 */
public abstract class AbstractPart implements IPart {

    private final String id;
    private final boolean usesMaterialColor;

    protected AbstractPart(String id, boolean usesMaterialColor) {
        this.id = id;
        this.usesMaterialColor = usesMaterialColor;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getItemId(IMaterial material) {
        return material.getId() + "_" + id;
    }

    @Override
    public boolean isApplicableTo(IMaterial material) {
        return true;
    }

    @Override
    public boolean usesMaterialColor() {
        return usesMaterialColor;
    }

    protected static String toUpperCamel(String snakeCase) {
        StringBuilder builder = new StringBuilder(snakeCase.length());
        boolean capitalizeNext = true;
        for (int i = 0; i < snakeCase.length(); i++) {
            char current = snakeCase.charAt(i);
            if (current == '_') {
                capitalizeNext = true;
                continue;
            }
            builder.append(capitalizeNext ? Character.toUpperCase(current) : current);
            capitalizeNext = false;
        }
        return builder.toString();
    }
}
