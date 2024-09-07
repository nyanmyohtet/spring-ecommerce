package com.nyan.springrestfulapi.repository;

import com.nyan.springrestfulapi.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    Page<Student> findAll(Pageable pageable);

//    @Query(value = " select sr.id, sr.first_name, sr.last_name, sr.c_name from (select s.*, c.name as c_name from Student s " +
//            " left join Course c on s.course_name = c.name" +
//            " where ((?1 is null or s.first_name like lower(CONCAT('%',?1,'%'))) " +
//            " and (?2 is null or s.last_name like lower(CONCAT('%',?2,'%'))))) sr " +
//            " where (?3 is null or sr.course_name in (?3, ?4)) ",
//            countQuery = " select count(sr.id) from (select s.*, c.name as c_name from Student s " +
//                    " left join Course c on s.course_name = c.name " +
//                    " where ((?1 is null or s.first_name like lower(CONCAT('%',?1,'%'))) " +
//                    " and (?2 is null or s.last_name like lower(CONCAT('%',?2,'%'))))) sr " +
//                    " where (?3 is null or sr.course_name in (?3, ?4)) ",
//            nativeQuery = true)
//    Page<Object[]> searchAndFilterByFirstNameStartingWithAndLastNameStartingWith(
//            String firstName, String lastName, String c1, String c2, Pageable pageable);

    @Query(value = " select s.id, s.first_name, s.last_name, s.course_name from Student s " +
//            " left join Course c on s.course_name = c.name ",
//            " where ((?1 is null or s.first_name like lower(CONCAT('%',?1,'%'))) " +
//            " and (?2 is null or s.last_name like lower(CONCAT('%',?2,'%'))))) sr " +
            " where (:pSomeNull = 0 and s.course_name is null) or s.course_name in :pCourseList ",
            countQuery = " select count(s.id) from Student s " +
//                    " left join Course c on s.course_name = c.name ",
//                    " where ((?1 is null or s.first_name like lower(CONCAT('%',?1,'%'))) " +
//                    " and (?2 is null or s.last_name like lower(CONCAT('%',?2,'%'))))) sr " +
                    " where (:pSomeNull = 0 and s.course_name is null) or s.course_name in :pCourseList ",
            nativeQuery = true)
    Page<Object[]> searchAndFilterByFirstNameStartingWithAndLastNameStartingWith(
            @Param("pCourseList") List<String> courseList,
            @Param("pSomeNull") Integer someNUll,
            Pageable pageable);

    Optional<Student> findFirstByFirstNameAndLastNameOrderByVersionNoDesc(String firstName, String lastName);

    Student findFirstByVersionIdOrderByVersionNoDesc(String versionId);

}
