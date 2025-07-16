package com.influy.domain.like.entity;

import com.influy.domain.item.entity.Item;
import com.influy.domain.like.repository.LikeRepository;
import com.influy.domain.member.entity.Member;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeScheduler {
    private final LikeRepository likeRepository;

    @Scheduled(cron = "0 0 0 * * *")
//    @Scheduled(cron = "0 */2 * * * *") 테스트용 (2분마다)
    @Transactional
    public void softDeleteLikes() {
        List<Like> toDeleteLikeList = likeRepository.findAllByLikeStatus(LikeStatus.UNLIKE);
        for (Like like : toDeleteLikeList) {
            Member member = like.getMember();
            if (member != null) member.getLikeList().remove(like);

            SellerProfile seller = like.getSeller();
            if (seller != null) seller.getLikeList().remove(like);

            Item item = like.getItem();
            if (item != null) item.getLikeList().remove(like);
        }
        likeRepository.deleteAll(toDeleteLikeList);
        log.info("{}개의 UNLIKE 찜을 삭제했습니다.", toDeleteLikeList.size());
    }
}
