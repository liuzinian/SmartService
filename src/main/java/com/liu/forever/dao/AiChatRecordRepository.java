package com.liu.forever.dao;

import com.liu.forever.model.AiChatRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiChatRecordRepository extends JpaRepository<AiChatRecord, String> {
    List<AiChatRecord> findByUserId(String userId);
}
