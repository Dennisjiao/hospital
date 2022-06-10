package com.atguigu.yygh.hosp.controller.api;


import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.HttpRequestHelper;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;
    //得到传来的医院信息

    //查询医院
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request){
        //获取传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //获取传过来的医院编号
        String hoscode = (String)paramMap.get("hoscode");
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
        //获取传递过来的信息
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
