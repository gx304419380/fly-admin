package com.fly.admin.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/9/18
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AppDto {
    private String name;
    private String entry;
    private String container;
    private String activeRule;
}
