package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface HospitalService {

    //新增数据
    void save(Map<String, Object> paramMap);

    //根据医院编号进行查询
    Hospital getByHoscode(String hoscode);

    Page selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    //更新医院的上线状态
    void updateStatus(String id, Integer status);

    //医院的详情信息
    Map<String, Object> getHospById(String id);

    //获取医院名称
    String getHospName(String hoscode);
}
