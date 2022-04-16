package com.atguigu.yygh.hosp.controller;

import com.alibaba.excel.util.StringUtils;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@CrossOrigin //允许跨域访问
public class HospitalController {
    //注入Service然后调用
    @Autowired
    private HospitalSetService hospitalSetService;
    //http://localhost:8080/admin/hosp/hospitalSet/findAll

    //增删改查方法

    //查询医院设置表中的所有数据
    @ApiOperation(value = "获取医院所有设置信息")
    @GetMapping("findAll")
    public Result findAllHospitalSet() {
        //调用Service中的方法
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
        //return list;
    }

    /*//查询医院设置表中的所有数据
    @ApiOperation(value = "获取医院所有设置信息")
    @GetMapping("findAll")
    public List<HospitalSet> findAllHospitalSet() {
        //调用Service中的方法
        List<HospitalSet> list = hospitalSetService.list();
        return list;
    }*/

    //删除医院设置(逻辑删除)
    @ApiOperation(value = "逻辑删除医院设置信息")
    @DeleteMapping("{id}")
    public Result removeHospitalSet(@PathVariable Long id){
        boolean flag = hospitalSetService.removeById(id);
        return Result.ok(flag);
        //return flag;
    }

    //3条件查询带分页
    @PostMapping("findPageHospSet/{current}/{limit}")
    //因为是通过路径传递的，所以加入PathVariable注解
    //HospitalQueryVo以对象的形式直接传进来
    public Result findPageHospSet(@PathVariable long current, @PathVariable long limit,@RequestBody(required = false) HospitalQueryVo hospitalQueryVo){
        //↑加入@RequestBody 通过json传参数，将HospitalQueryVo值加入对象中去。required = false指这个值可以为空

        //创建page对象，传递当前页，每页记录数
        //传入current,limit两个参数
        Page<HospitalSet> page =new Page<>(current, limit);
        //构建条件 ，使用mp中的条件构造器
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hosname = hospitalQueryVo.getHosname();//医院名称
        String hoscode = hospitalQueryVo.getHoscode();//医院编号
        //模糊查询  在vo对象中取值 数据库字段
        //先判断是不是为空，为空则不传
        if(!StringUtils.isEmpty(hosname)){
            wrapper.like("hosname",hospitalQueryVo.getHosname());
        }
        if(!StringUtils.isEmpty(hoscode)){
            wrapper.like("hoscode",hospitalQueryVo.getHoscode());
        }
        //调用方法实现分页查询
        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page, wrapper);
        return Result.ok(pageHospitalSet);
    }

    //4添加医院设置接口
    @PostMapping("saveHosptialSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet){
        //设置状态 1是可以使用 0是不能使用
        hospitalSet.setStatus(1);
        //签名密钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));

        //调用service方法 做其他属性得添加
        boolean save = hospitalSetService.save(hospitalSet);
        if(save){
            return Result.ok();
        }else{
            return Result.fail();
        }

    }


    //5根据id获取医院设置接口
    @GetMapping("getHospSet/{id}")
    public Result getHospitalSet(@PathVariable Long id){
//        try {
//            int a = 1/0;
//        }catch(Exception e){
//            throw new YyghException("失败",201);
//        }
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    //6修改医院设置接口
    //先查询，再修改   RequestBody是传递的json数据类，在前端中要使用data:来通过json来转换数据
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if(flag){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

    //7批量删除医院设置
    @DeleteMapping("batchRemove")
    public Result batchRemoveHosptialSet(@RequestBody List<Long> idList){
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }

    //8医院设置锁定和解锁功能
    //因为是修改status值所以可以用putmapping
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id, @PathVariable Integer status){
        //根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        //调用方法
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    //9发送签名密钥(短信验证码) 之后加上短信验证功能
    @PutMapping("sendKey/{id}")
    public Result lockHospitalSet(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //TODO 发送短信
        return Result.ok();
    }
}
