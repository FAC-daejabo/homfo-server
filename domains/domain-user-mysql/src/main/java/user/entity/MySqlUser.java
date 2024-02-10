package user.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import user.infra.attributeConverter.UserStatusAttributeConverter;
import user.infra.enums.UserStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class MySqlUser implements User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 15)
  private String account;

  @Column(length = 60)
  private String password;

  @Column(length = 13)
  private String phoneNumber;

  @Column(length = 15)
  private String nickname;

  @Column(length = 1)
  private String gender;

  @Column(length = 10)
  private String birthday;

  @Column(length = 15)
  private String job;

  @Column(length = 1)
  @Convert(converter = UserStatusAttributeConverter.class)
  private UserStatus status = UserStatus.USE;

  @Column(length = 50)
  private String refreshToken;

  @Column(updatable = false)
  @CreationTimestamp
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime updatedAt;
}
