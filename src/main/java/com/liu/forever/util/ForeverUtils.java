package com.liu.forever.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 工具类
 *
 * @author Admin
 * @date 2023/07/21
 */
public class ForeverUtils {




    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }


    public static void copyProperties(Object source, Object target) {

        BeanUtil.copyProperties(source, target, CopyOptions.create().setIgnoreNullValue(true));
    }
}
