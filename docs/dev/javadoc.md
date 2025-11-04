# 📄 Viết Javadoc

### 💾 Format

Javadoc được viết tuân theo các quy tắc javadoc cơ bản và viết trên format HTML.

### 🏗️ Cấu trúc

Trên IDE, khi bạn di chuyển lên trên đầu lớp/method và viết `/**` rồi Enter, IDE sẽ tự sinh ra cho bạn khung để bạn viết
javadoc. Cấu trúc của dự án chúng ta sẽ là:

- Đối với Lớp:

```java
/**
 *
 *
 * <h1>?{@link Tên Lớp}</h1>
 *
 * Mô tả lớp... Có thể sử dụng {@link Class nào đó#TP trong class} để liên kết <br>
 * <b>Chú ý: Sử dụng thẻ br để xuống dòng</b> <br>
 * <i>Plot Twist: tui cũng ko bt viết gì ở đây nữa</i>
 *
 * @see ...
 */

```

trong đó `?` ở Tên lớp sẽ là:

| Loại lớp | 📚 Class | 📱Interface | 🔢 Enum | ❗ Exception | 📍 Annotation | 📝 Record |
|----------|----------|-------------|---------|-------------|---------------|-----------|
| Kí hiệu  |          | %           | #       | !           | @             | $         |

**VD:** `ThisIsClass`, `%Interface`, `!StackoverflowException`, `#EntityType`

- Đối với method:

```java
/**
 * Hàm này thực hiện chức năng gì... .
 *
 * @params Input1 Đầu vào 1
 * @params Input2 Đầu vào 2
 * @return Kết quả của hàm
 * @throws Exception nếu có lỗi xảy ra...
 */

```

### ❗Chú ý

- Formating docs sau khi viết code xong. (Phím tắt thường là `Alt` + `Shift` + `F`)

### Bạn có thể xem source code trong dự án để xem ví dụ.