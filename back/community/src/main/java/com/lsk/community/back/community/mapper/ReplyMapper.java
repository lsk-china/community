package com.lsk.community.back.community.mapper;

import com.lsk.community.back.community.reply.Reply;
import com.lsk.community.back.community.reply.ReplyType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ReplyMapper {
	@Select("select * from reply where type=#{type} and from=#{from}")
	public List<Reply> queryReplyOf(@Param("type") ReplyType type, @Param("from") Integer from);
	@Update("update reply set reply=reply+1 where id=#{id}")
	public void increaseReply(@Param("id") Integer id);
	@Update("update reply set like=like+1 where id=#{id}")
	public void increaseLike(@Param("id") Integer id);

}
