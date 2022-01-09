package com.lsk.community.back.community.model;

import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "work")
public class Work {
	@IsKey
	@Column
	@IsAutoIncrement
	private Integer id;
	@Column
	@IsNotNull
	private String name;
	@Column
	@IsNotNull
	private Integer owner;
	@Column
	@DefaultValue("0")
	private Integer like;
	@Column
	@DefaultValue("0")
	private Integer view;
	@Column
	@DefaultValue("0")
	private Integer reply;
	@Column
	@DefaultValue("0")
	private Integer favourites;
	@DefaultValue("0")
	@Column(comment = "作品是否可见，当作品未过审或被举报时该字段为0")
	private Integer visible;
}
