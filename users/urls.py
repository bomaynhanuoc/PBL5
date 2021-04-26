from django.urls import path

from . import views

urlpatterns = [
    path('', views.index, name='index'),
    path('get-user', views.get_user, name='get_user'),
    path('create', views.create_user, name='create_user'),
    path('delete', views.delete_user, name='delete_user'),
    path('upload-image', views.upload_image, name='upload_image'),
    path('sign-in', views.sign_in, name='sign_in'),
    path('sign-out', views.sign_out, name='sign_out')
]
