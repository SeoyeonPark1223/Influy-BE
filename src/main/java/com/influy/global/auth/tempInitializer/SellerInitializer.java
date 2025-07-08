package com.influy.global.auth.tempInitializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.influy.domain.member.converter.MemberConverter;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.dto.MemberResponseDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.repository.MemberRepository;
import com.influy.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SellerInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final ObjectMapper objectMapper;


    @Override
    public void run(String... args) throws Exception {
        if (memberRepository.findByKakaoId(4339098764L).isEmpty()) {
            ClassPathResource resource = new ClassPathResource("tempUser/FrontUser.json");
            if (!resource.exists()) {
                throw new IOException("파일이 존재하지 않습니다.");
            }
            InputStream inputStream = resource.getInputStream();

            //JSON->JAVA 객체
            MemberRequestDTO.SellerJoin dto = objectMapper.readValue(inputStream, new TypeReference<MemberRequestDTO.SellerJoin>() {
            });

            Member member = memberService.joinSeller(dto);

            memberRepository.save(member);

        }
    }

}
