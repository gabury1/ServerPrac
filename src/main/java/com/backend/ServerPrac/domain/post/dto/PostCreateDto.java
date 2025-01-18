package com.backend.ServerPrac.domain.post.dto;


import lombok.*;

@RequiredArgsConstructor @Getter
@Setter @ToString @EqualsAndHashCode
public class PostCreateDto
{
    String title;
    String content;
    Long writerNo;

}
