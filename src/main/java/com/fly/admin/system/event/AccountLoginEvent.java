package com.fly.admin.system.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/18
 */
@Data
@AllArgsConstructor
public class AccountLoginEvent {
    private String token;
    private String userId;
    private String username;
}
