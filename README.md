![vaccine](https://user-images.githubusercontent.com/47212652/122642476-82a91600-d145-11eb-86c4-365c8d78fcae.png)

# 백신 예약 시스템
# Table of contents
- [백신 예약 시스템](#백신-예약-시스템)
- [Table of contents](#table-of-contents)
- [서비스 시나리오](#서비스-시나리오)
- [체크포인트](#체크포인트)
- [분석/설계](#분석설계)
  - [AS-IS 조직 (Horizontally-Aligned)](#as-is-조직-horizontally-aligned)
  - [TO-BE 조직 (Vertically-Aligned)](#to-be-조직-vertically-aligned)
  - [Event Storming 결과](#event-storming-결과)
    - [이벤트 도출](#이벤트-도출)
    - [액터, 커맨드 부착하여 읽기 좋게](#액터-커맨드-부착하여-읽기-좋게)
    - [어그리게잇으로 묶기](#어그리게잇으로-묶기)
    - [바운디드 컨텍스트로 묶기](#바운디드-컨텍스트로-묶기)
    - [폴리시의 이동과 컨텍스트 매핑 (점선은 Pub/Sub, 실선은 Req/Resp)](#폴리시의-이동과-컨텍스트-매핑-점선은-pubsub-실선은-reqresp)
    - [기능적 요구사항 검증](#기능적-요구사항-검증)
    - [비기능 요구사항 검증](#비기능-요구사항-검증)
    - [완성된 모델](#완성된-모델)
  - [헥사고날 아키텍처 다이어그램 도출](#헥사고날-아키텍처-다이어그램-도출)
- [구현:](#구현)
  - [DDD 의 적용](#ddd-의-적용)
  - [CQRS](#cqrs)
  - [폴리글랏 퍼시스턴스](#폴리글랏-퍼시스턴스)
  - [Gateway 적용](#gateway-적용)
  - [동기식 호출 과 Fallback 처리](#동기식-호출-과-fallback-처리)
  - [비동기식 호출 / 시간적 디커플링 / 장애격리 / 최종 (Eventual) 일관성 테스트](#비동기식-호출--시간적-디커플링--장애격리--최종-eventual-일관성-테스트)
- [운영](#운영)
  - [CI/CD 설정](#cicd-설정)
  - [동기식 호출 / 서킷 브레이킹 / 장애격리](#동기식-호출--서킷-브레이킹--장애격리)
  - [오토스케일아웃 (HPA)](#오토스케일아웃-hpa)
  - [ConfigMap](#configmap)
  - [Zero-downtime deploy (Readiness Probe)](#zero-downtime-deploy-readiness-probe)
  - [Self-healing (Liveness Probe)](#self-healing-liveness-probe)


# 서비스 시나리오


기능적 요구사항
1. 병원 관리자는 병원정보를 등록한다
2. 고객은 백신을 예약한다
3. 예약이 되면 백신이 할당된다
4. 백신이 할당되면 백신이 있는 병원에 등록되며 예약 현황이 업데이트 된다.
5. 고객은 백신 예약을 취소할 수 있다
6. 예약이 취소되면 백신 할당 및 병원 등록이 취소되며 예약 현황이 업데이트 된다.
7. 예약 현황은 언제나 확인할 수 있다
8. 병원에 등록/등록취소가 되면 알림을 보낸다.


비기능적 요구사항
1. 트랜잭션
    > `Sync 호출`
    - 백신이 할당되지 않으면 병원에 등록되지 않는다. 

2. 장애격리
    > `Async (event-driven)` / `Eventual Consistency`
    - 백신 관리 기능이 수행되지 않더라도 신청은 365일 24시간 가능해야한다. 
    > `Circuit Breaker` / `Fallback`
    - 백신 예약이 많아 백신 할당이 급증하여 장애가 발생하면 병원에 등록되는 것은 잠시 뒤에 처리되도록 한다 

3. 성능
    > `CQRS`
    - 고객은 백신예약 현황을 언제든지 확인할 수 있어야한다 . 
    > `Event Driven`
    - 예약상태가 변경되면 알림을 줄 수 있어야한다 


# 체크포인트
- 체크포인트 : https://workflowy.com/s/assessment-check-po/T5YrzcMewfo4J6LW
1. Saga
2. CQRS
3. Correlation
4. Req/Resp
5. Gateway
6. Deploy/ Pipeline
7. Circuit Breaker
8. Autoscale (HPA)
9. Zero-downtime deploy (Readiness Probe)
10. Config Map/ Persistence Volume
11. Polyglot
12. Self-healing (Liveness Probe)

# 분석/설계


## AS-IS 조직 (Horizontally-Aligned)
![image](https://user-images.githubusercontent.com/47212652/123186329-da8fa600-d4d2-11eb-8dbb-18708aea4dcd.png)

## TO-BE 조직 (Vertically-Aligned)
![image](https://user-images.githubusercontent.com/47212652/123186353-e9765880-d4d2-11eb-99bd-62475aadffe7.png)

## Event Storming 결과
* MSAEZ 모델링한 이벤트스토밍 결과:  http://www.msaez.io/#/storming/pYauKq27pAMMO4ZZcMLRDtjzgIv1/share/40d9c225e0f9826deff3b8035d97b38f


### 이벤트 도출
![image](https://user-images.githubusercontent.com/47212652/123186376-f430ed80-d4d2-11eb-8c6d-e1dd84729e02.png)

### 액터, 커맨드 부착하여 읽기 좋게
![image](https://user-images.githubusercontent.com/47212652/123186404-03b03680-d4d3-11eb-8852-1e9fb5ba8c2e.png)

### 어그리게잇으로 묶기
![image](https://user-images.githubusercontent.com/47212652/123186422-0e6acb80-d4d3-11eb-8a09-11f82e8fff9e.png)

    - 예약관리, 백신관리, 병원관리 어그리게잇을 생성하고 그와 연결된 command와 event들에 의하여 트랜잭션이 유지되어야 하는 단위로 묶어줌

### 바운디드 컨텍스트로 묶기
![image](https://user-images.githubusercontent.com/47212652/123186451-22163200-d4d3-11eb-8a05-4c28132d8d08.png)

### 폴리시의 이동과 컨텍스트 매핑 (점선은 Pub/Sub, 실선은 Req/Resp)
![image](https://user-images.githubusercontent.com/47212652/123186733-bb454880-d4d3-11eb-969f-e6e4a77ece18.png)

  - 도메인 서열 분리 
    1. Core Domain(reservation, vaccine) : 없어서는 안될 핵심 서비스이며, 연견 Up-time SLA 수준을 99.999% 목표, 배포주기는 reservatoin 의 경우 1주일 1회 미만, vaccine 의 경우 1개월 1회 미만
    2. Supporting Domain(customer center) : 경쟁력을 내기위한 서비스이며, SLA 수준은 연간 60% 이상 uptime 목표, 배포주기는 각 팀의 자율이나 표준 스프린트 주기가 1주일 이므로 1주일 1회 이상을 기준으로 함.
    3. General Domain(hospital) : 병원 정보와 관련된 서비스로 병원들과 협약을 맺은 3rd Party 외부 서비스를 사용하는 것이 경쟁력이 높음 

### 기능적 요구사항 검증
![image](https://user-images.githubusercontent.com/47212652/123187659-889c4f80-d4d5-11eb-8b7b-47ee13d89f7d.png)

    - 병원 관리자는 병원정보를 등록한다 (o)
    - 고객은 백신을 예약한다 (o)
    - 예약이 되면 백신이 할당된다 (o)
    - 백신이 할당되면 백신이 있는 병원에 등록되며 예약 현황이 업데이트 된다. (o)

![image](https://user-images.githubusercontent.com/47212652/123187691-96ea6b80-d4d5-11eb-8de5-b29657f9c2de.png)

    - 고객은 백신 예약을 취소할 수 있다(o)
    - 예약이 취소되면 백신 할당 및 병원 등록이 취소되며 예약 현황이 업데이트 된다.(o)
 

### 비기능 요구사항 검증
![image](https://user-images.githubusercontent.com/47212652/123188194-94d4dc80-d4d6-11eb-9a60-ab9466df212a.png)

    - 트랜잭션
        > `Sync 호출`
        1. 백신이 할당되지 않으면 병원에 등록되지 않는다. 

    - 장애격리
        > `Async (event-driven)` / `Eventual Consistency`
        2. 백신 관리 기능이 수행되지 않더라도 신청은 365일 24시간 가능해야한다. 
        > `Circuit Breaker` / `Fallback`
        3. 백신 예약이 많아 백신 할당이 급증하여 장애가 발생하면 병원에 등록되는 것은 잠시 뒤에 처리되도록 한다 

    - 성능
        > `CQRS`
        4. 고객은 백신예약 현황을 언제든지 확인할 수 있어야한다 . 
        > `Event Driven`
        5. 예약상태가 변경되면 알림을 줄 수 있어야한다

### 완성된 모델
![image](https://user-images.githubusercontent.com/47212652/123186913-1e36df80-d4d4-11eb-955b-b0f20f938996.png)

    - 이후 이벤트, 어그리게잇 별로 변수 일부 추가됨
    - 수정된 모델은 모든 요구사항을 커버함.

## 헥사고날 아키텍처 다이어그램 도출
- 외부에서 들어오는 요청을 인바운드 포트를 호출해서 처리하는 인바운드 어댑터와 비즈니스 로직에서 들어온 요청을 회부 서비스를 호출해서 처리하는 아웃바운드 어댑터로 분리
- 호출관계에서 Pub/Sub 과 Req/Resp 를 구분함
- 서브 도메인과 바운디드 컨텍스트의 분리: 각 팀의 KPI 별로 아래와 같이 관심 구현 스토리를 나눠가짐
- 예약(Reservation)의 경우 Polyglot 적용을 위해 Hsqldb로 설계

![image](https://user-images.githubusercontent.com/47212652/123188263-b209ab00-d4d6-11eb-987e-6da0e887c208.png)


# 구현:

분석/설계 단계에서 도출된 헥사고날 아키텍처에 따라, 각 Bounded Context별로 마이크로서비스들을 스프링부트로 구현하였다. 구현한 각 서비스를 로컬에서 실행하는 방법은 아래와 같다. (각 서비스의 포트넘버는 8081 ~ 8084, 8088 이다)

```
cd Reservation
mvn spring-boot:run

cd Vaccine
mvn spring-boot:run 

cd Hospital
mvn spring-boot:run  

cd CustomerCenter
mvn spring-boot:run 

cd gateway
mvn spring-boot:run
```

## DDD 의 적용

- msaez.io에서 이벤트스토밍을 통해 DDD를 작성하고 Aggregate 단위로 Entity를 선언하여 구현을 진행하였다.

> Reservation 서비스의 Reservation.java
```java
package vaccinereservation;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Reservation_table")
public class Reservation {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Date date;
    private String status;  //예약신청 APPLYED / 예약취소 CANCELED / 예약완료 COMPLETED / 예약불가 CANTAPPLY
    private String userName;
    private String userPhone;
    private Long vaccineId;
    private Long hospitalId;

    @PostPersist
    public void onPostPersist(){
        VaccineReserved vaccineReserved = new VaccineReserved();
        vaccineReserved.setReservationId(this.id);
        vaccineReserved.setReservationStatus(this.status);
        vaccineReserved.setUserName(this.userName);
        vaccineReserved.setUserPhone(this.userPhone);
        vaccineReserved.setHospitalId(this.hospitalId);
        vaccineReserved.setReservationDate(this.date);
        vaccineReserved.publishAfterCommit();
    }

    @PostUpdate
    public void onPostUpdate(){
        if(this.getStatus().equals("CANCELED")){
            CanceledVaccineReservation canceledVaccineReservation = new CanceledVaccineReservation();
            canceledVaccineReservation.setReservationId(this.id);
            canceledVaccineReservation.setReservationStatus(this.status);
            canceledVaccineReservation.setVaccineId(this.vaccineId);
            canceledVaccineReservation.setHospitalId(this.hospitalId);
            canceledVaccineReservation.publishAfterCommit();
        }
    }

    public Long getId()                         {        return id;                         }
    public void setId(Long id)                  {        this.id = id;                      }     
    public Date getDate()                       {        return date;                       }
    public void setDate(Date date)              {        this.date = date;                  }
    public String getStatus()                   {        return status;                     }
    public void setStatus(String status)        {        this.status = status;              }
    public String getUserName()                 {        return userName;                   }
    public void setUserName(String userName)    {        this.userName = userName;          }
    public String getUserPhone()                {        return userPhone;                  }
    public void setUserPhone(String userPhone)  {        this.userPhone = userPhone;        }
    public Long getVaccineId()                  {        return vaccineId;                  }
    public void setVaccineId(Long vaccineId)    {        this.vaccineId = vaccineId;        }
    public Long getHospitalId()                 {        return hospitalId;                 }
    public void setHospitalId(Long hospitalId)  {        this.hospitalId = hospitalId;      }

    @Override
	public String toString() {
		return "Reservation [id=" + id + ", date=" + date + ", status=" + status + ", userName=" + userName
				+ ", userPhone=" + userPhone + ", vaccineId=" + vaccineId + ", hospitalId=" + hospitalId + "]";
	}

}


```

- Entity Pattern 과 Repository Pattern 을 적용하여 JPA 기반의 다양한 데이터소스 유형 (RDB or NoSQL) 에 대한 별도의 처리 없이 데이터 접근 어댑터를 자동 생성하기 위하여 Spring Data REST 의 RestRepository 를 적용하였다.

> Reservation 서비스의 ReservationRepository.java
```java
package vaccinereservation;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="reservations", path="reservations")
public interface ReservationRepository extends PagingAndSortingRepository<Reservation, Long>{

}
```
> Reservation 서비스의 PolicyHandler.java
```java
package vaccinereservation;

import vaccinereservation.config.kafka.KafkaProcessor;

import java.util.Optional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @Autowired ReservationRepository reservationRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCanceledVaccineAssigned_UpdateedReservationStatus(@Payload CanceledVaccineAssigned canceledVaccineAssigned){

        if(!canceledVaccineAssigned.validate()) return;

        System.out.println("\n\n##### listener UpdateedReservationStatus : " + canceledVaccineAssigned.toJson() + "\n\n");
        Optional<Reservation> optional = reservationRepository.findById(canceledVaccineAssigned.getReservationId());
        Reservation reservation = optional.get();
        if(canceledVaccineAssigned.getReservationStatus().equals("CANTAPPLY")){
            //신청 불가 - 이미 불가인 상태라서.
            reservation.setStatus("CANTAPPLY");
        }
        else{
            reservation.setStatus("CANCELED");
        }
        reservationRepository.save(reservation);
            
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverVaccineAssigned_UpdateedReservationStatus(@Payload VaccineAssigned vaccineAssigned){

        if(!vaccineAssigned.validate()) return;

        System.out.println("\n\n##### listener UpdateedReservationStatus : " + vaccineAssigned.toJson() + "\n\n");       
        Optional<Reservation> optional = reservationRepository.findById(vaccineAssigned.getReservationId());
        Reservation reservation = optional.get();
        //할당 받은 경우, 백신 및 병원 정보 업데이트
        if(vaccineAssigned.getVaccineStatus().equals("ASSIGNED")){
            reservation.setStatus("COMPLETED");
            reservation.setVaccineId(vaccineAssigned.getVaccineId());
            reservation.setHospitalId(vaccineAssigned.getHospitalId());
            //있었다면 세팅이 되는 것
        }//백신 있는 병원이 없어서 불가한 경우
        else if(vaccineAssigned.getVaccineStatus().equals("CANTAPPLY")){
            reservation.setStatus("CANTAPPLY");
        }
        reservationRepository.save(reservation); //예약이 업데이트 됨          
    }
}

```

- 적용 후 REST API 의 테스트
```cmd
// 병원 정보 등록
http POST http://localhost:8083/hospitals name=samsung location=gangnam status=PERSIST

// 병원 정보 수정
http PATCH http://localhost:8083/hospitals/1 vaccineType=1 vaccineName=moderna vaccineCount=100 status=MODIFIED

//백신 정보 등록
http POST http://localhost:8082/vaccines name=moderna type=1 date=2021-06-01 validationDate=2022-05-31 status=CANUSE

//예약 신청
http POST http://localhost:8081/reservations date=2021-06-20 userName=check userPhone=010-1234-5678 status=APPLYED

// 확인
http GET http://localhost:8081/reservations/1
http GET http://localhost:8082/vaccines/1
http GET http://localhost:8083/hospitals/1
http GET http://localhost:8084/reservationStatuses/1

// 예약 취소
http PATCH http://localhost:8081/reservations/1 status=CANCELED

// 확인
http GET http://localhost:8081/reservations/1
http GET http://localhost:8082/vaccines/1
http GET http://localhost:8083/hospitals/1
http GET http://localhost:8084/reservationStatuses/1
```

> 백신 예약 신청 후 Reservation 동작 결과
![image](https://user-images.githubusercontent.com/47212652/123189901-97850100-d4d9-11eb-8155-3bb04e8cdc5c.png)
![image](https://user-images.githubusercontent.com/47212652/123189958-af5c8500-d4d9-11eb-9076-71b07a8e76f1.png)

## CQRS

- Materialized View 구현을 통해 다른 마이크로서비스의 데이터 원본에 접근없이 내 서비스의 화면 구성과 잦은 조회가 가능하게 하였습니다. 본 과제에서 View 서비스는 CustomerCenter 서비스가 수행하며 예약 상태를 보여준다.

> 백신 예약 신청 후 customerCenter 결과(백신 할당된 상태)
![image](https://user-images.githubusercontent.com/47212652/123190050-d31fcb00-d4d9-11eb-8e52-bff822df7eb9.png)

## 폴리글랏 퍼시스턴스

- 예약(Reservation)의 경우 H2 DB인 백신(Vaccine)/병원(Hospital) 서비스와 달리 Hsqldb로 구현하여 MSA의 서비스간 서로 다른 종류의 DB에도 문제없이 동작하여 다형성을 만족하는지 확인하였다.

> Vaccine, Hospital, CustomerCenter 서비스의 pom.xml 설정
```xml
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
```
> Reservation 서비스의 pom.xml 설정
```xml
    <dependency>
        <groupId>org.hsqldb</groupId>
        <artifactId>hsqldb</artifactId>
        <scope>runtime</scope>
    </dependency>
```
## Gateway 적용
- API Gateway를 통하여 마이크로서비스들의 진입점을 단일화하였습니다.
> gateway > application.yaml 설정
```yaml
server:
  port: 8088

---

spring:
  profiles: default
  cloud:
    gateway:
      routes:
        - id: Reservation
          uri: http://localhost:8081
          predicates:
            - Path=/reservations/** 
        - id: Vaccine
          uri: http://localhost:8082
          predicates:
            - Path=/vaccines/** 
        - id: Hospital
          uri: http://localhost:8083
          predicates:
            - Path=/hospitals/** 
        - id: CustomerCenter
          uri: http://localhost:8084
          predicates:
            - Path= /reservationStatuses/**
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins:
              - "*"
            allowedMethods:
              - "*"
            allowedHeaders:
              - "*"
            allowCredentials: true

---

spring:
  profiles: docker
  cloud:
    gateway:
      routes:
        - id: Reservation
          uri: http://Reservation:8080
          predicates:
            - Path=/reservations/** 
        - id: Vaccine
          uri: http://Vaccine:8080
          predicates:
            - Path=/vaccines/** 
        - id: Hospital
          uri: http://Hospital:8080
          predicates:
            - Path=/hospitals/** 
        - id: CustomerCenter
          uri: http://CustomerCenter:8080
          predicates:
            - Path= /reservationStatuses/**
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins:
              - "*"
            allowedMethods:
              - "*"
            allowedHeaders:
              - "*"
            allowCredentials: true

server:
  port: 8080
```
- 단일화 된 진입점 8088포트로 진입함을 확인할 수 있습니다.

![image](https://user-images.githubusercontent.com/47212652/123190392-6c4ee180-d4da-11eb-9f5d-b81b90cb844e.png)
## 동기식 호출 과 Fallback 처리

분석단계에서의 조건 중 하나로 Vaccine -> Hospital 간의 호출은 동기식 일관성을 유지하는 트랜잭션으로 처리하기로 하였다. 호출 프로토콜은 이미 앞서 Rest Repository 에 의해 노출되어있는 REST 서비스를 FeignClient 를 이용하여 호출하도록 한다. 

- 병원서비스를 호출하기 위하여 FeignClient를 이용하여 Service 대행 인터페이스 (Proxy) 를 구현 

> Pay 서비스의 external\PayService.java

```java
package vaccinereservation.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;
import java.util.Date;

@FeignClient(name="Hospital", url="http://localhost:8083")
public interface HospitalService {

    @RequestMapping(method= RequestMethod.GET, path="/hospitals/assignHospital")
    public Map<String,String> assignHospital(@RequestParam("vaccineType") Long vaccineType, @RequestParam("vaccineId") Long vaccineId, @RequestParam("reservationId") Long reservationId);
}
```

- 예약을 받고, PolicyHandler에서 할당가능한 백신 있을 때 업데이트하면서 (@PostUpdate) 병원 할당을 요청하도록 처리

> Vaccine 서비스의 Vaccine.java (Entity)

```java
    @PostUpdate
    public void onPostUpdate(){
        //백신 할당 시 Request 보내고 가서 백신있는 병원 찾고 Response 받음
        if(this.status.equals("ASSIGNED")){
            String hospitalStatus = "";
            String hospitalId = "";
            String vaccineStatus =this.status;
            try {
                Map<String,String> res = VaccineApplication.applicationContext
                                                           .getBean(vaccinereservation.external.HospitalService.class)
                                                           .assignHospital(this.getType(),this.getId(),this.getReservationId());

                hospitalStatus = res.get("status")==null?"":res.get("status");
                hospitalId = res.get("hospitalId").equals("-1")?"-1":res.get("hospitalId");
                if(hospitalStatus.equals("EMPTYVACCINE")){
                    vaccineStatus = "CANTAPPLY";
                }else if(hospitalStatus.equals("ASSIGNED")){
                    vaccineStatus = "ASSIGNED";
                }
                System.out.println("백신상태 : "+vaccineStatus);
            } catch (Exception e) {
                e.printStackTrace();
            }    
        
            VaccineAssigned vaccineAssigned = new VaccineAssigned();
            vaccineAssigned.setVaccineId(this.id);
            vaccineAssigned.setVaccineName(this.name);
            vaccineAssigned.setVaccineType(this.type);
            vaccineAssigned.setVaccineStatus(vaccineStatus);
            vaccineAssigned.setVaccineDate(this.date);
            vaccineAssigned.setVaccineValidationDate(this.validationDate);
            vaccineAssigned.setReservationId(this.reservationId);
            vaccineAssigned.setReservationStatus(vaccineStatus);
            vaccineAssigned.setHospitalId(Long.valueOf(hospitalId)); 
            vaccineAssigned.publishAfterCommit();
        }
        ...
    }
```

- 동기식 호출에서는 호출 시간에 따른 타임 커플링이 발생하며, 병원 시스템이 장애가 나면 백신할당도 못받는다는 것을 확인:


```
# 병원 (hospital) 서비스를 잠시 내려놓음 (ctrl+c)

# 예약 신청 처리 (예약신청 시 -> 할당가능한 백신 찾고 그 백신 있는 병원 할당)
http POST http://localhost:8081/reservations date=2021-06-20 userName=check userPhone=010-1234-5678 status=APPLYED # Fail

```
> 병원 할당 요청 오류 발생
![image](https://user-images.githubusercontent.com/47212652/123200262-53026100-d4eb-11eb-8ace-cad49674d9d1.png)
```
#병원서비스 재기동
cd hospital
mvn spring-boot:run

http POST http://localhost:8088/hospitals name=samsung location=gangnam status=PERSIST
http PATCH http://localhost:8088/hospitals/1 vaccineType=1 vaccineName=moderna vaccineCount=100 status=MODIFIED
http POST http://localhost:8088/vaccines name=moderna type=1 date=2021-06-01 validationDate=2022-05-31 status=CANUSE

#예약 처리
http POST http://localhost:8088/reservations date=2021-06-20 userName=check userPhone=010-1234-5678 status=APPLYED # Success
```

- 또한 과도한 요청시에 서비스 장애가 도미노 처럼 벌어질 수 있다. (서킷브레이커, 폴백 처리는 운영단계에서 설명한다.)


## 비동기식 호출 / 시간적 디커플링 / 장애격리 / 최종 (Eventual) 일관성 테스트


예약신청이 이루어진 후에 백신 할당(Vaccine)으로 이를 알려주는 행위는 동기식이 아니라 비동기식으로 처리하여 예약 신청 서비스의 처리를 위하여 백신 할당이 블로킹 되지 않아도록 처리한다.
 
- 이를 위하여 예약신청이력을 기록을 남긴 후에 곧바로 백신신청이 되었다는 도메인 이벤트를 카프카로 송출한다(Publish)
 
```java
package vaccinereservation;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Reservation_table")
public class Reservation {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Date date;
    private String status;  //예약신청 APPLYED / 예약취소 CANCELED / 예약완료 COMPLETED / 예약불가 CANTAPPLY
    private String userName;
    private String userPhone;
    private Long vaccineId;
    private Long hospitalId;

    @PostPersist
    public void onPostPersist(){
        VaccineReserved vaccineReserved = new VaccineReserved();
        vaccineReserved.setReservationId(this.id);
        vaccineReserved.setReservationStatus(this.status);
        vaccineReserved.setUserName(this.userName);
        vaccineReserved.setUserPhone(this.userPhone);
        vaccineReserved.setHospitalId(this.hospitalId);
        vaccineReserved.setReservationDate(this.date);
        vaccineReserved.publishAfterCommit();
    }
   ...
}
```
- 백신 서비스에서는 예약신청 이벤트에 대해서 이를 수신하여 자신의 정책을 처리하도록 PolicyHandler 를 구현한다:

```java
package vaccinereservation;

import vaccinereservation.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @Autowired VaccineRepository vaccineRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverVaccineReserved_ReserveVaccine(@Payload VaccineReserved vaccineReserved){

        if(!vaccineReserved.validate()) return;

        System.out.println("\n\n##### listener ReserveVaccine : " + vaccineReserved.toJson() + "\n\n");

        // Sample Logic //
        Vaccine vaccine;
        boolean check = false;

        final Iterable<Vaccine> list = vaccineRepository.findAll();
        //사용가능한 백신을 찾아서 할당
        for(Vaccine v : list){
            if(v.getStatus().equals("CANUSE")){
                vaccine = v;
                vaccine.setStatus("ASSIGNED");
                vaccine.setReservationId(vaccineReserved.getReservationId());
                vaccine.setUserName(vaccineReserved.getUserName());
                vaccine.setUserPhone(vaccineReserved.getUserPhone());
                vaccine.setHospitalId(vaccineReserved.getHospitalId()); 
                check = true;
                vaccineRepository.save(vaccine);
                break;
            }
        }
        if(!check){ //백신이 없어서 신청 불가한 경우
            Vaccine cantVaccine = new Vaccine();
            cantVaccine.setStatus("CANTUSE");
            cantVaccine.setReservationId(vaccineReserved.getReservationId());
            vaccineRepository.save(cantVaccine);
        }
             
    }
    ...
}

```

예약관리 시스템은 백신관리 시스템과 완전히 분리되어있으며, 이벤트 수신에 따라 처리되기 때문에, 백신 관리 시스템이 유지보수로 인해 잠시 내려간 상태라도 신청을 받는데 문제가 없다:
```
# 백신 관리 시스템 (Vaccine) 를 잠시 내려놓음 (ctrl+c)

#신청 처리
http POST http://localhost:8081/reservations date=2021-06-21 userName=tester userPhone=010-1234-5679 status=APPLYED   #Success

#신청 상태 확인
http GET http://localhost:8088/reservations/1     # 신청 상태. 현재 불가임을 보여줌
- 인메모리 db이다보니 백신 데이터 자체가 없어져서 불가로 나옴
- policy handler에서 에러 처리를 위해 신청 가능한 백신이 없음을 고객에게 보여주도록 조치를 해놓았음

- 아래와 같은 이벤트는 명확히 publishing 되어있으므로 잠시 내려가도 신청을 받을 수 있음
{"eventType":"VaccineReserved","timestamp":"20210624130515","reservationId":2,"reservationStatus":"APPLYED","reservationDate":1624233600000,"userName":"tester","userPhone":"010-1234-5679","hospitalId":null}


```


# 운영

## CI/CD 설정

각 구현체들은 각자의 source repository 에 구성되었고, 도커라이징, deploy 및
서비스 생성을 진행하였다.

- 리소스그룹 `skcc-user20-rsrcgrp`
- 레지스트리 `skccuser20`
- 쿠버네티스 `skccuser20-aks`

- Helm 설치 설정
```
curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 > get_helm.sh
chmod 700 get_helm.sh
./get_helm.sh
kubectl --namespace kube-system create sa tiller
kubectl create clusterrolebinding tiller --clusterrole cluster-admin --serviceaccount=kube-system:tiller
```
- 카프카 세팅
```
helm repo add incubator https://charts.helm.sh/incubator 
helm repo update 
kubectl create ns kafka 
helm install my-kafka --namespace kafka incubator/kafka 

or
 
helm repo update
helm repo add bitnami https://charts.bitnami.com/bitnami
kubectl create ns kafka
helm install my-kafka bitnami/kafka --namespace kafka
```

- 소스 세팅
```
mkdir vaccinereservation
cd vaccinereservation
git clone https://github.com/honeion/Vaccine.git
```

- Azure Login
```
az login
az aks get-credentials --resource-group skcc-user20-rsrcgrp --name skcc-user20-aks

# Azure AKS에 ACR Attach 설정

az aks update -n skccuser20-aks -g skcc-user20-rsrcgrp --attach-acr skccuser20
az acr login --name skccuser20
```

- Build 하기
```
cd Vaccine
cd Reservation # Vaccine, Hospital, CustomerCenter, gateway 동일
mvn package
```
- 도커라이징
```
docker build -t skccuser20.azurecr.io/reservation:latest .
docker push skccuser20.azurecr.io/reservation:latest
docker build -t skccuser20.azurecr.io/vaccine:latest .
docker push skccuser20.azurecr.io/vaccine:latest 
docker build -t skccuser20.azurecr.io/hospital:latest .
docker push skccuser20.azurecr.io/hospital:latest 
docker build -t skccuser20.azurecr.io/customercenter:latest .
docker push skccuser20.azurecr.io/customercenter:latest 
docker build -t skccuser20.azurecr.io/gateway:latest .
docker push skccuser20.azurecr.io/gateway:latest 

or

az acr build --registry skccuser20 --image skccuser20.azurecr.io/reservation:latest .
az acr build --registry skccuser20 --image skccuser20.azurecr.io/vaccine:latest .
az acr build --registry skccuser20 --image skccuser20.azurecr.io/hospital:latest .
az acr build --registry skccuser20 --image skccuser20.azurecr.io/customerCenter:latest .
az acr build --registry skccuser20 --image skccuser20.azurecr.io/gateway:latest .
```

- 디플로이 생성
```
kubectl create deploy reservation --image=skccuser20.azurecr.io/reservation:latest
kubectl create deploy vaccine --image=skccuser20.azurecr.io/vaccine:latest
kubectl create deploy hospital --image=skccuser20.azurecr.io/hospital:latest
kubectl create deploy customercenter --image=skccuser20.azurecr.io/customercenter:latest
kubectl create deploy gateway --image=skccuser20.azurecr.io/gateway:latest
```

- 서비스 생성
```
kubectl expose deployment gateway --type=LoadBalancer --port=8080
kubectl expose deployment reservation --port=8080
kubectl expose deployment vaccine --port=8080
kubectl expose deployment hospital --port=8080
kubectl expose deployment customercenter --port=8080
```

- 확인
```
kubectl get all
```
![image](https://user-images.githubusercontent.com/47212652/123219458-e2b60880-d507-11eb-88bc-08c9dfc06abc.png)

## 동기식 호출 / 서킷 브레이킹 / 장애격리

- Spring FeignClient + Hystrix을 사용하여 서킷 브레이킹 구현
- Hystrix 설정 : 결제 요청 쓰레드의 처리 시간이 510ms가 넘어서기 시작한 후 어느정도 지속되면 서킷 브레이커가 닫히도록 설정
- 병원 할당을 요청하는 Vaccine 서비스에서 Hystrix 설정

> Vaccine 서비스의 application.yml 파일
```yaml
feign:
  hystrix:
    enabled: true

hystrix:
  command:
    default:
      execution.isolation.thread.timeoutInMilliseconds: 510
```

- 백신 서비스(Vaccine)및 병원 서비스(Hospital) 컨트롤러에서 임의 부하 처리 
  - 사용 가능한 백신이 ASSIGNED(UPDATE) 되었을 때, 해당 백신이 있는 병원을 찾기위해 요청 수행
  - 요청하는 쪽, 요청받는쪽 양쪽에 부하를 주어 장애 유발
> Vaccine 서비스의 Vaccine.java 파일
```java
    @PostUpdate
    public void onPostUpdate(){
      
        //백신 할당 시 Request 보내고 가서 백신있는 병원 찾고 상태값(할당가능/불가능), 수량, 체크
        if(this.status.equals("ASSIGNED")){
            String hospitalStatus = "";
            String hospitalId = "";
            String vaccineStatus =this.status;
            try {
                Thread.currentThread().sleep((long) (500 + Math.random() * 220));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Map<String,String> res = VaccineApplication.applicationContext
                                                           .getBean(vaccinereservation.external.HospitalService.class)
                                                           .assignHospital(this.getType(),this.getId(),this.getReservationId());

                hospitalStatus = res.get("status")==null?"":res.get("status");
                hospitalId = res.get("hospitalId").equals("-1")?"-1":res.get("hospitalId");
                if(hospitalStatus.equals("EMPTYVACCINE")){
                    //병원에 백신이 없음. 
                    vaccineStatus = "CANTAPPLY";
                }else if(hospitalStatus.equals("ASSIGNED")){
                    //할당이 되었다면 백신에 병원 아이디를 줘야 어디 병원에 몇번 백신이 있는지 관리가 될 것. 
                    vaccineStatus = "ASSIGNED";
                }
                System.out.println("백신상태 : "+vaccineStatus);
            } catch (Exception e) {
                e.printStackTrace();
            }    
        ...
        }
        ...
```
> Hospital 서비스의 HospitalController.java 파일
```java
public Map<String,String> assignHospital(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,String> res = new HashMap<String,String>();
        Long vaccineType = Long.valueOf(request.getParameter("vaccineType"));
        Long vaccineId = Long.valueOf(request.getParameter("vaccineId"));
        Long reservationId = Long.valueOf(request.getParameter("reservationId"));
        String status = "";
        String data = "";
        Long id = -1L;
        try {
            Thread.currentThread().sleep((long) (500 + Math.random() * 220));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Iterable<Hospital> hosOptional = hospitalRepository.findAll();
        ...
```
- 수정 후 재배포
```
az acr build --registry skccuser20 --image skccuser20.azurecr.io/vaccine:latest .
az acr build --registry skccuser20 --image skccuser20.azurecr.io/hospital:latest .
kubectl delete deploy,svc hospital vaccine
kubectl create deploy hospital --image=skccuser20.azurecr.io/hospital:latest
kubectl create deploy vaccine --image=skccuser20.azurecr.io/vaccine:latest
kubectl expose deployment vaccine --port=8080
kubectl expose deployment hospital --port=8080
```

- 부하테스터 siege 툴을 통한 서킷브레이커 동작 확인:
    - 동시사용자 100명
    - 60초 동안 실시
    - siege pod 없으면
      > kubectl run siege --image=apexacme/siege-nginx 
    - siege 접속
      > kubectl exec -it pod/siege-d484db9c-lpvmr -c siege -- /bin/bash
    - siege 종료
      > Ctrl + C -> exit
    - 테스트 코드
    ```
    siege -c100 -t60S -r10 -v --content-type "application/json" 'http://Vaccine:8080/vaccines/1 PATCH {"name":"moderna","type":1,"status":"ASSIGNED"}'
    ```
- 부하가 발생하고 서킷브레이커가 발동하여 요청 실패하였고, 밀린 부하가 다시 처리되면서 백신 수정 및 병원 할당 요청을 받기 시작
![image](https://user-images.githubusercontent.com/47212652/123272188-7bff1200-d53c-11eb-9065-6737ccc9d225.png)


- 운영 중인 시스템은 죽지 않고 지속적으로 서킷브레이커에 의하여 적절히 회로가 열림과 닫힘이 벌어지면서 자원을 보호하고 있음을 보여줌. 하지만, 84.72% 가 성공하였고, 나머지 15.28%의 고객을 위해 Retry 설정과 동적 Scale out (replica의 자동적 추가,HPA) 을 통하여 시스템을 확장 해주는 후속처리가 필요.
![image](https://user-images.githubusercontent.com/47212652/123272240-8ae5c480-d53c-11eb-8ce7-7e60af11216e.png)


## 오토스케일아웃 (HPA)
앞서 서킷브레이커는 시스템을 안정되게 운영할 수 있게 해줬지만 사용자의 요청을 100% 받아들여주지 못했기 때문에 이에 대한 보완책으로 자동화된 확장 기능을 적용하고자 한다. 

- Vaccine/kubernetes/deployment.yaml 파일 설정

![image](https://user-images.githubusercontent.com/47212652/123277286-efa31e00-d540-11eb-81a5-1f98e0a0f60d.png)

- 신청서비스에 대한 replica 를 동적으로 늘려주도록 HPA 를 설정한다. 설정은 CPU 사용량이 15프로를 넘어서면 replica 를 10개까지 늘려준다:

```
# yml파일로 배포
cd kubernetes
kubectl apply -f ./deployment.yml
kubectl expose deployment vaccine --port=8080

kubectl autoscale deploy vaccine --min=1 --max=10 --cpu-percent=15
kubectl get hpa
```

- hpa 설정 확인

![image](https://user-images.githubusercontent.com/47212652/123277586-33962300-d541-11eb-8fb6-5b5259c7e751.png)

- 오토스케일이 어떻게 되고 있는지 모니터링을 걸어둔다:
```
kubectl get deploy vaccine -w
```
![image](https://user-images.githubusercontent.com/47212652/123277630-401a7b80-d541-11eb-9a67-634a1e0161a3.png)

- CB 에서 했던 방식대로 워크로드를 1분 동안 걸어준다.
```
siege -c100 -t60S -r10 -v --content-type "application/json" 'http://Vaccine:8080/vaccines/1 PATCH {"name":"moderna","type":1,"status":"ASSIGNED"}'
```

- 어느정도 시간이 흐른 후 스케일 아웃이 벌어지는 것을 확인할 수 있다:
![image](https://user-images.githubusercontent.com/47212652/123277775-604a3a80-d541-11eb-9bb5-e134cc657547.png)

- 트래픽에 따라 자동으로 스케일 아웃 처리해줌을 확인 할 수 있다.
![image](https://user-images.githubusercontent.com/47212652/123277851-6f30ed00-d541-11eb-8034-daad72830af0.png)

## ConfigMap
- 환경정보로 변경 시 ConfigMap으로 설정함

- 리터럴 값으로부터 ConfigMap 생성
![image](https://user-images.githubusercontent.com/81279673/121073309-4ef8f280-c80d-11eb-998e-d13b361d53e4.png)

- 설정된 ConfigMap 정보 가져오기
![image](https://user-images.githubusercontent.com/81279673/121074021-42c16500-c80e-11eb-8db8-2497dcc099e1.png)
![image](https://user-images.githubusercontent.com/81279673/121073595-a9924e80-c80d-11eb-80e5-88b40effb31b.png)

- 관련된 프로그램(application.yaml, PayService.java) 적용
![image](https://user-images.githubusercontent.com/81279673/121073814-fe35c980-c80d-11eb-980b-5dcc1c6d7019.png)
![image](https://user-images.githubusercontent.com/81279673/121073824-ffff8d00-c80d-11eb-8bda-cc188492d138.png)

## Zero-downtime deploy (Readiness Probe)
- Room 서비스에 kubectl apply -f deployment_non_readiness.yml 을 통해 readiness Probe 옵션을 제거하고 컨테이너 상태 실시간 확인
![non_de](https://user-images.githubusercontent.com/47212652/121105020-32c17980-c83e-11eb-8e10-c27ee89a369d.PNG)

- Room 서비스에 kubectl apply -f deployment.yml 을 통해 readiness Probe 옵션 적용
- readinessProbe 옵션 추가  
    > initialDelaySeconds: 10  
    > timeoutSeconds: 2  
    > periodSeconds: 5  
    > failureThreshold: 10  

- 컨테이너 상태 실시간 확인
![dep](https://user-images.githubusercontent.com/47212652/121105025-33f2a680-c83e-11eb-9db0-ee2206a966fe.PNG)

## Self-healing (Liveness Probe)
- Pay 서비스에 kubectl apply -f deployment.yml 을 통해 liveness Probe 옵션 적용

- liveness probe 옵션을 추가
- initialDelaySeconds: 10
- timeoutSeconds: 2
- periodSeconds: 5
- failureThreshold: 5
                 
  ![스크린샷 2021-06-08 오후 2 16 45](https://user-images.githubusercontent.com/40500484/121127061-2cde8f00-c864-11eb-8b4f-7d3abcba60b3.png)


- Pay 서비스에 liveness가 적용된 것을 확인

- Http Get Pay/live를 통해서 컨테이너 상태 실시간 확인 및 재시동 

  
  ![스크린샷 2021-06-07 오후 9 45 31](https://user-images.githubusercontent.com/40500484/121018788-c9a81a80-c7d9-11eb-9013-1a68ccf1a9b1.png)


- Liveness test를 위해 port : 8090으로 변경
- Delay time 등 옵션도 작게 변경
  
  ![스크린샷 2021-06-08 오후 1 59 29](https://user-images.githubusercontent.com/40500484/121125804-1cc5b000-c862-11eb-8d5d-34b5a0ba1df2.png)

- Liveness 적용된 Pay 서비스 , 응답불가로 인한 restart 동작 확인

  ![스크린샷 2021-06-08 오후 1 59 15](https://user-images.githubusercontent.com/40500484/121125928-50083f00-c862-11eb-91dd-c47a74eade37.png)
