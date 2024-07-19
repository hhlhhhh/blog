package com.blog.back.POJO.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("tb_comment")
public class Comment implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT,value = "create_time")
    private LocalDateTime createTime;

    private String content;

    private Integer status;

    private Integer articleId;

    private Integer parentId;

    private Integer count;

    private Integer topParent;

    @TableField(exist = false)
    private List<Comment> commentList;  //子评论

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private Comment parentComment;

}

