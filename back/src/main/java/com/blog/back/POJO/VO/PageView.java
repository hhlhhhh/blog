package com.blog.back.POJO.VO;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class PageView {

    @Min(1)
    private Long size;

    @Min(1)
    private Long current;

    private String keyword;

    private String field;

}
