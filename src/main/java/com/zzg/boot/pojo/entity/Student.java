package com.zzg.boot.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 张中贵
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student implements Serializable {

    private int id;

    private String sName;

    private String sAge;

    private String sSex;

}