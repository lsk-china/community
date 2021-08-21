package com.lsk.community.back.auth.mapper;

import com.lsk.community.back.auth.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
	@Select("select * from user")
	public List<User> queryAllUsers();
	@Select("select * from user where username=#{data} or mail=#{data}")
	public User queryUserByUsernameOrEmail(@Param("data") String data);
	@Insert("insert into user(username,password,mail) values(#{username}, #{password}, #{mail})")
	public void createUser(User user);
}
