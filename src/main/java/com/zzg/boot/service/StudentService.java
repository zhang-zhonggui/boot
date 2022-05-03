package com.zzg.boot.service;

import com.zzg.boot.result.AjaxResult;
import org.springframework.stereotype.Service;

/**
 * @author ：zzg
 * @description：
 * @date ：2022/5/3 19:45
 */

public interface StudentService {
    /**
     * 查询所以学生
     * @return
     */
    AjaxResult listAllStudent();
}
