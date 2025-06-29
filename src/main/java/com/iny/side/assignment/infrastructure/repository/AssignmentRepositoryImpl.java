package com.iny.side.assignment.infrastructure.repository;

import com.iny.side.assignment.domain.entity.Assignment;
import com.iny.side.assignment.domain.repository.AssignmentRepository;
import com.iny.side.assignment.web.dto.StudentAssignmentSimpleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    public Slice<StudentAssignmentSimpleResponseDto> findAllByCourseIdAndStudentId(Long courseId, Long studentId, Pageable pageable) {
        return assignmentJpaRepository.findAllByCourseId(courseId, pageable);
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
