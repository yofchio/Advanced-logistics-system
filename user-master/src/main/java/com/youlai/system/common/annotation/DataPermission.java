package com.youlai.system.common.annotation;

import java.lang.annotation.*;

/**
 * MP数据权限注解
 * <p>
 * https://gitee.com/baomidou/mybatis-plus/issues/I37I90
 *
 * @author  YANG FUCHAO
 * @since 2022-12-10
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DataPermission {

    /**
     * 数据权限 {@link com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor}
     */
    String deptAlias() default "";

    String deptIdColumnName() default "dept_id";

    String userAlias() default "";

    String userIdColumnName() default "create_by";

}

