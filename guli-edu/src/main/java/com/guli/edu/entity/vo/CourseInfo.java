package com.guli.edu.entity.vo;

import com.guli.edu.entity.Course;
import com.guli.edu.entity.CourseDescription;
import lombok.Data;

@Data
public class CourseInfo {

    private Course course;

    private CourseDescription courseDescription;
}
