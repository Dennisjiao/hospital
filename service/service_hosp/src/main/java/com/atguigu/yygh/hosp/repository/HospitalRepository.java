package com.atguigu.yygh.hosp.repository;

import com.atguigu.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String> {

    //在Impl实现类中 判断是否存在数据
    Hospital getHospitalByHoscode(String hoscode);

    //模糊查询名字
    List<Hospital> findHospitalByHosnameLike(String hosname);
}
