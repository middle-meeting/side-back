package com.iny.side.course.application.service;

import com.iny.side.common.SliceResponse;
import com.iny.side.course.domain.entity.Enrollment;
import com.iny.side.course.domain.repository.CourseRepository;
import com.iny.side.course.domain.repository.EnrollmentRepository;
import com.iny.side.course.web.dto.EnrolledCoursesDto;
import com.iny.side.course.web.dto.ProfessorCoursesDto;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    @Override
    public SliceResponse<EnrolledCoursesDto> getAllEnrolled(Long studentId, String semester, int page) {
        Pageable pageable = PageRequest.of(page, 12);
        Slice<Enrollment> enrollmentSlice = enrollmentRepository.findAllByAccountIdAndSemester(studentId, semester, pageable);

        List<EnrolledCoursesDto> content = enrollmentSlice.getContent().stream()
                .map(EnrolledCoursesDto::from)
                .toList();

        return SliceResponse.of(content, page, 12, enrollmentSlice.hasNext());
    }
}
