from django.shortcuts import render

from django.views.decorators.csrf import csrf_exempt
from django.core.files.storage import default_storage
from django.core.files.base import ContentFile
from django.conf import settings

from rest_framework.parsers import MultiPartParser, FormParser
from rest_framework.decorators import api_view, parser_classes
from rest_framework.response import Response
from rest_framework import status

from pyrebase.pyrebase import *
from aiproject.firebase_config import database, auth, storage

from users.utils.refresh_token import refresh_token
from users.utils.save_to_db import save_to_db
# from users.recognition.training import generate_vector
from users.recognition.test import recognizer
from firebase_admin import storage as admin_storage

import json
import ast
import datetime
import os
import socket
import requests


@api_view(['GET'])
def index(request):
    print(socket.gethostbyname(socket.gethostname()))
    return Response({'message': 'Connected to the server!!'})


# @api_view(['GET'])
# def upload(request):
#     try:
#         # file_names = [f for f in os.listdir('../Data')]
#         # print(f[0])
#         # get directory path of all file and file names
#         dirpath, _, filenames = next(os.walk('../Data'))

#         # print(dirpath)
#         for f in filenames:
#             # print(f)
#             abs_path = os.path.abspath('{0}/{1}'.format(dirpath, f))

#             # save img into storage
#             res = storage.child(f).put(abs_path)
#             imageUrl = storage.child(f).get_url(res['downloadTokens'])

#             # save data infomation in realtime db
#             data = {
#                 'imageName': f,
#                 'imageUrl': imageUrl,
#                 'name': f.split('.')[0]
#             }
#             database.child("IDs").push(data)

#         return Response("ok")
#     except Exception as e:
#         print(e)
#         return Response(e)

# get all the user from the database


@api_view(['GET'])
def get_user(request):
    try:
        if auth.current_user == None:
            raise TypeError(auth.current_user)
        # only admin can see this page
        # print(auth.current_user['localId'])
        else:
            user = auth.current_user
            # auto refresh_token
            # refresh_token(user)

            IDs = database.child('IDs').get(
                token=user['idToken']).val()
    except HTTPError as e:
        # print(e)
        error_json = e.args[1]
        error = json.loads(error_json)  # convert to Python dict
        return Response(error['error'])
    except TypeError as t:
        print(t.args)
        return Response({'message': 'Unauthorized'}, status.HTTP_403_FORBIDDEN)
    # except:
    #     return Response("Something wrong")

    return Response(IDs)


@api_view(['GET'])
def get_result(request):
    try:
        result = database.child('Result').get().val()
        database.child('Result').remove()
        return Response({'data': result})
    except Exception as e:
        return Response('something wrong')


# @csrf_exempt  # pass the csrf problem
@api_view(['POST'])
@parser_classes([MultiPartParser, FormParser])
def recognize(request):
    try:
        # flow:
        # get all images from db, regconize with regconize module
        # + if regconized, response the text to the app.
        # + if don't, temporarily save the image into tmp folder, response the image file to the app.
        user_agent = request.META['HTTP_USER_AGENT']

        # print(request.data['image'])
        # for f in request.FILES.getlist('image'):
        #     print(f)

        # face recognition here (get data from firebase and recognition)
        # if true, return the name in the db (send data to open the electric lock), else return the image
        if user_agent.find('python-requests') >= 0:
            file = request.FILES['image']
            print(file)

            # create file from request
            content_file = ContentFile(file.read())
            path = default_storage.save(
                'tmp/{0}.jpg'.format(file.name), content_file)
            tmp_file = os.path.join(settings.MEDIA_ROOT, path)
            # get data from db
            all_users = database.child('IDs').get()
            names = []
            vectors = []

            for user in all_users.each():
                names.append(user.val()['name'])
                vectors.append(user.val()['vector'])

            # recognize
            result = recognizer(tmp_file, vectors, names)
            print(result)

            database.child('Result').set(result)
            # result is in db, open the door
            if result in names:
                r = requests.get("http://192.168.1.2:8000/users/open-door")
                return Response(json.loads(r.text))

            # imgs_file = [f for f in request.FILES.getlist('image')]
            # print(imgs_file)

            # check if user has signed in
            # if auth.current_user == None:
            #     raise TypeError(auth.current_user)
            # else:
            #     user = auth.current_user
            #     # if regconize is True
            #     r = requests.get("http://192.168.1.8:8000/users/open-door")
            #     return Response(json.loads(r.text))
                # else response to the phone

                # print(os.path.isfile('tmp/{0}'.format(file.name)))

                # auto refresh token
        #         refresh_token(user)

        #         if os.path.isfile('tmp/{0}'.format(file.name)) == False:
        #             # if True:
        #             # create file in folder tmp
        #             content_file = ContentFile(file.read())
        #             path = default_storage.save(
        #                 'tmp/{0}'.format(file.name), content_file)
        #             tmp_file = os.path.join(settings.MEDIA_ROOT, path)

        #             # find the path to file
        #             # print(os.path.abspath(tmp_file))
        #             abs_file_dir = os.path.abspath(tmp_file)

        #             # save image to storage
        #             res = storage.child(file.name).put(
        #                 abs_file_dir, token=user['idToken'])

        #             # delete the file
        #             os.remove(abs_file_dir)
        #         else:
        #             return Response({'message': 'file already exist'}, status=status.HTTP_400_BAD_REQUEST)
        return Response('done recognize')
    except TypeError:
        # print(t.args)
        return Response({'message': 'Unauthorized'}, status.HTTP_403_FORBIDDEN)
    except Exception as e:
        return Response(str(e.args), status=status.HTTP_400_BAD_REQUEST)
    except Exception:
        return Response('something wrong')

    # # get the image url
    # return Response({'imageUrl': storage.child(file.name).get_url(token=res['downloadTokens']), 'imageName': file.name})


@api_view(['POST'])
@parser_classes([MultiPartParser, FormParser])
def create_user(request):
    try:
        if auth.current_user == None:
            raise TypeError(auth.current_user)
        else:
            # request will have: name of the user
            data = request.body
            # convert byte to dictionary
            dict_str = data.decode('UTF-8')
            myData = ast.literal_eval(dict_str)
            strange_face_name = myData['name']

            # image already have in tmp folder, now just change the file name -> save to images local -> train -> update image to storage -> update realtime db -> return true

            # change file name to the name that coming from request.body
            new_file_name = "{0}.jpg".format(strange_face_name)
            old_file = os.path.join('tmp', "image.jpg")
            new_file = os.path.join('tmp', new_file_name)
            moved_dst = os.path.join('Data', new_file_name)

            if os.path.exists(old_file):
                os.rename(old_file, new_file)
                # move the file
                if os.path.exists(new_file):
                    os.rename(new_file, moved_dst)

                # train and save to db
                save_to_db('create', new_file_name)
                return Response({'message': 'user created'}, status.HTTP_201_CREATED)
            else:
                return Response({'message': "don't have file in tmp or have file with other name"}, status.HTTP_501_NOT_IMPLEMENTED)

            # file = request.FILES['image']
            # data = request.data['data']
            # print(file)
            # print(ast.literal_eval(data)['name'])
            # print(image_name)
            # name = ast.literal_eval(data)['name']
            # image_name = file.name.split('.')[0]

            # create file from request
            # content_file = ContentFile(file.read())
            # path = default_storage.save(
            #     'Data/{0}.jpg'.format(name), content_file)
            # tmp_file = os.path.join(settings.MEDIA_ROOT, path)

            # flow:
            # take the name, the image file (in tmp folder), replace the file name with the name, then save to db.
    except TypeError as t:
        return Response({'message': 'Unauthorized'}, status.HTTP_403_FORBIDDEN)
    except Exception as e:
        print(e)
        return Response(str(e.args), status=status.HTTP_400_BAD_REQUEST)


@api_view(['DELETE'])
def delete_user(request):
    try:
        # check if user has signed in
        if auth.current_user == None:
            raise TypeError(auth.current_user)
        else:
            data = request.body
            dict_str = data.decode('UTF-8')
            myData = ast.literal_eval(dict_str)
            userId = myData['id']
            found_user = database.child(
                'IDs').child(userId).get().val()
            file_name = found_user['imageName']

            # delete user
            database.child('IDs').child(userId).remove()

            # delete image according to username
            bucket = admin_storage.bucket()
            blob = bucket.blob(file_name)
            # print(blob)
            blob.delete()

            # delete image in local images folder (data for training)
            # way 1:
            # abs_path = os.path.abspath(
            #     os.getcwd()) + '\\Data\\{0}'.format(file_name)
            # way 2 (faster than way 1):
            abs_path = os.path.join(os.path.abspath(
                os.getcwd()), 'Data', file_name)
            # print(abs_path)
            os.remove(abs_path)

            # training model again and update to db
            save_to_db()

            return Response({'message': 'Deleted successfully'})
    except TypeError as t:
        err_mes = str(t.args[0])

        # print(err_mes)
        if err_mes.find("object") > 0:
            return Response({'message': 'Does not have user that matched this id'}, status.HTTP_404_NOT_FOUND)
        return Response({'message': 'Unauthorized'}, status.HTTP_403_FORBIDDEN)


# @csrf_exempt
@api_view(['POST'])
def sign_in(request):
    try:
        # print(request.body)
        # data in bytestring
        data = request.body
        # convert byte to dictionary
        dict_str = data.decode('UTF-8')
        myData = ast.literal_eval(dict_str)
        # print(myData)
        # sign in with firebase authentication
        user = auth.sign_in_with_email_and_password(**myData)

        # auth.current_user['idTokenCreatedAt'] = datetime.datetime.now()
        # print(auth.current_user)
        print(request.META['HTTP_USER_AGENT'])

        return Response(auth.current_user)
    # invalid credential (wrong format email. wrong password)
    except HTTPError as e:
        error_json = e.args[1]
        error = json.loads(error_json)  # convert to Python dict
        # print(error['error'])
        return Response({'message': error['error']['message']}, status=error['error']['code'])


@api_view(['GET'])
def open_door(request):
    user_agent = request.META['HTTP_USER_AGENT']

    print(user_agent)
    # user has signed in
    if auth.current_user != None:
        user = auth.current_user

        # if Java app or server is send request
        if user_agent.find('Postman') >= 0 or user_agent.find('python-requests') >= 0 or user_agent.find('okhttp') >= 0:
            user['is_open'] = True
            return Response({'message': 'door opened'})
        # esp 8266 send request
        if user_agent.find('ESP8266') >= 0 and 'is_open' in user:
            if user['is_open'] == True:
                user['is_open'] = False
                return Response({'isOpen': 1})
        # wait for request
        return Response({'message': 'waiting for response'})
    else:
        return Response({'message': 'Unauthorized'}, status.HTTP_403_FORBIDDEN)
