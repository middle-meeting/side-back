package com.iny.side.course.infrastructure.repository;

import com.iny.side.course.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseJpaRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByAccountIdAndSemester(Long accountId, String semester);

}
