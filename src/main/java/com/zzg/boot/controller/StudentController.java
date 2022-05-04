package com.zzg.boot.controller;


import com.zzg.boot.pojo.entity.Student;
import com.zzg.boot.result.AjaxResult;
import com.zzg.boot.service.StudentService;
import io.swagger.annotations.Api;
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
    public AjaxResult listStudent() {
        return studentService.listAllStudent();
    }
}