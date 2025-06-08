package com.iny.side.assignment.domain.entity;

import com.iny.side.assignment.exception.InvalidAssignmentDueDateException;
import com.iny.side.assignment.web.dto.AssignmentCreateDto;
import com.iny.side.common.domain.GenderType;
import com.iny.side.course.domain.entity.Course;
import com.iny.side.users.domain.entity.Account;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "persona_name", nullable = false)
    private String personaName;

    @Column(name = "persona_age", nullable = false)
    private Integer personaAge;

    @Column(name = "persona_gender", nullable = false)
    private GenderType personaGender;

    @Column(name = "persona_symptom", nullable = false)
    private String personaSymptom;

    @Column(name = "persona_history")
    private String personaHistory;

    @Column(name = "persona_personality")
    private String personaPersonality;

    @Column(name = "persona_disease", nullable = false)
    private String personaDisease;

    @Column(name = "objective", nullable = false)
    private String objective;

    @Column(name = "max_turns", nullable = false)
    private Integer maxTurns;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Builder
    public Assignment(Long id, String title, String personaName, Integer personaAge, GenderType personaGender, String personaSymptom, String personaHistory, String personaPersonality, String personaDisease, String objective, Integer maxTurns, LocalDateTime dueDate, Course course) {
        this.id = id;
        this.title = title;
        this.personaName = personaName;
        this.personaAge = personaAge;
        this.personaGender = personaGender;
        this.personaSymptom = personaSymptom;
        this.personaHistory = personaHistory;
        this.personaPersonality = personaPersonality;
        this.personaDisease = personaDisease;
        this.objective = objective;
        this.maxTurns = maxTurns;
        this.dueDate = dueDate;
        this.course = course;
    }

    public static Assignment create(Course course, AssignmentCreateDto dto) {
        if (dto.dueDate().getMinute() % 30 != 0 || dto.dueDate().getSecond() != 0 || dto.dueDate().getNano() != 0) {
            throw new InvalidAssignmentDueDateException();
        }

        return Assignment.builder()
                .title(dto.title())
                .personaName(dto.personaName())
                .personaAge(dto.personaAge())
                .personaGender(dto.personaGender())
                .personaSymptom(dto.personaSymptom())
                .personaHistory(dto.personaHistory())
                .personaPersonality(dto.personaPersonality())
                .personaDisease(dto.personaDisease())
                .objective(dto.objective())
                .maxTurns(dto.maxTurns())
                .dueDate(dto.dueDate())
                .course(course)
                .build();
    }
}
