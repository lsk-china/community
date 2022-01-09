package com.lsk.community.back.community.model;

import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import com.lsk.community.back.community.reply.ReplyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reply")
public class Reply {
	@IsKey
	@Column
	@IsAutoIncrement
	private Integer id;

	@DefaultValue("")
	@Column(decimalLength = 200)
	private String content;

	@Column
	@IsNotNull
	private Integer senderID;

	@Column
	@IsNotNull
	private String type;

	@IsNotNull
	@Column(comment = "回复的目标id")
	private Integer from;

	@IsNotNull
	@Column(comment = "回复者的UID")
	private Integer by;

	@Column
	@DefaultValue("0")
	private Integer like;

	@Column
	@DefaultValue("0")
	private Integer reply;

	@Column
	@IsNotNull
	private Date replyTime;


}
