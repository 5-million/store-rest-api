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