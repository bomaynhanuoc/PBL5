### Tải mã nguồn
```shell git clone https://github.com/bomaynhanuoc/PBL5.git```
### Server - Backend:
Checkout sang branch API:
```shell git checkout API```
Cài đặt các thư viện cần thiết:
```shell pip install -r requirements.txt```
Chạy server:
```shell python manage.py runserver```
## Lưu ý:
Trong file urls ở thư mục users, bỏ comment ở dòng 18 rồi chạy server 1 lần, mục đích là để train dữ liệu và cập nhật vào cơ sở dữ liệu, sau đó comment dòng này lại.
### Client - Mobile:
Checkout sang branch Android-app:
```shell git checkout Android-App```
Tải file app-debug.apk về điện thoại và cài đặt.
### Hardware:
Checkout sang branch HARDWARE:
```shell git checkout HARDWARE```
Cài đặt các thư viện:
```shell pip install opencv-python requests```
