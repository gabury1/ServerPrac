package com.backend.ServerPrac.domain.post;

import com.backend.ServerPrac.domain.post.dto.PostCreateDto;
import com.backend.ServerPrac.global.apiPayload.code.Status.ErrorStatus;
import com.backend.ServerPrac.global.apiPayload.code.exception.GeneralException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService
{
    public final PostRepository postRepository;
    private final JPAQueryFactory jpaQueryFactory;

    private final QPost post = QPost.post;

    public Post submit(PostCreateDto postCreateDto)
    {
        Post p = postRepository.save(Post.of(postCreateDto));
        return p;
    }

    public List<Post> search(String title, String content) {
        var predicate = new BooleanBuilder();

        if (title != null) {
            predicate.and(post.title.eq(title));
        }

        if (content != null) {
            predicate.and(post.content.goe(content));
        }

        return jpaQueryFactory       // 모든 값을 받아오고 싶다면 List<tuple>로 반환하면 되겠다.
                .selectFrom(post)
                .where(predicate)
                .fetch();
    }

    public int flagCheck(int flag)
    {
        if(flag == 1) throw new GeneralException(ErrorStatus.TEMP_EXCEPTION);
        return flag;
    }

}

// QClass : 자동으로 빌드됨. 해당 클래스를 통해 타입안정성, 자동완성의 이점을 얻을 수 있음.
// BooleanBuilder : where문 입력에 사용됨.
// Join을 통해 여러 테이블 값을 동시에 받고 싶다면,
// List<tuple>로 반환을 받아낸다.
