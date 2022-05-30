Vendor
---

Vendor의 데이터는 다음과 같다.  
| field     | desc            |
|-----------|-----------------|
| id        | 식별자          |
| name      | 공급업체 이름(상호명)   |
| regNumber | 사업자 등록번호 |
| ceo       | 대표            |
| location<sup>*</sup>  | 소재지          |

공급업체 이름(name)과 사업자 등록번호(regNumber)는 유일한 값이다.

location<sup>*</sup>는 Address 객체이며 Address는 다음과 같다.
| field   | desc      |
|---------|-----------|
| zipcode | 우편번호  |
| base    | 기본 주소 |
| detail  | 상세 주소 |

기능
---
1. 공급업체 등록
- 등록시 임원 권한을 갖는 한 명의 VendorManager와 함께 등록해야한다.
- 입력 데이터: VendorRegisterRequest
    ```json
    {
        "name": "String",
        "regNumber": "String", //사업자 등록번호
        "ceo": "String",
        "location": {
            "zipcode": "String", //우편번호
            "base": "String", //기본 주소
            "detail": "String" //상세 주소
        },
        "executive": { //임원 계정
            "email": "String",
            "name": "String",
            "phone": "String",
            "password": "String",
            "confirmPassword": "String",
        }
    }
    ```
- 등록 성공시 VendorInfo와 함께 `201` 응답
    - VendorInfo
    ```json
    {
        "id": "공급업체 식별값",
        "name": "공급업체 이름",
        "regNumber": "공급업체 사업자 등록번호",
        "ceo": "공급업체 대표",
        "location": {
            "zipcode": "우편번호",
            "base": "기본 주소",
            "detail": "상세 주소"
        }
    }
    ```
- 이미 등록된 공급업체 이름이거나 사업자 등록번호일 경우 `409` 응답
- Email과 Phone에 대해 유효성 검사
- password와 confirmPassword가 일치하지 않으면 `400` 응답
- 이미 등록된 계정의 이메일 또는 번호인 경우 `409` 응답
    - 번호인 경우에는 등록된 이메일을 암호화하여 응답