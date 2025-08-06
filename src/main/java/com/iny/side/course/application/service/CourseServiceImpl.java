package com.iny.side.course.application.service;

import com.iny.side.common.SliceResponse;
import com.iny.side.common.exception.NotFoundException;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.entity.Enrollment;
import com.iny.side.course.domain.repository.CourseRepository;
import com.iny.side.course.domain.repository.EnrollmentRepository;
import com.iny.side.course.web.dto.EnrolledCoursesDetailDto;
import com.iny.side.course.web.dto.EnrolledCoursesSimpleDto;
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
    private final EnrollmentValidationService enrollmentValidationService;

    @Override
    public SliceResponse<ProfessorCoursesDto> getAll(Long professorId, String semester, int page) {
        Pageable pageable = PageRequest.of(page, 12);
        Slice<Course> courseSlice = courseRepository.findAllByAccountIdAndSemester(professorId, semester, pageable);

        List<ProfessorCoursesDto> content = courseSlice.getContent().stream()
                .map(ProfessorCoursesDto::from)
                .toList();

        return SliceResponse.of(content, page, 12, courseSlice.hasNext());
    }

    @Override
    public List<EnrolledCoursesSimpleDto> getAllEnrolled(Long studentId, String semester) {
        return enrollmentRepository.findAllByAccountIdAndSemester(studentId, semester).stream()
                .map(EnrolledCoursesSimpleDto::from)
                .toList();
    }

    @Override
    public SliceResponse<EnrolledCoursesSimpleDto> getAllEnrolled(Long studentId, String semester, int page) {
        Pageable pageable = PageRequest.of(page, 12);
        Slice<Enrollment> enrollmentSlice = enrollmentRepository.findAllByAccountIdAndSemester(studentId, semester, pageable);

        List<EnrolledCoursesSimpleDto> content = enrollmentSlice.getContent().stream()
                .map(EnrolledCoursesSimpleDto::from)
                .toList();

        return SliceResponse.of(content, page, 12, enrollmentSlice.hasNext());
    }

    @Override
    public EnrolledCoursesDetailDto getEnrolled(Long studentId, Long courseId) {
        // 1. 과목 존재 여부 확인
        courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("과목"));

        // 2. 수강 등록 여부 확인
        enrollmentValidationService.validateStudentEnrolledInCourse(courseId, studentId);

        // 3. 수강 정보 조회
        Enrollment enrollment = enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId)
                .orElseThrow(() -> new NotFoundException("수강신청 정보"));

        return EnrolledCoursesDetailDto.from(enrollment);
    }
}
