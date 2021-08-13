package com.fly.admin.system.mapper;

import com.fly.admin.system.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/12
 */
@Mapper
public interface UserXmlMapper {
    User search(String name);
}
