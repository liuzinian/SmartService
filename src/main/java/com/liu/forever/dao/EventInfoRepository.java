package com.liu.forever.dao;

import com.liu.forever.model.EventInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventInfoRepository extends JpaRepository<EventInfo, String> {

    List<EventInfo> findByTaskState(String taskState);
}
