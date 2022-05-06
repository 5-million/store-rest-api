User
---

User는 Cosumer와 VendorEmployee의 공통 데이터로 구성된다.
|      	|  	|
|:---------:	|:-------------:	|
|   email   	| 사용자 이메일 	|
|    name   	|  사용자 이름  	|
|  password 	|    비밀번호   	|
|   phone   	|  핸드폰 번호  	|

email과 phone은 unique value이다.

따로 테이블이 분리되는 것이아니라 `@MappedSuperclass`로 Consumer와 VendorEmployee 테이블에 데이터 들어간다.

따로 클래스를 만드는 이유는 User 클래스를 활용해 Jwt인증을 하기 위함이다.