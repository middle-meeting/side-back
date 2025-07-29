package com.iny.side.course.infrastructure.repository;

import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseRepository {

    private final CourseJpaRepository courseJpaRepository;

    @Override
    public Slice<Course> findAllByAccountIdAndSemester(Long accountId, String semester, Pageable pageable) {
        return courseJpaRepository.findAllByAccountIdAndSemester(accountId, semester, pageable);
    }

    @Override
    public Optional<Course> findById(Long courseId) {
        return courseJpaRepository.findById(courseId);
    }
}
