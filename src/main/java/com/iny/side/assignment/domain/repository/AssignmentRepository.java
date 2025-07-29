package com.iny.side.assignment.domain.repository;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.web.dto.StudentAssignmentSimpleResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository {

    List<Assignment> findAllByCourseId(Long courseId);

    Slice<Assignment> findAllByCourseId(Long courseId, Pageable pageable);

    Slice<StudentAssignmentSimpleResponseDto> findAllByCourseIdAndStudentId(Long courseId, Long studentId, Pageable pageable);

    Assignment save(Assignment assignment);

    Optional<Assignment> findByAssignmentId(Long assignmentId);

    void delete(Assignment assignment);
}
