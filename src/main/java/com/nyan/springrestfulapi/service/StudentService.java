package com.nyan.springrestfulapi.service;

import com.nyan.springrestfulapi.controller.Student.responseBody.StudentSearchAndFilterResponseBody;
import com.nyan.springrestfulapi.dto.StudentDto;
import com.nyan.springrestfulapi.dto.StudentExistingDto;
import com.nyan.springrestfulapi.dto.StudentNewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {

    StudentDto addStudent(StudentNewDto studentNewDto);

    StudentDto updateStudent(StudentExistingDto studentExistingDto);

    StudentDto updateStudentWithVersion(StudentExistingDto studentExistingDto);

    void deleteStudent(Integer studentId);

    StudentDto getStudentById(Integer studentId);

    Page<StudentDto> getAllStudents(Pageable pageable);

    StudentSearchAndFilterResponseBody getStudentsBySearchAndFilter(String firstName, String lastName, Pageable pageable);
}
