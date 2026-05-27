# Manager - Huong dan chay va test

## Yeu cau
- JDK 8+ (khuyen nghi 11+)
- macOS/Linux/Windows co the chay duoc Java

## Build toan bo source
Tu thu muc goc du an:

```bash
javac -d out $(find src -name "*.java")
```

Neu dung Windows PowerShell, co the dung:

```powershell
Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName } | javac -d out @-
```

## Chay demo

```bash
java -cp out com.example.manager.Demo
```

## Chay unit test (TestRunner)
Cac bai test dang o `com.example.manager.TestRunner` va khong can thu vien ngoai.

```bash
java -cp out com.example.manager.TestRunner
```

Neu tat ca pass se in: `All tests passed.`

## Ghi chu
- Cac lop DAO co the can ket noi DB. Demo va TestRunner hien tai su dung du lieu mock, khong can DB.
- Neu ban muon viet test moi, co the them phuong thuc test vao `TestRunner` va goi trong `main`.
