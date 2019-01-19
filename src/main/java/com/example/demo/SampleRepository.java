package com.example.demo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SampleRepository {
    List<String> findAll();

    @Select("SELECT 1")
    int lock();
}
