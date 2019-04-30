from django.contrib import admin
from django.urls import path, include

from .views import twitter_news_view

urlpatterns = [
    path('', twitter_news_view),
]
