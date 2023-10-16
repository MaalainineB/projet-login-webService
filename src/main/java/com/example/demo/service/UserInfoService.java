package com.example.demo.service;

import java.util.Optional;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.UserInfo;
import com.example.demo.mappers.UserMapper;

@Service
public class UserInfoService implements UserDetailsService {

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private SqlSession sqlSession;

	public UserInfoService() {
	}



	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);

		Optional<UserInfo> userDetail = mapper.findByName(username);

		// Converting userDetail to UserDetails
		return userDetail.map(UserInfoDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
	}

	public String addUser(UserInfo userInfo) {
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		System.err.println(userInfo.getPassword());
		userInfo.setPassword(encoder.encode(userInfo.getPassword()));
		System.err.println(userInfo.getPassword());
		mapper.insertUser(userInfo);
		return "User Added Successfully";
	}

	public UserInfo loadByUsername(String name) {
		if (name != null && !name.isBlank()) {
			Optional<UserInfo> existingUser = userMapper.findByName(name);
			return existingUser
					.orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", name)));
		}
		return null;
	}

}
