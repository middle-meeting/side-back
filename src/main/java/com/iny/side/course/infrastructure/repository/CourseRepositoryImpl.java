package com.iny.side.course.infrastructure.repository;

import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseRepository {

    private final CourseJpaRepository courseJpaRepository;

    @Override
    public List<Course> findAllByAccountIdAndSemester(Long accountId, String semester) {
        return courseJpaRepository.findAllByAccountIdAndSemester(accountId, semester);
    }

    @Override
    public Optional<Course> findById(Long courseId) {
        return courseJpaRepository.findById(courseId);
    }
}
