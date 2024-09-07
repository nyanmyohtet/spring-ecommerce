package com.nyan.springrestfulapi.controller.global.Pagination;

import lombok.Data;

@Data
public class Pagination {

    private Integer number;
    private Integer size;
    private Integer totalPages;
    private Integer totalElements;

    public Pagination withNumber(Integer number) {
        this.number = number;
        return this;
    }

    public Pagination withSize(Integer size) {
        this.size = size;
        return this;
    }

    public Pagination withTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public Pagination withTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
        return this;
    }
}
