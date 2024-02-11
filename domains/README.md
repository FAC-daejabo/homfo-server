# domains
도메인에만 집중하는 모듈입니다. 다음과 같은 원칙이 추가로 적용됩니다.

1. 어플리케이션의 로직을 모른다.
2. 하나의 모듈은 하나의 액터만을 책임진다.
3. 하나의 모듈은 최대 하나의 인프라에 대한 책임만 갖는다. (출처 : <a href="https://techblog.woowahan.com/2637/">멀티모듈 설계 이야기 with Spring, Gradle</a>)

2번과 3번 원칙을 같이 적용하기 위해서 도메인을 다음과 같이 세분화 했습니다.

+ domain-{actor} - 하나의 액터에 대한 도메인을 Interface로 관리합니다.
+ domain-{actor}-{infra} - 하나의 액터에 대한 도메인을, 하나의 인프라만을 사용해 실제로 구현합니다.

Infra에는 MySQL, Oracle, MongoDB 등이 사용될 수 있습니다. 실제 개발에서는 이러한 구현체를 참조하지 못 하게끔 다음과 같은 컨벤션을 가지고 있습니다.

1. 컴파일 타임에는 interface-module을 활용해, 실제 구현체와의 의존성을 분리한다. (참고 : <a href="https://www.youtube.com/watch?v=nuRcbCfW-YM&list=PLgXGHBqgT2TundZ81MAVHPzeYOTeII69j&index=7">모놀리식에서 점진적 서비스 분리: 사업과제와 병행하여 시스템 개선하기</a>)
2. 실제 구현체는 runtimeOnly로 적용한다.

이를 통해 다른 모듈에서는 실제 구현체를 전혀 몰라도 되고, 동시에 DB 커넥션 풀을 낭비하지 않을 수 있다는 장점이 있었습니다.

## 바로가기
- [domain-common](#domain-common)
- [domain-employee](#domain-employee)
- [domain-real-estate](#domain-real-estate)
- [domain-user](#domain-user)
- [코드 구성 방법](#코드-구성-방법)
 
## domain-common
JWT처럼 모든 액터에게 공통으로 적용되어야 하는 도메인을 관리합니다.

## domain-employee
관리자 액터와 관련있는 도메인을 Interface로 관리합니다.

### domain-employee-mysql
관리자 액터 도메인을 MySQL를 활용해 구현합니다. 현재 라이브 서버에 제공되는 DB입니다.

### domain-employee-oracle
관리자 액터 도메인을 Oracle DB를 활용해 구현합니다. 인스턴스 비용 문제로 Oracle Cloud Free Tier로 제공되는 autonomous database를 사용할 예정입니다.

## domain-real-estate
부동산 시스템 액터와 관련있는 도메인을 Interface로 관리합니다.

### domain-real-estate-mysql
부동산 시스템 도메인을 MySQL를 활용해 구현합니다. 현재 라이브 서버에 제공되는 DB입니다.

### domain-real-estate-oracle
부동산 시스템 도메인을 Oracle DB를 활용해 구현합니다. 인스턴스 비용 문제로 Oracle Cloud Free Tier로 제공되는 autonomous database를 사용할 예정입니다.

## domain-user
사용자 액터와 관련있는 도메인을 Interface로 관리합니다.

### domain-user-mysql
관리자 액터 도메인을 MySQL를 활용해 구현합니다. 현재 라이브 서버에 제공되는 DB입니다.

### domain-user-oracle
관리자 액터 도메인을 Oracle DB를 활용해 구현합니다. 인스턴스 비용 문제로 Oracle Cloud Free Tier로 제공되는 autonomous database를 사용할 예정입니다.

## 코드 구성 방법
Interface module에서는 추상 클래스, 추상 메소드나 인터페이스를 활용해서 규격을 만듭니다. 이후 infra module에서 실제 구현체를 만듭니다.

주석은 Interface module에 작성해야 실제 개발할 때 주석 설명을 볼 수 있습니다.

### Entity
#### Interface module
추상 클래스와 추상 메소드를 활용해 규격을 만듭니다. 다른 모듈에서는 이 모듈의 entity를 참조합니다.

```java
public abstract class User {
    /**
     * 계정은 8자 이상, 15자 이하의 대소문자 및 숫자로 구성되어야 합니다.
     */
    protected final String ACCOUNT_REGEXP = "^[a-zA-Z\\d]{8,15}$";
    
    /**
     * 사용자 ID
     * */
    public abstract Long getId();

    /**
     * 사용자 계정
     * */
    public abstract String getAccount();

    /**
     * 사용자 계정 검증
     * */
    protected void validateAccount(String account) {
        Assert.isTrue(Pattern.matches(ACCOUNT_REGEXP, Objects.requireNonNull(account)), "올바르지 않은 계정입니다.");
    }
}
```

#### Infra module
규격을 실제로 구현합니다. 다른 모듈에서는 런타임 시점에 이 entity를 사용합니다.

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class MySqlUser extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 15)
    private String account;
    
    @Builder
    public MySqlUser(String account) {
        validateAccount(account);
        
        this.account = account;
    }
}
```

### Service
#### Interface module
인터페이스를 활용해 규격을 만듭니다. 다른 모듈에서는 이 모듈의 service를 참조합니다.
```java
/**
 * 사용자 정보를 DB에 저장합니다.
 * */
public interface UserWriteService {
    /**
     * 계정 정보를 삭제합니다.
     * */
    void deleteAccount(long userId);
}
```

#### Infra module
규격을 실제로 구현합니다. 다른 모듈에서는 런타임 시점에 이 service를 사용합니다.
```java
/**
 * 사용자 정보를 DB에 저장합니다.
 * */
public class MySqlUserWriteService implements UserWriteService {
    @Override
    public void deleteAccount(long userId) {
        MySqlUser user = userRepository.findByIdAndStatusNot(userId, UserStatus.DELETED).orElseThrow(RuntimeException::new);

        user.deleteAccount();
        userRepository.save(user);
    }
}
```

### Repository
Infra module에서만 구현합니다. Spring의 JPA나 Mongo를 사용할 예정이고, 이런 DB는 Infra module에서만 접근 가능해야 합니다. 따라서 Interface module에서는 Service layer까지만 개방합니다.

실제로 JPA를 사용할 때 Repository의 Entity로 Interface module의 entity를 사용하면, Interface는 Entity로 사용할 수 없다는 에러가 발생합니다.

### Usecase
두 가지 이상의 서비스가 필요하거나 하나의 트랜잭션으로 묶어야할 필요가 있을 때 사용하는 레이어입니다.

다른 모듈에서는 Interface module의 Usecase를 참고합니다.

#### Interface module
```java
/**
 * 사용자 계정 정보를 삭제합니다.
 * */
public interface DeleteAccountUsecase {
    /**
     * 사용자 계정 삭제와 Refresh token 삭제를 한 트랜잭션으로 묶어야 합니다.
     * */
    void execute(long userId);
}
```

#### Infra module
```java
@Service
public class MySqlDeleteAccountUsecase implements DeleteAccountUsecase{
    private final UserWriteService userWriteService;

    private final UserRefreshTokenWriteService userRefreshTokenWriteService;

    public MySqlDeleteAccountUsecase(
            UserWriteService userWriteService,
            UserRefreshTokenWriteService userRefreshTokenWriteService
    ) {
        this.userWriteService = userWriteService;
        this.userRefreshTokenWriteService = userRefreshTokenWriteService;
    }

    /**
     * 사용자 정보를 삭제합니다.
     *
     * 하나라도 실패하면 삭제가 실패해야 해 한 트랜잭션으로 묶습니다.
     * */
    @Transactional
    public void execute(long userId) {
        userWriteService.deleteAccount(userId);
        userRefreshTokenWriteService.deleteByUserId(userId);
    }
}

```