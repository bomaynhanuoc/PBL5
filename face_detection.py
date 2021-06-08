import numpy
import cv2
from cv2 import dnn
from picamera import PiCamera
import time
import requests

inWidth = 300
inHeight = 300
confThreshold = 0.5

prototxt = 'deploy.prototxt'
caffemodel = 'res10_300x300_ssd_iter_140000.caffemodel'
def doNothing():
    return 0
if __name__ == '__main__':
    net = dnn.readNetFromCaffe(prototxt, caffemodel)
    with PiCamera() as camera:
        time.sleep(5)
        #camera.rotation = 180
        while True:
            camera.capture("/home/pi/Pictures/img.jpg", use_video_port=True)
            img = cv2.imread("/home/pi/Pictures/img.jpg")
            cols = img.shape[1]
            rows = img.shape[0]
            
            net.setInput(dnn.blobFromImage(img, 1, (inWidth, inHeight), (104.0, 177.0, 123.0), False, False))
            detections = net.forward()
            detected = False
            for i in range(detections.shape[2]):
                conf = detections[0, 0, i, 2]
                if conf > confThreshold:
                    xLeftBottom = int(detections[0, 0, i, 3] * cols)
                    yLeftBottom = int(detections[0, 0, i, 4] * rows)
                    xRightTop = int(detections[0, 0, i, 5] * cols)
                    yRightTop = int(detections[0, 0, i, 6] * rows)
                    if (xRightTop - xLeftBottom < 250 or yRightTop - yLeftBottom < 250):
                        continue
                    #cv2.rectangle(img, (xLeftBottom, yLeftBottom), (xRightTop, yRightTop), (0, 255, 0))
                    img = img[yLeftBottom: yRightTop, xLeftBottom: xRightTop]
                    detected = True
                    break
            if detected == True:
                try:
                    _, img_encoded = cv2.imencode('.jpg', img)
                    requests.post(url = "http://192.168.1.2:8000/users/recognize", files = {"image" : img_encoded.tobytes()})
                    print("Sending image")
                except:
                    doNothing()
            time.sleep(10)
            
        pass

cv2.destroyAllWindows()