package com.iny.side.course.domain.repository;

import com.iny.side.course.domain.entity.Enrollment;

import java.util.List;

public interface EnrollmentRepository {

    List<Enrollment> findAllByAccountIdAndSemester(Long accountId, String semester);

}
