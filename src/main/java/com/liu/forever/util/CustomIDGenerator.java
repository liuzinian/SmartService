package com.liu.forever.util;

import cn.hutool.core.util.IdUtil;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * 自定义ID生成器
 *
 * @author Admin
 * @date 2023/07/28
 */
public class CustomIDGenerator extends UUIDGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        return IdUtil.objectId();
    }
}