package com.fly.admin.system.dto;

import com.fly.admin.common.dto.OrderDto;
import lombok.Data;

import java.util.List;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/18
 */
@Data
public class UserCondition {
    private Integer pageNo;
    private Integer pageSize;
    private String username;
    private String phone;
    private String groupId;
    private List<OrderDto> orderList;
}
