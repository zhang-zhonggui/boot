package com.zzg.boot.mapper;

import com.zzg.boot.pojo.entity.Student;

import java.util.List;

public interface StudentMapper {
    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(Student record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(Student record);

     List<Student> selectAllStudent();
}