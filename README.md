<div align=center>
	<img src="https://capsule-render.vercel.app/api?type=rect&color=auto&height=130&section=header&text=홈포 서버&fontSize=80&fontAlignY=53" />
</div>
<div align=center>
	<h3>📚 Tech Stack 📚</h3>
	<p>✨ Platforms & Languages ✨</p>
</div>
<div align="center">
    <img src="https://img.shields.io/badge/Java-6DB33F?style=flat&logo=Spring&logoColor=white" />
    <img src="https://img.shields.io/badge/Gradle-02303A?style=flat&logo=Gradle&logoColor=white" />
    <img src="https://img.shields.io/badge/Spring _MVC-6DB33F?style=flat&logo=Spring&logoColor=white" />
    <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white" />
    <img src="https://img.shields.io/badge/Redis-DC382D?style=flat&logo=Redis&logoColor=white" />
    <img src="https://img.shields.io/badge/Junit-25A162?style=flat&logo=Junit5&logoColor=white" />
    <img src="https://img.shields.io/badge/Jacoco-55C2E1?style=flat&logo=Cocos&logoColor=white" />
    <br/>    
    <img src="https://img.shields.io/badge/Oracle_Cloud-F80000?style=flat&logo=Oracle&logoColor=white" />
    <img src="https://img.shields.io/badge/GitHub_Actions-2088FF?style=flat&logo=GitHubActions&logoColor=white" />
    <img src="https://img.shields.io/badge/SonarQube-4E9BCD?style=flat&logo=SonarQube&logoColor=white" />
    <img src="https://img.shields.io/badge/Prometheus-E6522C?style=flat&logo=Prometheus&logoColor=white" />
<br/>
<br/>
<br/>
</div>

# 홈포 서버
홈포 서비스의 서버 프로젝트입니다. 모놀리틱 / 멀티모듈 / 헥사고날 으로 구성되어 있습니다.

## 목차
- [모듈](#모듈)

## 모듈
모듈은 다음과 같은 원칙하에 설계하고 있습니다.

+ 하위 모듈에만 의존성을 가진다.
+ 같은 계층의 모듈과는 의존성을 가지지 않는다.

### main
부팅가능한 어플리케이션입니다. Jackson, Security, Swagger 등 어플리케이션에 필요한 설정을 수행합니다.

개발 전체 환경에서 Domain, Inbounds, Outbounds 모듈에 대한 의존성을 가집니다. 런타임 환경에서 RDB에 대한 의존성을 가집니다.

### [Domains](domains/README.md)
도메인에만 집중하는 모듈입니다. 다음과 같은 원칙이 적용됩니다.

1. 어플리케이션의 로직을 모른다. 
2. 하나의 모듈은 하나의 액터만을 책임진다.
3. 모듈명은 domain-{actor}로 작명합니다.

#### 폴더 구조
```shell
.
├── command # Inbounds에서 제공되는 command
├── dto # domain 레벨에서 외부로 제공되는 데이터 형식 클래스
├── entity # Actor의 비즈니스 로직을 담는 클래스
├── infra # ETC
│   ├── enums # enum 폴더
│   └── util # Util 클래스 폴더
├── port # Outbounds와의 Interface
├── service # Inbounds, Outbounds, Entity 간 로직 매핑 클래스
└── usecase # Inbounds와의 Interface
```

### [Inbounds](inbounds/README.md)
서비스에 데이터를 요청하는 모듈입니다. 다음과 같은 원칙을 적용합니다.

+ Domains 모듈과 Interface로 소통합니다.
+ Outbounds 모듈에 대해서 알지 못합니다.
+ 확장성을 위해서 모듈명은 inbound-{외부 어플리케이션 명}-{통신 프로토콜} 으로 작명합니다.
+ 각 모듈에서는 인터페이스의 에러를 통신 프로토콜에 맞게 핸들링합니다.

### [Outbouds](outbounds/README.md)
서비스의 데이터를 외부로 저장하거나, 외부의 데이터를 불러오는 모듈입니다. 다음과 같은 원칙을 적용합니다.

+ Domains 모듈과 Interface로 소통합니다.
+ Inbounds 모듈에 대해서 알지 못합니다.
+ 확장성을 위해서 모듈명은 outbound-{액터 이름}-{구현체} 로 작명합니다.
+ 각 모듈에서는 인터페이스의 에러에 알맞게 에러를 발생시켜야 합니다.
+ 런타임 구현체는 main에서 import 합니다. ex) jpa에서 rdb는 mysql을 사용한다면, outbound 모듈이 아니라 main에서 runtimeOnly로 import

### [Core](core/README.md)
제일 하위 모듈입니다. 시스템 내 모든 레이어에서 공통으로 사용되어야 할 POJO, Util 등을 모아놓습니다.