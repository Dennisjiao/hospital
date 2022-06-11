package com.atguigu.yygh.hosp.controller.api;


import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.HttpRequestHelper;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import javafx.concurrent.ScheduledService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    //得到传来的医院信息

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    //删除排班接口
    @PostMapping("schedule/remove")
    public Result remove(HttpServletRequest request){

        //获取传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //得到排班编号和医院编号
        String hoscode = (String)paramMap.get("hoscode");
        String hosScheduleId = (String)paramMap.get("hosScheduleId");

        /**
         * 签名校验
         * **/
        //1.获取医院系统传递过来的签名,签名进行了MD5加密，所以转换为String对比
        String hospSign = (String)paramMap.get("sign");

        //2.根据传递过来的医院编码，查询数据库，查询签名是否一样
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把数据库查询签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);
        //4.判断签名是否一致，传过来的签名和数据库的签名是否一致
        if(!hospSign.equals(signKeyMD5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        scheduleService.remove(hoscode,hosScheduleId);
        return Result.ok();

    }

    //查询排班接口
    @PostMapping("schedule/list")
    public Result findSchedule(HttpServletRequest request){

        //获取传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //获取传过来的医院编号 和科室编号
        String hoscode = (String)paramMap.get("hoscode");
        String depcode = (String)paramMap.get("depcode");

        //当前页和 每页记录数  判断是否一致，一致返回1不一致返回hoscode
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1:Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1:Integer.parseInt((String)paramMap.get("limit"));

        /**
         * 签名校验
         * **/
        //1.获取医院系统传递过来的签名,签名进行了MD5加密，所以转换为String对比
        String hospSign = (String)paramMap.get("sign");

        //2.根据传递过来的医院编码，查询数据库，查询签名是否一样
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把数据库查询签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);
        //4.判断签名是否一致，传过来的签名和数据库的签名是否一致
        if(!hospSign.equals(signKeyMD5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //对实体类进行封装 通过对象做传递
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        //调用方法
        Page<Schedule> pageModel = scheduleService.findPageSchedule(page,limit,scheduleQueryVo);
        return Result.ok(pageModel);
    }

    //上传排班接口
    @PatchMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        //获取传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //得到科室编号和医院编号
        String hoscode = (String)paramMap.get("hoscode");

        /**
         * 签名校验
         * **/
        //1.获取医院系统传递过来的签名,签名进行了MD5加密，所以转换为String对比
        String hospSign = (String)paramMap.get("sign");

        //2.根据传递过来的医院编码，查询数据库，查询签名是否一样
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把数据库查询签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);
        //4.判断签名是否一致，传过来的签名和数据库的签名是否一致
        if(!hospSign.equals(signKeyMD5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        scheduleService.save(paramMap);
        return Result.ok();
    }

    //删除科室接口
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request){
        //获取传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //得到科室编号和医院编号
        String hoscode = (String)paramMap.get("hoscode");
        String depcode = (String)paramMap.get("depcode");

        /**
         * 签名校验
         * **/
        //1.获取医院系统传递过来的签名,签名进行了MD5加密，所以转换为String对比
        String hospSign = (String)paramMap.get("sign");

        //2.根据传递过来的医院编码，查询数据库，查询签名是否一样
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把数据库查询签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);
        //4.判断签名是否一致，传过来的签名和数据库的签名是否一致
        if(!hospSign.equals(signKeyMD5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        departmentService.remove(hoscode,depcode);
        return Result.ok();
    }

    //查询科室接口
    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request){

        //获取传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //获取传过来的医院编号
        String hoscode = (String)paramMap.get("hoscode");

        //当前页和 每页记录数  判断是否一致，一致返回1不一致返回hoscode
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1:Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1:Integer.parseInt((String)paramMap.get("limit"));

        /**
         * 签名校验
         * **/
        //1.获取医院系统传递过来的签名,签名进行了MD5加密，所以转换为String对比
        String hospSign = (String)paramMap.get("sign");

        //2.根据传递过来的医院编码，查询数据库，查询签名是否一样
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把数据库查询签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);
        //4.判断签名是否一致，传过来的签名和数据库的签名是否一致
        if(!hospSign.equals(signKeyMD5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //对实体类进行封装 通过对象做传递
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        //调用方法
        Page<Department> pageModel = departmentService.findPageDepartment(page,limit,departmentQueryVo);
        return Result.ok(pageModel);
    }

    //上传科室接口
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        //获取传递过来的科室信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //获取传过来的医院编号
        String hoscode = (String)paramMap.get("hoscode");

        /**
         * 签名校验
         * **/
        //1.获取医院系统传递过来的签名,签名进行了MD5加密，所以转换为String对比
        String hospSign = (String)paramMap.get("sign");

        //2.根据传递过来的医院编码，查询数据库，查询签名是否一样
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把数据库查询签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);
        //4.判断签名是否一致，传过来的签名和数据库的签名是否一致
        if(!hospSign.equals(signKeyMD5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //调用service中的方法
        departmentService.save(paramMap);
        return Result.ok();
    }


    //查询医院
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request){
        //获取传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //获取传过来的医院编号
        String hoscode = (String)paramMap.get("hoscode");

        /**
         * 签名校验
         * **/
        //1.获取医院系统传递过来的签名,签名进行了MD5加密，所以转换为String对比
        String hospSign = (String)paramMap.get("sign");

        //2.根据传递过来的医院编码，查询数据库，查询签名是否一样
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把数据库查询签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);
        //4.判断签名是否一致，传过来的签名和数据库的签名是否一致
        if(!hospSign.equals(signKeyMD5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //调用service方法实现根据医院编号查询
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

    //上传医院接口
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request){
        //获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //对key做校验
        //1.获取医院系统传递过来的签名,签名进行了MD5加密，所以转换为String对比
        String hospSign = (String)paramMap.get("sign");
        //2.根据传递过来的医院编码，查询数据库，查询签名是否一样
        String hoscode = (String)paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把数据库查询签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);
        //4.判断签名是否一致，传过来的签名和数据库的签名是否一致
        if(!hospSign.equals(signKeyMD5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        //在显示图片时，使用了base64进行处理，传输过程中"+"转换为了“ ”，因此要转换过来
        String logoData = (String) paramMap.get("logoData");
        logoData = logoData.replaceAll(" ","+");
        paramMap.put("logoData",logoData);

        //调用service方法
        hospitalService.save(paramMap);
        return Result.ok();
    }



}
