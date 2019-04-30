
from django.shortcuts import render
from django.http import JsonResponse
import requests
import json
import datetime

# Create your views here.
def currency_rate_view(request,target='TRY'):

    url = "http://data.fixer.io/api/latest?access_key=46c682a11a810c7f4daec5cea86bda17&symbols="+target
    
    response = requests.get(url)
    parsed = response.json()
    
    try:
        dic={}
        dic['datetime']=datetime.datetime.fromtimestamp(int(parsed["timestamp"])).strftime('%Y-%m-%d %H:%M:%S')
        dic['EURto'+str(target)]=parsed["rates"][target]
    except:
        return JsonResponse(parsed)
    
    return JsonResponse(dic)





