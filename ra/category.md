## Category
|        |                |
|--------|----------------|
| id     | 식별자         |
| name   | 카테고리 이름  |
| parent | 상위 카테고리  |
| depth  | 카테고리 depth |

- 최상위 카테고리의 `parent = null`, `depth = 0`
- 같은 depth 내에 카테고리 이름은 유일

## 기능
### 1. 카테고리 등록
- `POST /categories`
- request data: `CategoryRegisterRequest`
    ```json
    {
        "parentCategoryId": "Long",
        "categoryName": "String"
    }
    ```
- 같은 depth 내에 동일한 카테고리 이름이 있다면 `409` 응답
- 등록 성공시 `201` 응답, 헤더에 location 추가 `/categories/{id}`
- resposne data: `CategoryInfo`
    ```json
    {
        "categoryId": "Long",
        "categoryName": "String",
        "depth": "Integer",
        "parentId": "Long"
    }
    ```

### 2. 카테고리 단건 조회
- `GET /categories/{id}`
- id에 해당하는 카테고리가 없을 경우 `404`응답
- response data: `CategoryInfo`
    ```json
    {
        "categoryId": "Long",
        "categoryName": "String",
        "depth": "Integer",
        "parentId": "Long"
    }
    ```

### 3. 카테고리 전체 조회
- `GET /categories`
- 계층형으로 모든 카테고리 응답
- response data: `Categories`
    ```json
    {
        "categories": [
            {
                "categoryId": "Long",
                "categoryName": "String",
                "depth": "Integer",
                "parentId": "Long"
            }, ...
        ]
    }