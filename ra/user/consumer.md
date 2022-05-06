Consumer
---

Consumer는 User 클래스를 확장하며 다음과 같은 정보를 추가로 포함한다.  
|      	|  	|
|:---------:	|:-------------:	|
| adReceive<sup>*</sup> 	| 광고 수신동의 	|

adReceive<sup>*</sup>는 광고 수신동의 내역이다.
|    |  |
|-----------|-----------------|
| toEmail   | 이메일 수신동의 |
| toMessage | 문자 수신동의   |
| toAppPush | 앱 푸시 동의    |

기능
---
1. 회원가입 (join)
- 입력 데이터: email, name, password, confirmPassword, phone, adReceive<sup>*</sup>
    - adReceive<sup>*</sup>: toEmail, toMessage, toAppPush
- email과 phone은 정해진 형식으로 요청해야한다.
    - Request Body의 유효성 검사를 통해 올바르지 않으면 `400` 응답
- 이미 등록된 email 또는 phone은 `409` 응답.
    - phone의 경우 해당하는 사용자의 이메일을 응답 데이터에 포함한다.
        - 이메일 id부분의 길이 절반은 `*`로 치환한다.
- 등록 성공 시 `201` 응답
- 나중에 등록 후에 이메일 인증을 해야 로그인 가능하도록 하는 기능 추가
- password는 후에 유효성 검사가 필요해질 수 있다.