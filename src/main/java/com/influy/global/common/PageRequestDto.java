package com.influy.global.common;

import com.influy.global.validation.annotation.CheckPage;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDto {
    @CheckPage
    private int page = 1;
    private int size = 10;

    public Pageable toPageable(Sort sort) {
        return PageRequest.of(page-1, size, sort);
    }
    public Pageable toPageable() {
        return PageRequest.of(page-1, size);
    }
}
