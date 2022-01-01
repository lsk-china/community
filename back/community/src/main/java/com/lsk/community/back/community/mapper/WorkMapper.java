package com.lsk.community.back.community.mapper;

import com.lsk.community.back.community.work.Work;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WorkMapper {
	@Select("select count(id) from work where visible=1")
	public Integer visibleWorkCount();
	@Select("select count(id) from work where visible=0")
	public Integer invisibleWorkCount();
	@Select("select * from work where id=#{id}")
	public Work queryWorkById(@Param("id") Integer id);
	@Select("select * from work where owner=#{uid}")
	public List<Work> queryOnesWork(@Param("uid") Integer id);
	@Select("select * from work where visible=1 limit (${pageNo}-1)*${pageSize}, ${pageSize}")
	public List<Work> queryAllVisibleWorks(@Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);
	@Select("select * from work where visible=0")
	public List<Work> queryAllInvisibleWorks();
	@Insert("insert into work(name, owner) values(#{name}, #{owner})")
	public void createWork(@Param("name") String name, @Param("owner") Integer owner);
	@Update("update work set name=#{newName} where id=#{id}")
	public void renameWorkById(@Param("id") Integer id, @Param("newName") String newName);
	@Update("update work set visible=1 where id=#{id}")
	public void enableWork(@Param("id") Integer id);
	@Update("update work set visible=0 where id=#{id}")
	public void disableWork(@Param("id") Integer id);
	@Delete("delete * from work where id=#{id}")
	public void deleteWork(@Param("id") Integer id);
}
