package com.parent.service.mapper;

import com.child.common.entity.po.MessageBoard;
import com.child.common.entity.po.TaskInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface TaskMapper {
    @Insert("insert into task_info(task_id,task_name,publisher_id,is_accepted,is_finished,publish_date) values(#{taskId},#{taskName},#{publisherId},#{isAccepted},#{isFinished},#{publishDate})")
    void insertTask(TaskInfo taskInfo);

    @Select("select * from task_info where publisher_id = #{publisherId} and is_finished = 0 and is_accepted = 0")
    List<TaskInfo> selectByPublisherId(String publisherId);

    @Update("update task_info set is_accepted = #{isAccepted},receiver_id = #{receiverId},is_finished=#{isFinished} where task_id = #{taskId}")
    void updateTask(TaskInfo taskInfo);

    @Select("select * from task_info where receiver_id = #{receiverId} and task_name = #{taskName}")
    TaskInfo selectByReceiverIdAndTaskName(String receiverId, String taskName);

    List<TaskInfo> selectUnfinishedExpiredTasks();

    void batchUpdateToFinished(List<String> taskIds);


    @Select("select * from task_info where receiver_id = #{userId} and is_finished = 0 and is_accepted = 1")
    List<TaskInfo> selectByReceiverId(String userId);

    List<TaskInfo> searchTaskOfAllMember(String familyId);
}
