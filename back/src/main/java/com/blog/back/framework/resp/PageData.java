package com.blog.back.framework.resp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;

@Getter
public class PageData<E> {

    private List<E> records;

    private Long size;

    private Long current;

    private Long total;

    public PageData(List<E> records, Long size, Long current, Long total) {
        this.records = records;
        this.size = size;
        this.current = current;
        this.total = total;
    }

    public static <T> PageData<T> builder(List<T> data, Long size, Long current, Long total){
        return new PageData<T>(data,size,current,total);
    }

    public static <T> PageData<T> builder(IPage<T> page){
        return new PageData<>(page.getRecords(),page.getSize(),page.getCurrent(),page.getTotal());
    }

    public static <T> PageData<T> builder(IPage<T> page, Function<PageData<T>,PageData<T>> function){
        PageData<T> res =  builder(page);
        return function.apply(res);
    }

}
