package com.example.demo.mappers;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.example.demo.entity.UserInfo;


@Mapper
public interface UserMapper {
	@Select("SELECT * FROM user_info WHERE name = #{name}")
	Optional<UserInfo> findByName(String name);

	@Select("SELECT * FROM user_info WHERE id = #{id}")
	UserInfo getUserById(int userId);

	@Select("SELECT * FROM user_info")
	List<UserInfo> getAllUsers();

//	@Insert("INSERT INTO user_info (name, email,password,roles)" + " VALUES (#{name}, #{email}, #{password}, #{roles})")
//	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
//	void insertUser(UserInfo user);
	
	@Insert("INSERT INTO user_info (name, email, password, roles) VALUES (#{name}, #{email}, #{password}, #{roles})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void insertUser(UserInfo user);


	

}
