package com.zzg.boot.service.impl;

import com.zzg.boot.mapper.StudentMapper;
import com.zzg.boot.pojo.entity.Student;
import com.zzg.boot.result.AjaxResult;
import com.zzg.boot.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：zzg
 * @description：
 * @date ：2022/5/3 19:47
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public AjaxResult listAllStudent() {
        List<Student> students = studentMapper.selectAllStudent();
        if (students.size() > 0) {
            return AjaxResult.success(students);
        } else {
            return AjaxResult.error("查询失败");
        }
    }

    @Override
    public AjaxResult deleteStudent(int id) {
        int i = studentMapper.deleteByIdStudent(id);
        if (i > 0) {
            return AjaxResult.success("删除成功");
        }
        return AjaxResult.error("删除失败");
    }
}
