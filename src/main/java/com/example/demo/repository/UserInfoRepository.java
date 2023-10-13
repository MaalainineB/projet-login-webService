package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
//	Profile findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
    Optional<UserInfo> findByName(String username); 

//
}

