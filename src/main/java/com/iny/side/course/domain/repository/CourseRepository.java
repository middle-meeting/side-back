package com.iny.side.course.domain.repository;

import com.iny.side.course.domain.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {

    List<Course> findAllByAccountIdAndSemester(Long accountId, String semester);

    Optional<Course> findById(Long courseId);
}
