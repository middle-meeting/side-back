package com.iny.side.course.application.service;

import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.repository.CourseRepository;
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

    @Override
    public List<ProfessorCoursesDto> findProfessorCourses(AccountResponseDto accountResponseDto, String semester) {
        List<Course> courses = courseRepository.findAllByAccountIdAndSemester(accountResponseDto.id(), semester);
        return courses.stream()
                .map(ProfessorCoursesDto::from)
                .toList();
    }
}
