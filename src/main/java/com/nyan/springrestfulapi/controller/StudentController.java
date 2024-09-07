package com.nyan.springrestfulapi.controller;

import com.nyan.springrestfulapi.controller.Student.responseBody.StudentSearchAndFilterResponseBody;
import com.nyan.springrestfulapi.dto.StudentDto;
import com.nyan.springrestfulapi.dto.StudentExistingDto;
import com.nyan.springrestfulapi.dto.StudentNewDto;
import com.nyan.springrestfulapi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/students")
public class StudentController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private StudentService studentService;

    @PostMapping(path = "/add")
    public ResponseEntity<StudentDto> addStudent(@RequestBody @Valid StudentNewDto studentNewDto) {
        StudentDto student = studentService.addStudent(studentNewDto);
        return ResponseEntity.ok(student);
    }

    @PostMapping(path = "/update")
    public ResponseEntity<StudentDto> updateStudent(@RequestBody  @Valid StudentExistingDto studentExistingDto) {
        StudentDto student = studentService.updateStudent(studentExistingDto);
        return ResponseEntity.ok(student);
    }

    @PostMapping(path = "/update-with-version")
    public ResponseEntity<StudentDto> updateStudentWithVersion(@RequestBody  @Valid StudentExistingDto studentExistingDto) {
        StudentDto student = studentService.updateStudentWithVersion(studentExistingDto);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping(path = "/{studentId}/delete")
    public void deleteStudent(@PathVariable(name = "studentId") Integer studentId) {
        studentService.deleteStudent(studentId);
    }

    @GetMapping(path = "/{studentId}")
    public ResponseEntity<StudentDto> getStudent(@PathVariable(name = "studentId") Integer studentId) {
        StudentDto student = studentService.getStudentById(studentId);
        return ResponseEntity.ok(student);
    }

    @GetMapping(path = "/demo-bulk-update")
    public ResponseEntity<String> demoBulkUpdate() {
        String query = "select * from student";

        List<Object[]> resultList = entityManager.createNativeQuery(query).getResultList();

        return ResponseEntity.ok("demo bulk update");
    }

    @GetMapping(path = "/all")
    public ResponseEntity<String> getStudents(
            @PageableDefault(page = 0, size = 30)
            @SortDefault.SortDefaults({@SortDefault(sort = "modified", direction = Sort.Direction.DESC)})
            Pageable pageable) {
//        Page<StudentDto> students = studentService.getAllStudents(pageable);

        List<String> ll = new ArrayList<>();
        ll.add("Yann");
        ll.add("Kyaw");

//        String query = " select s.* from student s where s.first_name in ('Yann', 'Kyaw')" ;
//        String query = " select s.* from student s where s.first_name in (" + ll + ") " ;
        String query = " select s.* from student s where s.first_name in :pList " ;

        Query query1 = entityManager.createNativeQuery(query);
        query1.setParameter("pList", ll);
        List<Object[]> resultList = query1.getResultList();

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
        try {
            Date dd = dateFormat.parse(dateFormat.format(new Date()));

            return ResponseEntity.ok(dd.toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
/*
        String dt = "2008-01-01";  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        c.add(Calendar.DATE, 1);  // number of days to add
        dt = sdf.format(c.getTime());  // dt is now the new date
//        return ResponseEntity.ok(dt);
*/
    }

    @GetMapping(path = "/search-and-filter")
    public ResponseEntity<StudentSearchAndFilterResponseBody> getStudentsBySearchAndFilter(
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @PageableDefault(page = 0, size = 30)
            @SortDefault.SortDefaults({@SortDefault(sort = "modified", direction = Sort.Direction.DESC)})
            Pageable pageable) {
        StudentSearchAndFilterResponseBody students = studentService.getStudentsBySearchAndFilter(firstName, lastName, pageable);
        return ResponseEntity.ok(students);
    }
}
