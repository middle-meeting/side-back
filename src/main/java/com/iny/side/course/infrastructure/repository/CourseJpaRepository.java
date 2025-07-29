package com.iny.side.course.infrastructure.repository;

import com.iny.side.course.domain.entity.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseJpaRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE c.account.id = :accountId AND c.semester = :semester")
    Slice<Course> findAllByAccountIdAndSemester(@Param("accountId") Long accountId, @Param("semester") String semester, Pageable pageable);

}
