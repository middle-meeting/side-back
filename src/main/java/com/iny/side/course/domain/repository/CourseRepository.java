package com.iny.side.course.domain.repository;

import com.iny.side.course.domain.entity.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {

    Slice<Course> findAllByAccountIdAndSemester(Long accountId, String semester, Pageable pageable);

    Optional<Course> findById(Long courseId);
}
