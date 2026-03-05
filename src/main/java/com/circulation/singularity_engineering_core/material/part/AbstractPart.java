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

    protected AbstractPart(String id) {
        this.id = id;
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
}
