package com.blog.back.POJO.VO;

import com.blog.back.POJO.entity.Article;
import com.blog.back.POJO.entity.Comment;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class SelfCommentDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    private Comment comment;

    private Article article;

}
