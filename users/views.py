from django.core.files.storage import default_storage
from django.core.files.base import ContentFile
from django.conf import settings

from rest_framework.parsers import MultiPartParser, FormParser
from rest_framework.decorators import api_view, parser_classes
from rest_framework.response import Response
from rest_framework import status

from pyrebase.pyrebase import *
from aiproject.firebase_config import database, auth, storage

from users.utils.db_interact import save_to_db
from users.recognition.test import recognizer
from firebase_admin import storage as admin_storage

import json
import ast
import os
import socket
import requests


@api_view(['GET'])
def index(request):
    print(socket.gethostbyname(socket.gethostname()))
    userIds = []
    return Response({'message': 'Connected to the server!!'})


@api_view(['GET'])
def get_user(request):
    try:
        if auth.current_user == None:
            raise TypeError(auth.current_user)
        else:
            user = auth.current_user
            users = []

            IDs = database.child('IDs').get(
                token=user['idToken'])

            for user in IDs.each():
                data = {
                    'id': user.key(),
                    'name': user.val()['name'],
                    'imageUrl': user.val()['imageUrl']
                }
                users.append(data)

    except HTTPError as e:
        # print(e)
        error_json = e.args[1]
        error = json.loads(error_json)
        return Response(error['error'])
    except TypeError as t:
        print(t.args)
        return Response({'message': 'Unauthorized'}, status.HTTP_403_FORBIDDEN)

    return Response(users)


@api_view(['GET'])
def get_result(request):
    try:
        result = database.child('Result').get().val()
        if result is None:
            data = {
                'name': "don't have data yet",
                'imageUrl': 'null'
            }
            return Response(data)
        else:
            database.child('Result').remove()
            return Response(result)
    except Exception as e:
        return Response('something wrong')


@api_view(['POST'])
@parser_classes([MultiPartParser, FormParser])
def recognize(request):
    try:
        user_agent = request.META['HTTP_USER_AGENT']

        if user_agent.find('python-requests') >= 0:
            file = request.FILES['image']
            print(file)

            content_file = ContentFile(file.read())
            path = default_storage.save(
                'tmp/{0}.jpg'.format(file.name), content_file)
            tmp_file = os.path.join(settings.MEDIA_ROOT, path)

            abs_file_dir = os.path.abspath(tmp_file)

            all_users = database.child('IDs').get()
            userIds = []
            names = []
            vectors = []

            for user in all_users.each():
                userIds.append(user.key())
                names.append(user.val()['name'])
                vectors.append(user.val()['vector'])

            result = recognizer(tmp_file, vectors, names)
            print(result)
            data = {}

            if result in names:
                res = storage.child(file.name).put(abs_file_dir)
                image_of_found_user = storage.child(
                    file.name).get_url(token=res['downloadTokens'])
                data = {
                    "name": result,
                    "imageUrl": image_of_found_user
                }
                database.child('Result').set(data)

                os.remove(abs_file_dir)

                time.sleep(5)

                r = requests.get("http://192.168.1.2:8000/users/open-door")
                return Response(json.loads(r.text))
            else:

                res = storage.child(file.name).put(abs_file_dir)
                image_of_strange_user = storage.child(
                    file.name).get_url(token=res['downloadTokens'])
                data = {
                    "name": result,
                    "imageUrl": image_of_strange_user
                }
                database.child('Result').set(data)

                os.remove(abs_file_dir)
                return Response(data)
        return Response('done recognize')
    except TypeError:
        return Response({'message': 'Unauthorized'}, status.HTTP_403_FORBIDDEN)
    except Exception as e:
        return Response(str(e.args), status=status.HTTP_400_BAD_REQUEST)
    except Exception:
        return Response('something wrong')


@api_view(['POST'])
@parser_classes([MultiPartParser, FormParser])
def create_user(request):
    try:
        if auth.current_user == None:
            raise TypeError(auth.current_user)
        else:
            data = request.body
            dict_str = data.decode('UTF-8')
            myData = ast.literal_eval(dict_str)
            strange_face_name = myData['name']
            image_url = myData['imageUrl']

            new_file = "{0}.jpg".format(strange_face_name)
            storage.child('image').download('', new_file)

            moved_dst = os.path.join('Data', new_file)
            os.rename(new_file, moved_dst)

            save_to_db('create', new_file)

            return Response({'message': 'user created'}, status.HTTP_200_OK)
    except TypeError as t:
        return Response({'message': 'Unauthorized'}, status.HTTP_403_FORBIDDEN)
    except Exception as e:
        # print(e)
        return Response({'message': 'user created'}, status.HTTP_200_OK)


@api_view(['POST'])
def delete_user(request):
    try:
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

            database.child('IDs').child(userId).remove()

            bucket = admin_storage.bucket()
            blob = bucket.blob(file_name)
            blob.delete()

            abs_path = os.path.join(os.path.abspath(
                os.getcwd()), 'Data', file_name)
            os.remove(abs_path)

            # save_to_db()

            return Response({'message': 'Deleted successfully'})
    except TypeError as t:
        err_mes = str(t.args[0])

        if err_mes.find("object") > 0:
            return Response({'message': 'Does not have user that matched this id'}, status.HTTP_404_NOT_FOUND)
        return Response({'message': 'Unauthorized'}, status.HTTP_403_FORBIDDEN)


@api_view(['POST'])
def sign_in(request):
    try:
        data = request.body
        dict_str = data.decode('UTF-8')
        myData = ast.literal_eval(dict_str)
        user = auth.sign_in_with_email_and_password(**myData)

        print(request.META['HTTP_USER_AGENT'])

        return Response(auth.current_user)
    except HTTPError as e:
        error_json = e.args[1]
        error = json.loads(error_json)
        return Response({'message': error['error']['message']}, status=error['error']['code'])


@api_view(['GET'])
def open_door(request):
    user_agent = request.META['HTTP_USER_AGENT']

    print(user_agent)
    if auth.current_user != None:
        user = auth.current_user

        if user_agent.find('python-requests') >= 0 or user_agent.find('okhttp') >= 0:
            user['is_open'] = True
            return Response({'message': 'door opened'})
        if user_agent.find('ESP8266') >= 0 and 'is_open' in user:
            if user['is_open'] == True:
                user['is_open'] = False
                return Response({'isOpen': 1})
        return Response({'message': 'waiting for response'})
    else:
        return Response({'message': 'Unauthorized'}, status.HTTP_403_FORBIDDEN)
