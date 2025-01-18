package com.backend.ServerPrac.domain.post;

import com.backend.ServerPrac.domain.post.dto.PostCreateDto;
import com.backend.ServerPrac.domain.user.User;
import com.backend.ServerPrac.global.domain.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor @ToString
@AllArgsConstructor
@Getter @Setter @Builder @Entity
public class Post extends BaseEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    String content;
    
    @ManyToOne
    @JoinColumn(name="writer_id", nullable = false)
    User writer;

    public static Post of(PostCreateDto postCreateDto)
    {
        return Post.builder()
                .title(postCreateDto.getTitle())
                .content(postCreateDto.getContent())
                .writer(new User(postCreateDto.getWriterNo()))
                .build();
    }
    
}
