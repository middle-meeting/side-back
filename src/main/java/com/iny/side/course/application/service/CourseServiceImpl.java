package com.iny.side.course.application.service;

import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.entity.Enrollment;
import com.iny.side.course.domain.repository.CourseRepository;
import com.iny.side.course.domain.repository.EnrollmentRepository;
import com.iny.side.course.web.dto.EnrolledCoursesDto;
import com.iny.side.course.web.dto.ProfessorCoursesDto;
import com.iny.side.users.web.dto.AccountResponseDto;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public List<ProfessorCoursesDto> getAll(Long professorId, String semester) {
        return courseRepository.findAllByAccountIdAndSemester(professorId, semester).stream()
                .map(ProfessorCoursesDto::from)
                .toList();
    }

    @Override
    public List<EnrolledCoursesDto> getAllEnrolled(Long studentId, String semester) {
        return enrollmentRepository.findAllByAccountIdAndSemester(studentId, semester).stream()
                .map(EnrolledCoursesDto::from)
                .toList();
    }
}
