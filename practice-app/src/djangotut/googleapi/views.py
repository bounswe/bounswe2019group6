from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from urllib import request as urllibrequest
from urllib.parse import urlencode
from urllib.error import HTTPError

import environ

env = environ.Env(DEBUG=(bool, False))

# Create your views here.

def getGooglePlace(request):
    url = 'https://maps.googleapis.com/maps/api/place/autocomplete/json'

    params = {
        'key': env('API_KEY'), 
        'input': request.GET.get('input', ''), 
        'inputtype': 'textquery', 
        'types': 'geocode'
    }

    req = urllibrequest.Request(url + '?' + urlencode(params))

    body = ""

    with urllibrequest.urlopen(req) as r:
        body = r.read()
    
    return HttpResponse(body)

def getGoogleGecode(request):
    url = 'https://maps.googleapis.com/maps/api/geocode/json'

    params = {
        'key': env('API_KEY'), 
        'place_id': request.GET.get('input', '')
    }

    req = urllibrequest.Request(url + '?' + urlencode(params))

    body = ""

    with urllibrequest.urlopen(req) as r:
        body = r.read()

    return HttpResponse(body)

def getAccessToken(request):

    url = 'https://www.googleapis.com/oauth2/v4/token'

    params = {
        'code': request.GET.get('code', ''),
        'redirect_uri': 'OUR_NEXT_DOMAIN',
        'client_id': env('CLIENT_ID'),
        'client_secret': env('CLIENT_SECRET'),
        'scope': '',
        'grant_type': 'authorization_code'
    }

    data = urlencode(params).encode('ascii')

    req = urllibrequest.Request(url, data=data)

    body = ""

     with urllibrequest.urlopen(req) as r:
        body = r.read()

    return HttpResponse(body)