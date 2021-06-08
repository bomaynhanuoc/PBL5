### Tải mã nguồn
* ```shell git clone https://github.com/bomaynhanuoc/PBL5.git```
### Server - Backend:
* Checkout sang branch API:
```shell git checkout API```
* Cài đặt các thư viện cần thiết:
```shell pip install -r requirements.txt```
* Chạy server:
```shell python manage.py runserver```
## Lưu ý:
* Trong file urls ở thư mục users, bỏ comment ở dòng 18 rồi chạy server 1 lần, mục đích là để train dữ liệu và cập nhật vào cơ sở dữ liệu, sau đó comment dòng này lại.
### Client - Mobile:
* Checkout sang branch Android-app:
```shell git checkout Android-App```
* Tải file app-debug.apk về điện thoại và cài đặt.
### Hardware:
* Checkout sang branch HARDWARE:
```shell git checkout HARDWARE```
* Cài đặt các thư viện để chạy raspberry:
```shell pip install opencv-python requests```
* Đưa 3 file face_detection.py, deploy.prototxt, res10_300x300_ssd_iter_140000.caffemodel vào cùng một thư mục trên raspberry và chạy ```shell python face_detection.py```.
* Cài đặt các thư viện để chạy ESP8266:
* Trong cửa sổ Arduino IDE, chọn File > Preferences, thêm dòng này vào ô Additional Boards Manager URLs: http://arduino.esp8266.com/stable/package_esp8266com_index.json.
* Vào mục Tools > Manage Libraries..., tìm thư viện Arduino_JSON và cài đặt.
