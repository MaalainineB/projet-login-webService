package com.example.demo.dto;

import org.springframework.stereotype.Component;

import com.example.demo.entity.UserInfo;

@Component
public class UserInfoMapper {

	public UserInfoDto toDto(UserInfo userInfo) {
		return new UserInfoDto(userInfo.getId(), userInfo.getEmail(), userInfo.getPassword(),
				userInfo.getRoles());
	}

	public UserInfo toUser(UserInfoDto userInfoDto) {
		return new UserInfo(userInfoDto.getId(), userInfoDto.getName(), userInfoDto.getEmail(),
				userInfoDto.getPassword(), userInfoDto.getRoles());
	}

}