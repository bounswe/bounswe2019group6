
from django.contrib import admin
from django.urls import path, include

from .views import *

urlpatterns = [
    path('', index_page),
    path('<str:target>', currency_rate_view),
]
