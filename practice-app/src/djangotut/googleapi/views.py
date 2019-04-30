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
        'redirect_uri': 'https://pinoynanny.com',
        'client_id': '399765628337-9vk6citn3kvbf2jiebn4n620k63um5ib.apps.googleusercontent.com',
        'client_secret': 'LQt2bwkHmjmZQKDoVAVPhCsx',
        'scope': '',
        'grant_type': 'authorization_code'
    }

    data = urlencode(params).encode('ascii')
    print(data)

    req = urllibrequest.Request(url, data=data)

    body = ""

    try:
        with urllibrequest.urlopen(req) as r:
            body = r.read()
    except HTTPError as e:
        print(e)
        return HttpResponse(e)

    return HttpResponse(body)