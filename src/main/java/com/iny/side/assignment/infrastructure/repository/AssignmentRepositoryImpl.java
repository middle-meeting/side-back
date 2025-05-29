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
    public List<Assignment> findAssignmentsByCourseAndProfessor(Long courseId, Long accountId) {
        return assignmentJpaRepository.findAllByCourseIdAndAccountId(courseId, accountId);
    }

    @Override
    public Optional<Assignment> findByCourseId(Long courseId) {
        return assignmentJpaRepository.findByCourseId(courseId);
    }
}
