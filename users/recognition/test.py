import os
import numpy as np
import dlib
import pandas as pd


sp = dlib.shape_predictor(
        (os.path.join(os.getcwd(), 'users\\recognition', 'shape_predictor_5_face_landmarks.dat')))
model = dlib.face_recognition_model_v1((os.path.join(os.getcwd(), 'users\\recognition', 'dlib_face_recognition_resnet_model_v1.dat')))


def recognizer(img, vectors, names):
    img1 = dlib.load_rgb_image(img)
    arr = np.array(vectors)
    try:
        height, width, channels = img1.shape
        rec = dlib.rectangle(0, 0, height, width)
        img1_shape = sp(img1, rec)
        # print("shape" + str(img1_shape))
        img1_aligned = dlib.get_face_chip(img1, img1_shape)
        img1_represent = model.compute_face_descriptor(img1_aligned)
        # print("represent" + str(img1_represent))
        img1_represent = np.array(img1_represent)
        location = 0
        min = 1
        for i in range(arr.shape[0]):
            distance = findEuclid(img1_represent, arr[i])
            if distance < min:
                min = distance
                name = names[i]

        thresh_hold = 0.393587
        # print(min)
        if min < thresh_hold:
            return name
        else:
            return "not exist in db"
    except:
        return "cant reg"


def findEuclid(source_represent, test_represent):
    euclid_distance = source_represent - test_represent
    euclid_distance = np.sum(np.multiply(euclid_distance, euclid_distance))
    euclid_distance = np.sqrt(euclid_distance)
    return euclid_distance
