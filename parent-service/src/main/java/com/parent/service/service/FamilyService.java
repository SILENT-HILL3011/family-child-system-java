package com.parent.service.service;

import com.child.common.entity.po.TaskInfo;

import java.util.List;

public interface FamilyService {
    void publishTask(String publisherId, String taskName, String publishDate);

    TaskInfo acceptTask(String publisherId, String receiverId,String taskName);

    TaskInfo finishTask(String receiverId, String taskName);

    List<TaskInfo> searchTask(String publisherId);
}
