package com.iny.side.assignment.infrastructure.repository;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.web.dto.StudentAssignmentSimpleResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssignmentJpaRepository extends JpaRepository<Assignment, Long> {

    @Query("SELECT a FROM Assignment a JOIN FETCH a.course c JOIN FETCH c.account where a.course.id = :courseId")
    List<Assignment> findAllByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT new com.iny.side.assignment.web.dto.StudentAssignmentSimpleResponseDto(a.id, a.title, a.dueDate, a.objective, s.status) FROM Assignment a LEFT JOIN Submission s ON a.id = s.assignment.id AND s.student.id = :studentId WHERE a.course.id = :courseId")
    Slice<StudentAssignmentSimpleResponseDto> findAllByCourseIdAndStudentId(@Param("courseId") Long courseId, @Param("studentId") Long studentId, Pageable pageable);
}
