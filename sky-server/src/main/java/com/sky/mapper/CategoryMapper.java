package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    //插入新分类
    @Insert("insert into category " +
            "(type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "values " +
            "(#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @AutoFill(value=OperationType.UPDATE)
    void insert(Category category);

    //分页查询
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    //根据ID删除分类
    @Delete("delete from category where id =#{id}")
    void deleteById(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);

    List<Category> list(Integer type);
}

