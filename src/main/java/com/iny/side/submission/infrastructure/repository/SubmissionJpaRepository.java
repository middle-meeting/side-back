package com.iny.side.submission.infrastructure.repository;

import com.iny.side.submission.domain.entity.Submission;
import com.iny.side.submission.domain.vo.SubmissionDetailVo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubmissionJpaRepository extends JpaRepository<Submission, Long> {

    Optional<Submission> findByStudentIdAndAssignmentId(Long studentId, Long assignmentId);

    @Query("""
        select count(e)
          from Evaluation e
         where e.submission.assignment.id = :assignmentId
           and e.submission.assignment.course.id = :courseId
    """)
    Long countEvaluatedByCourseIdAndAssignmentId(@Param("courseId") Long courseId, @Param("assignmentId") Long assignmentId);

    @Query("""
        select count(s)
          from Submission s
     left join Evaluation e
            on e.submission.id = s.id
         where s.assignment.id = :assignmentId
           and s.assignment.course.id = :courseId
           and s.status = "SUBMITTED"
           and e.id is null
    """)
    Long countNotEvaluatedByCourseIdAndAssignmentId(@Param("courseId") Long courseId, @Param("assignmentId") Long assignmentId);

    @Query("""
        select count(e)
          from Course c
          join Enrollment e
            on c.id = e.course.id
     left join Submission s
            on s.student.id = e.account.id
           and s.assignment.id = :assignmentId
         where c.id = :courseId
           and s.id is null
    """)
    Long countNotSubmittedByCourseIdAndAssignmentId(@Param("courseId") Long courseId, @Param("assignmentId") Long assignmentId);

    @Query("""
        select new com.iny.side.submission.domain.vo.SubmissionDetailVo(
            ac.id,
            ac.name,
            ac.studentId,
            ac.username,
            s.id,
            s.submittedAt,
            (select count(c) 
               from ChatMessage c 
              where c.submission = s
            ),
            ev.score,
            s.primaryDiagnosis
        )
          from Enrollment e
          join e.account ac
     left join Submission s
            on s.student = ac
           and s.assignment.id = :assignmentId
     left join Evaluation ev
            on ev.submission = s
         where e.course.id = :courseId
           and ac.role = "STUDENT"
    """)
    Slice<SubmissionDetailVo> findAllByCourseIdAndAssignmentId(Long courseId, Long assignmentId, Pageable pageable);

    @Query("""
        select new com.iny.side.submission.domain.vo.SubmissionDetailVo(
            ac.id,
            ac.name,
            ac.studentId,
            ac.username,
            s.id,
            s.submittedAt,
            (select count(c) 
               from ChatMessage c 
              where c.submission = s
            ),
            ev.score,
            s.primaryDiagnosis
        )
          from Enrollment e
          join e.account ac
          join Submission s
            on s.student = ac
           and s.assignment.id = :assignmentId
          join Evaluation ev
            on ev.submission = s
         where e.course.id = :courseId
           and ac.role = "STUDENT"
    """)
    Slice<SubmissionDetailVo> findEvaluatedByCourseIdAndAssignmentId(Long courseId, Long assignmentId, Pageable pageable);

    @Query("""
        select new com.iny.side.submission.domain.vo.SubmissionDetailVo(
            ac.id,
            ac.name,
            ac.studentId,
            ac.username,
            s.id,
            s.submittedAt,
            (select count(c) 
               from ChatMessage c 
              where c.submission = s
            ),
            null,
            s.primaryDiagnosis
        )
          from Enrollment e
          join e.account ac
          join Submission s
            on s.student = ac
           and s.assignment.id = :assignmentId
     left join Evaluation ev
            on ev.submission = s
         where e.course.id = :courseId
           and ac.role = "STUDENT"
           and ev is null
    """)
    Slice<SubmissionDetailVo> findEvaluationRequiredByCourseIdAndAssignmentId(Long courseId, Long assignmentId, Pageable pageable);

    @Query("""
        select new com.iny.side.submission.domain.vo.SubmissionDetailVo(
            ac.id,
            ac.name,
            ac.studentId,
            ac.username,
            s.id,
            s.submittedAt,
            (select count(c) 
               from ChatMessage c 
              where c.submission = s
            ),
            null,
            s.primaryDiagnosis
        )
          from Enrollment e
          join e.account ac
     left join Submission s
            on s.student = ac
           and s.assignment.id = :assignmentId
         where e.course.id = :courseId
           and ac.role = "STUDENT"
           and s is null
    """)
    Slice<SubmissionDetailVo> findNotSubmittedByCourseIdAndAssignmentId(Long courseId, Long assignmentId, Pageable pageable);
}
