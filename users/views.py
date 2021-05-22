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
from aiproject.firebase_config import database, auth, storage, STORAGE_BUCKET

from users.utils.refresh_token import refresh_token
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
            refresh_token(user)

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
    # print(IDs)
    # convert result to json


# @csrf_exempt  # pass the csrf problem
@api_view(['POST'])
@parser_classes([MultiPartParser, FormParser])
def recognize(request):
    try:
        # flow:
        # get all images from db, regconize with regconize module
        # + if regconized, response the text to the app.
        # + if don't, temporarily save the image into tmp folder, response the image file to the app.

        # print(request.data['image'])
        # for f in request.FILES.getlist('image'):
        #     print(f)
        # file = request.FILES['image']
        imgs_file = [f for f in request.FILES.getlist('image')]
        # print(imgs_file)
        # face recognition here (get data from firebase and recognition)
        # if true, return the name in the db (send data to open the electric lock), else return the image

        # check if user has signed in
        if auth.current_user == None:
            raise TypeError(auth.current_user)
        else:
            user = auth.current_user
            # if regconize is True
            r = requests.get("http://192.168.1.2:8000/users/open-door")
            return Response(json.loads(r.text))
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
    # request will have: name of the user, image url(for phone), image name
    # flow:
    # take the name, the image file (in tmp folder), replace the file name with the name, then save to db.
    return Response('create user')


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

            # delete user
            database.child('IDs').child(userId).remove()

            # request will have: id of the deleted user
            return Response({'message': 'Deleted successfully'})
    except TypeError:
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

        auth.current_user['idTokenCreatedAt'] = datetime.datetime.now()
        # print(auth.current_user)

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
        if user_agent.find('Postman') >= 0 or user_agent.find('python-requests') >= 0:
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
