package com.example.demo.repository;

import java.util.Optional;

import com.example.demo.entity.UserInfo;

public interface UserInfoRepository {
//	Profile findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
	Optional<UserInfo> findByName(String username);

//
}
