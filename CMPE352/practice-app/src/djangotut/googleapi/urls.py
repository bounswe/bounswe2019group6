from django.urls import path

from . import views

urlpatterns = [
    path('map/place', views.getGooglePlace, name="getGooglePlace"),
    path('map/geocode', views.getGoogleGecode, name="getGoogleGecode"),
    path('oauth/exchange', views.getAccessToken, name="getAccessToken")
]