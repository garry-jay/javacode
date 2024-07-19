package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Autowired
    private DishMapper dishMapper;
    /**
     * 新增套餐
     * @return
     */

    public void save(SetmealDTO setmealDTO) {
        Setmeal setmeal =new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.insert(setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes !=null && setmealDishes.size()>0){
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmeal.getId());
            }
        }
        //批量插入setmeal_dish
        setMealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 套餐分页查询
     * @return
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<Setmeal> setmealPage =setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(setmealPage.getTotal(),setmealPage.getResult());
    }

    /**
     * 套餐批量删除
     * @param ids
     */
    public void deleteBatch(List<Long> ids) {
        //lambda表达式：（）->{} ()是方法的形参（数据类型可省略） {}是方法体
        ids.forEach(id->{
            Setmeal setmeal =setmealMapper.getById(id);
            if(StatusConstant.ENABLE==setmeal.getStatus()){
                //起售中的套餐不能删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        ids.forEach(setmealId->{
            //删除套餐表中的数据
            setmealMapper.deleteById(setmealId);
            //删除套餐菜品关系表中的数据
            setMealDishMapper.deleteBySetmealId(setmealId);
        });

    }

    //根据id查询套餐
    public SetmealVO getByIdWithDish(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        List<SetmealDish> setmealDishes =setMealDishMapper.getSetMealIdByDishId(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    //修改套餐
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal =new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        //修改套餐表
        setmealMapper.update(setmeal);
        //套餐id
        Long setmealId =setmealDTO.getId();
        //修改套餐-菜品表
        setMealDishMapper.deleteBySetmealId(setmealId);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        setMealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 套餐起售、停售
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        //如果status为1，要判断菜品的状态是否为停售，是就抛异常
        if(status==StatusConstant.ENABLE){
            List<Dish> dishList= dishMapper.getBySetmealId(id);
            if(dishList !=null && dishList.size()>0){
                dishList.forEach(dish -> {
                    if(StatusConstant.DISABLE==dish.getStatus()){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();
        setmealMapper.update(setmeal);
    }


}
