import os
import numpy as np
import dlib
import cv2
import pandas as pd


def generate_vector(path):
    sp = dlib.shape_predictor(
        (os.path.join(os.getcwd(), 'users\\recognition', 'shape_predictor_5_face_landmarks.dat')))
    model = dlib.face_recognition_model_v1((os.path.join(os.getcwd(), 'users\\recognition', 'dlib_face_recognition_resnet_model_v1.dat')))
    imagePaths = [os.path.join(path, f) for f in os.listdir(path)]

    names = []

    img2_embeedings = []

    for imagePath in imagePaths:

        name = str(imagePath)

        img2 = dlib.load_rgb_image(name)
        height, width, channels = img2.shape
        rec = dlib.rectangle(0, 0, height, width)
        img2_shape = sp(img2, rec)

        img2_aligned = dlib.get_face_chip(img2, img2_shape)

        img2_represent = model.compute_face_descriptor(img2_aligned)

        img2_embeeding = np.array(img2_represent)

        names.append(name.split("\\")[1].split(".")[0])

        img2_embeedings.append(img2_embeeding)

        cv2.waitKey(10)
        df = pd.DataFrame(img2_embeedings)
    return df
