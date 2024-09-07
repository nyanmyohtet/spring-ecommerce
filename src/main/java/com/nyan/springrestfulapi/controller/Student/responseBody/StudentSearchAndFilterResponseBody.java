package com.nyan.springrestfulapi.controller.Student.responseBody;

import com.nyan.springrestfulapi.controller.global.Pagination.Pagination;
import com.nyan.springrestfulapi.dto.StudentDto;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class StudentSearchAndFilterResponseBody {

    private HttpStatus httpStatus;
    private Pagination page;
    private List<StudentDto> dataList;

    public StudentSearchAndFilterResponseBody withDataList(List<StudentDto> dataList) {
        this.dataList = dataList;
        return this;
    }

    public StudentSearchAndFilterResponseBody withPage(Pagination page) {
        this.page = page;
        return this;
    }
}
