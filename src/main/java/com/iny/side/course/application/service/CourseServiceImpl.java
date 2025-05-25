package com.iny.side.course.application.service;

import com.iny.side.course.domain.entity.Course;
import com.iny.side.course.domain.repository.CourseRepository;
import com.iny.side.course.web.dto.MyCoursesDto;
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
    public List<MyCoursesDto> findMyCourse(AccountResponseDto accountResponseDto, String semester) {
        List<Course> myCourseBySemester = courseRepository.findMyCourseBySemester(accountResponseDto.id(), semester);
        return myCourseBySemester.stream()
                .map(MyCoursesDto::from)
                .toList();
    }
}
