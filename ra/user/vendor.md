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