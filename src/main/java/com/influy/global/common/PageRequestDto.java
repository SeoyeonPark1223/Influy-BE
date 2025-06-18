package com.influy.global.common;

import com.influy.global.validation.annotation.CheckPage;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDto {
    @CheckPage
    private int page = 1;

    private int size = 1;


    public Pageable toPageable(Sort sort) {
        return PageRequest.of(page-1, size, sort);
    }
}
