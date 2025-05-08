package com.influy.domain.user;

import com.influy.global.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder @Getter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @Id @GeneratedValue
    private Long id;
}
