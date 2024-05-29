package com.liu.forever.dao;

import com.liu.forever.model.EventInfo;
import com.liu.forever.model.TaiYoAiRoleDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiRoleDORepository extends JpaRepository<TaiYoAiRoleDO, String> {
}
