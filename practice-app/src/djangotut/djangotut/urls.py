"""djangotut URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/2.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path, include

from eventsapp.views import *
from newsapp.views import *
urlpatterns = [
    path('admin/', admin.site.urls),
    path('currencyrate/',include('currencyrateapp.urls')),
    path('google/', include('googleapi.urls')),
    path('events/', events_view),
  	path('news/',news_view),
    path('twitter/', include('twitterapp.urls')),
    # Create new endpoints here, and redirect them to a function.
    # To do the redirect operation, make the necessary import, and put 
    # your code inside
]
