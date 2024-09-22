package com.feature.recipesharingapp.data.specification;
import com.feature.recipesharingapp.exception.ApplicationErrorCode;
import com.feature.recipesharingapp.exception.CustomException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class Sorting {

    private Sorting(){

    }
    public static Sort sorting(String sortBy, String sortOrder) {
        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            if ("asc".equalsIgnoreCase(sortOrder)) {
                sort = Sort.by(sortBy).ascending();
            } else if ("desc".equalsIgnoreCase(sortOrder)) {
                sort = Sort.by(sortBy).descending();
            } else {
                throw new CustomException(ApplicationErrorCode.INVALID_SORT_ORDER);
            }
        }
        return sort;
    }
}