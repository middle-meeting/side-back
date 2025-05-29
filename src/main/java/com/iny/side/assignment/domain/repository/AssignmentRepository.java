package com.iny.side.assignment.domain.repository;

import com.iny.side.assignment.domain.entity.Assignment;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository {

    List<Assignment> findAssignmentsByCourseAndProfessor(Long courseId, Long accountId);

    Optional<Assignment> findByCourseId(Long courseId);
}
