VendorManager
---

VendorManager는 User 클래스를 확장하며 추가로 다음과 같은 정보를 포함한다.
| field           | desc                     |
|-----------------|--------------------------|
| id              | 식별자                   |
| vendor          | 소속 공급업체            |
| approved        | 소속 공급업체의 승인여부 |
| approvalManager | 해당 매니저의 승인자     |

id의 column name은 vendor_manager_id이다.
vendor의 column name은 vendor_id이다.

Vendor와 VendorManager의 관계는 `일대다`이다.


기능
---

1. VendorManager 등록
- 입력 데이터 VendorManagerRegisterRequest
    ```json
    {
        "email": "String",
        "name": "String",
        "phone": "String",
        "password": "String",
        "confirmPassword": "String",
        "vendorId": "long"
    }
    ```
- 권한은 기본적으로 `직원(ROLE_VENDOR_STAFF)`을 갖는다. 이후에 `임원(ROLE_VENDOR_EXECUTIVE)`이상의 권한을 갖는 사용자에 의해 더 높은 권한을 부여받을 수 있다.
- 등록시 approved는 기본적으로 false이며 후에 해당 공급업체의 `임원(ROLE_VENDOR_EXECUTIVE)`이상의 권한을 갖는 사용자에 의해 승인이 이루어진다.
- 등록 성공시 VendorManagerInfo와 함께 `201` 응답
    - VendorManagerInfo
    ```json
    {
        "vendorManagerId": "long",
        "email": "String",
        "name": "String",
        "phone": "String",
        "approved": "Boolean",
        "approvalManagerId": "long",
        "role": "String"
    }
    ```
- 중복된 email 또는 phone일 경우 `409` 응답
    - phone일 경우 해당 번호에 가입된 email을 암호화하여 함께 응답
- password와 confirmPassword가 일치하지 않는 경우 `400` 응답
- email과 phone은 유효성 검사

2. VendorManager 전체 조회
- url: `/vendor/manager`로 하며 JWT의 subject claim을 통해 vendor id를 구한다.
- QueryRepository로 DB로부터 DTO로 조회
- Service 계층을 거치지 않고 Controller에서 바로 접근
- 해당 공급업체의 `임원(ROLE_VENDOR_EXECUTIVE)` 이상의 권한을 가진 사용자만 요청 가능
- 응답 데이터
    - VendorManagerInfoList
    ```json
    {
        "vendorManagerList": [
            {...},
            ...
        ]
    }
    ```
    - VendorManagerInfo
    ```json
    {
        "vendorManagerId": "long",
        "email": "String",
        "name": "String",
        "phone": "String",
        "approved": "Boolean",
        "approvalManagerId": "long",
        "role": "String"
    }
    ```

3. VendorManager 승인
- 해당 공급업체의 `임원(ROLE_VENDOR_EXECUTIVE)` 이상의 권한을 가진 사용자만 요청 가능
- `PATCH /vendor/manager/approve/{id}`
- 승인 성공시 `200` 응답
- 해당 공급업체의 직원이 아닐 경우 `403` 응답
