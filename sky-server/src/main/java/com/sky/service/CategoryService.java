package com.sky.service;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;


public interface CategoryService {
    /**
     * 新增分类
     * @param
     * @return
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据ID删除分类
     * @param id
     * @return
     */
    void deleteById(Long id);

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    void update(CategoryDTO categoryDTO);
}
