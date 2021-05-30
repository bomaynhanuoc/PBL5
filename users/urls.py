from django.urls import path
from aiproject.firebase_config import database
from users.utils.save_to_db import save_to_db

from . import views

urlpatterns = [
    path('', views.index, name='index'),
    # path('upload', views.upload, name='upload'),
    path('get-user', views.get_user, name='get_user'),
    path('create', views.create_user, name='create_user'),
    path('delete', views.delete_user, name='delete_user'),
    path('get-result', views.get_result, name='get-result'),
    path('recognize', views.recognize, name='recognize'),
    path('sign-in', views.sign_in, name='sign_in'),
    path('open-door', views.open_door, name='open_door')
]

# save_to_db()
database.child('Result').remove()
