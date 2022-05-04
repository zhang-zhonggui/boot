package com.zzg.boot.controller;


import com.zzg.boot.pojo.entity.Student;
import com.zzg.boot.result.AjaxResult;
import com.zzg.boot.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author 张中贵
 */
@RestController
@Api(tags = "学生管理")
@RequestMapping("stu")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("queryAllStu")
    @ApiOperation(value = "查询所有学生")
    public AjaxResult listStudent() {
        return studentService.listAllStudent();
    }

    @DeleteMapping("deleteByIdStu")
    @ApiOperation(value = "根据id删除学生")
    public AjaxResult deleteStudent(int id) {
        return studentService.deleteStudent(id);
    }
}