package com.iny.side.assignment.domain.repository;

import com.iny.side.assignment.domain.entity.Assignment;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository {

    List<Assignment> findAllByCourseId(Long courseId);

    Assignment save(Assignment assignment);

    Optional<Assignment> findByAssignmentId(Long assignmentId);

    void delete(Assignment assignment);
}
