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
    public List<Assignment> findAllByCourseId(Long courseId) {
        return assignmentJpaRepository.findAllByCourseId(courseId);
    }

    @Override
    public Assignment save(Assignment assignment) {
        return assignmentJpaRepository.save(assignment);
    }

    @Override
    public Optional<Assignment> findByAssignmentId(Long assignmentId) {
        return assignmentJpaRepository.findById(assignmentId);
    }

    @Override
    public void delete(Assignment assignment) {
        assignmentJpaRepository.delete(assignment);
    }
}
