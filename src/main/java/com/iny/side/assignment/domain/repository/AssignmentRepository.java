package com.iny.side.assignment.domain.repository;

import com.iny.side.assignment.domain.entity.Assignment;

import java.util.List;

public interface AssignmentRepository {

    List<Assignment> findByCourseId(Long courseId);

    Assignment save(Assignment assignment);
}
