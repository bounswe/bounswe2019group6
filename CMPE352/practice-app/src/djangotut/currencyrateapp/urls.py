
from django.contrib import admin
from django.urls import path, include

from .views import *

urlpatterns = [
    path('', index_page),
    path('<str:source>to<str:target>', currency_rate_view),
    path('eurto', all_currency_rates),
]
