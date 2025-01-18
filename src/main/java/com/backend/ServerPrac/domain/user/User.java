package com.backend.ServerPrac.domain.user;

import java.util.List;

import com.backend.ServerPrac.domain.post.Post;
import com.backend.ServerPrac.global.domain.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity @RequiredArgsConstructor @AllArgsConstructor 
@Builder @Getter @Setter @ToString
public class User extends BaseEntity
{
    public User(Long id)
    {
        this.id = id;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name; // 이게 로그인 아이디가 될 예정
    String password;
    String email;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Post> posts;
}
