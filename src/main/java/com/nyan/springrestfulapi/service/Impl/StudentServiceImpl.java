package com.nyan.springrestfulapi.service.Impl;

import com.nyan.springrestfulapi.controller.Student.responseBody.StudentSearchAndFilterResponseBody;
import com.nyan.springrestfulapi.controller.global.Pagination.Pagination;
import com.nyan.springrestfulapi.dto.StudentDto;
import com.nyan.springrestfulapi.dto.StudentExistingDto;
import com.nyan.springrestfulapi.dto.StudentNewDto;
import com.nyan.springrestfulapi.exception.ResourceNotFoundException;
import com.nyan.springrestfulapi.model.Student;
import com.nyan.springrestfulapi.repository.StudentRepository;
import com.nyan.springrestfulapi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public StudentDto addStudent(StudentNewDto studentNewDto) {
        Student student = new Student();
        student.setId(studentNewDto.getId());
        student.setFirstName(studentNewDto.getFirstName());
        student.setLastName(studentNewDto.getLastName());
        student.setEmail(studentNewDto.getEmail());
        student.setContactNumber(studentNewDto.getContactNumber());
        student.setCourseName(studentNewDto.getCourseName());
        student.setVersionId(UUID.randomUUID().toString());
        student.setVersionNo(1);

        student = studentRepository.save(student);

        StudentDto studentDto = new StudentDto();
        studentDto.setId(student.getId());
        studentDto.setFirstName(student.getFirstName());
        studentDto.setLastName(student.getLastName());
        studentDto.setEmail(student.getEmail());
        studentDto.setContactNumber(student.getContactNumber());
        studentDto.setCourseName(student.getCourseName());
        studentDto.setCreated(student.getCreated());
        studentDto.setModified(student.getModified());
        return studentDto;

    }

    @Override
    public StudentDto updateStudent(StudentExistingDto studentExistingDto) {
        Optional<Student> studentOpt = studentRepository.findById(studentExistingDto.getId());
        if (!studentOpt.isPresent()) {
            throw new ResourceNotFoundException("student not found");
        }
        Student student = studentOpt.get();
        student.setFirstName(studentExistingDto.getFirstName());
        student.setLastName(studentExistingDto.getLastName());
        student.setEmail(studentExistingDto.getEmail());
        student.setContactNumber(studentExistingDto.getContactNumber());
        student.setCourseName(studentExistingDto.getCourseName());

        student = studentRepository.save(student);

        StudentDto studentDto = new StudentDto();
        studentDto.setId(student.getId());
        studentDto.setFirstName(student.getFirstName());
        studentDto.setLastName(student.getLastName());
        studentDto.setEmail(student.getEmail());
        studentDto.setContactNumber(student.getContactNumber());
        studentDto.setCourseName(student.getCourseName());
        studentDto.setCreated(student.getCreated());
        studentDto.setModified(student.getModified());
        return studentDto;
    }

    @Override
    public StudentDto updateStudentWithVersion(StudentExistingDto studentExistingDto) {
        Optional<Student> studentOpt = studentRepository.findById(studentExistingDto.getId());
        if (!studentOpt.isPresent()) {
            throw new ResourceNotFoundException("student not found");
        }
        Student student = studentOpt.get();

        Optional<Student> studentOptional = studentRepository.findFirstByFirstNameAndLastNameOrderByVersionNoDesc(studentExistingDto.getFirstName(), studentExistingDto.getLastName());

        if (studentOptional.isPresent()) {
            throw new ResourceNotFoundException("duplicate first name and last name");
        }

        if (student.getStatus() != null && student.getStatus().equals("CBMApproved")) {

            Student studentLastVersion = studentRepository.findFirstByVersionIdOrderByVersionNoDesc(student.getVersionId());

            Student studentNext = new Student();
            studentNext.setFirstName(studentExistingDto.getFirstName());
            studentNext.setLastName(studentExistingDto.getLastName());
            studentNext.setEmail(studentExistingDto.getEmail());
            studentNext.setContactNumber(studentExistingDto.getContactNumber());
            studentNext.setCourseName(studentExistingDto.getCourseName());
            studentNext.setVersionId(student.getVersionId());
            studentNext.setVersionNo(studentLastVersion.getVersionNo() + 1);
//            studentNext.setVersionNo(student.getVersionNo() + 1);

            student = studentRepository.save(studentNext);
        } else {
            student.setFirstName(studentExistingDto.getFirstName());
            student.setLastName(studentExistingDto.getLastName());
            student.setEmail(studentExistingDto.getEmail());
            student.setContactNumber(studentExistingDto.getContactNumber());
            student.setCourseName(studentExistingDto.getCourseName());

            student = studentRepository.save(student);
        }

        StudentDto studentDto = new StudentDto();
        studentDto.setId(student.getId());
        studentDto.setFirstName(student.getFirstName());
        studentDto.setLastName(student.getLastName());
        studentDto.setEmail(student.getEmail());
        studentDto.setContactNumber(student.getContactNumber());
        studentDto.setCourseName(student.getCourseName());
        studentDto.setCreated(student.getCreated());
        studentDto.setModified(student.getModified());
        studentDto.setVersionId(student.getVersionId());
        studentDto.setVersionNo(student.getVersionNo());
        return studentDto;
    }

    @Override
    public void deleteStudent(Integer studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("studentId must not be null");
        }
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (!studentOpt.isPresent()) {
            throw new ResourceNotFoundException("student not found");
        }
        studentRepository.deleteById(studentId);
    }

    @Override
    public StudentDto getStudentById(Integer studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("studentId must not be null");
        }

        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (!studentOpt.isPresent()) {
            throw new ResourceNotFoundException("student not found");
        }
        Student student = studentOpt.get();

        StudentDto studentDto = new StudentDto();
        studentDto.setId(student.getId());
        studentDto.setFirstName(student.getFirstName());
        studentDto.setLastName(student.getLastName());
        studentDto.setEmail(student.getEmail());
        studentDto.setContactNumber(student.getContactNumber());
        studentDto.setCourseName(student.getCourseName());
        studentDto.setCreated(student.getCreated());
        studentDto.setModified(student.getModified());
        return studentDto;
    }

    @Override
    public Page<StudentDto> getAllStudents(Pageable pageable) {
        Page<Student> studentsPage = studentRepository.findAll(pageable);

        List<StudentDto> studentsDto = new ArrayList<>();
        Page<StudentDto> studentsDtoPage = new PageImpl<>(studentsDto, pageable, 0);

        if (studentsPage != null && !studentsPage.isEmpty()) {

            studentsPage.getContent().forEach(student -> {
                StudentDto studentDto = new StudentDto();
                studentDto.setId(student.getId());
                studentDto.setFirstName(student.getFirstName());
                studentDto.setLastName(student.getLastName());
                studentDto.setEmail(student.getEmail());
                studentDto.setContactNumber(student.getContactNumber());
                studentDto.setCourseName(student.getCourseName());
                studentDto.setCreated(student.getCreated());
                studentDto.setModified(student.getModified());
                studentDto.setVersionId(student.getVersionId());
                studentDto.setVersionNo(student.getVersionNo());

                studentsDto.add(studentDto);
            });
            studentsDtoPage =
                    new PageImpl<>(studentsDto, pageable, studentsPage.getTotalElements());
        }
        return studentsDtoPage;
    }

    @Override
    public StudentSearchAndFilterResponseBody getStudentsBySearchAndFilter(String firstName, String lastName, Pageable pageable) {

        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        List<String> courseList = new ArrayList<>();
//        courseList.add(null);
//        courseList.add("c1");
//        courseList.add("c2");

        Integer someNUll = 0;

        Page<Object[]> studentsList = studentRepository.searchAndFilterByFirstNameStartingWithAndLastNameStartingWith(courseList, someNUll, pageRequest);

        List<StudentDto> studentsDto = new ArrayList<>();

        Integer count = null;
        Pagination page = new Pagination();

        if (!studentsList.isEmpty()) {

            count = Math.toIntExact(studentsList.getTotalElements());
            page.setTotalElements(count);
            page.setTotalPages(studentsList.getTotalPages());
            page.setSize(studentsList.getSize());
            page.setNumber(studentsList.getNumber());

            for (Object[] student: studentsList) {
                StudentDto studentDto = new StudentDto();
                studentDto.setId((Integer) student[0]);
                studentDto.setFirstName((String) student[1]);
                studentDto.setLastName((String) student[2]);
//                studentDto.setEmail((String) student[3]);
//                studentDto.setContactNumber(student[4].toString());
                studentDto.setCourseName(student[3] != null ? student[3].toString() : "");
//                studentDto.setCreated(student.getCreated());
//                studentDto.setModified(student.getModified());

                studentsDto.add(studentDto);
            };

        }

        return new StudentSearchAndFilterResponseBody()
                .withDataList(studentsDto)
                .withPage(page);
    }

}
