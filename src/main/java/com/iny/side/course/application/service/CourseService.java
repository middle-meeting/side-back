package com.iny.side.course.application.service;

import com.iny.side.course.web.dto.MyCoursesDto;
import com.iny.side.users.web.dto.AccountResponseDto;

import java.util.List;

public interface CourseService {
    List<MyCoursesDto> findMyCourse(AccountResponseDto accountResponseDto, String semester);
}
