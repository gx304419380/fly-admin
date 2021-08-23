package com.fly.admin.system.event;

import com.fly.admin.common.constant.EventType;
import com.fly.admin.system.entity.Group;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/20
 */
@Data
@Accessors(chain = true)
public class GroupEvent {
    private EventType type;
    private List<Group> data;
    private List<Group> oldData;

    public GroupEvent(EventType type, List<Group> data) {
        this.type = type;
        this.data = data;
    }

    public GroupEvent(EventType type, Group group) {
        this.type = type;
        this.data = singletonList(group);
    }

    public GroupEvent(EventType type, Group group, Group old) {
        this.type = type;
        this.data = singletonList(group);
        this.oldData = singletonList(old);
    }

    public GroupEvent(EventType type, List<Group> data, List<Group> oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }
}
