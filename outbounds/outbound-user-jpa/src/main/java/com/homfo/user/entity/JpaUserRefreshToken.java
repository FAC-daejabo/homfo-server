package com.homfo.user.entity;

import com.homfo.auth.entity.Jwt;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_refresh_token")
@Entity
public class JpaUserRefreshToken extends Jwt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(length = 60)
    private String token;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public JpaUserRefreshToken(Long id, String token) {
        validateToken(token);

        this.id = id;
        this.token = token;
    }
}
