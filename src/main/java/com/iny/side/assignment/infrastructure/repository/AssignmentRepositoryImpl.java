package com.iny.side.assignment.infrastructure.repository;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.domain.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AssignmentRepositoryImpl implements AssignmentRepository {

    private final AssignmentJpaRepository assignmentJpaRepository;

    @Override
    public List<Assignment> findByCourseId(Long courseId) {
        return assignmentJpaRepository.findByCourseId(courseId);
    }

    @Override
    public Assignment save(Assignment assignment) {
        return assignmentJpaRepository.save(assignment);
    }
}
