package com.liu.forever.dao;

import com.liu.forever.model.UserInfo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, String> {


}
