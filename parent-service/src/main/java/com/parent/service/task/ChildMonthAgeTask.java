package com.parent.service.task;

import com.child.common.entity.po.Child;
import com.parent.service.mapper.ChildMapper;
import com.parent.service.service.ChildService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChildMonthAgeTask {

    private static final Logger log = LoggerFactory.getLogger(ChildMonthAgeTask.class);
    @Resource
    private ChildMapper childMapper;
    @Resource
    private ChildService childService;

    @Scheduled(cron = "0 0 0 1 * ?")
    public void autoUpdateChildMonthAge(){
        log.info("开始更新月龄");
        List<Child> childList = childMapper.selectChildIds();
        if (childList.isEmpty() || childList == null){
            log.info("没有需要更新的月龄");
            return;
        }
        for (Child child : childList){
            String childId = child.getChildId();
            Integer oldAge = child.getAge();
            int newAge = oldAge + 1;
            child.setAge(newAge);
            if (newAge >= 84){
                log.info("该孩子已经7岁吗，自动删除："+childId);
                childService.deleteChild(childId);
            }else {
                childService.updateChildInfo(child);
                log.info("儿童：" + childId + " 月龄更新：" + oldAge + " → " + newAge);
            }
        }
        log.info("=== 每月1号：儿童月龄更新任务执行完毕 ===");
    }
}
