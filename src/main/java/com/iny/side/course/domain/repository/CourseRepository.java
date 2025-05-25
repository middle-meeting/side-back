package com.iny.side.course.domain.repository;

import com.iny.side.course.domain.entity.Course;

import java.util.List;

public interface CourseRepository {

    List<Course> findMyCourseBySemester(Long accountId, String semester);
}
