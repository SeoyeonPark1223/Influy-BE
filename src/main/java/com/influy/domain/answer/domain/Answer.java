package com.influy.domain.answer.domain;

import com.influy.domain.seller.domain.Seller;
import com.influy.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Builder @Getter
@AllArgsConstructor
@NoArgsConstructor
public class Answer extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String content;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

}
