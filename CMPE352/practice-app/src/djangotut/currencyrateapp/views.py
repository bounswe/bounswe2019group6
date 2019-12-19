
from django.shortcuts import render
from django.http import JsonResponse,HttpResponse
import requests
import json
import datetime
from .forms import *

# Create your views here.
def currency_rate_view(request,source='EUR',target='TRY'):

    url = "http://data.fixer.io/api/latest?access_key=46c682a11a810c7f4daec5cea86bda17&symbols="+target
    url2= "http://data.fixer.io/api/latest?access_key=46c682a11a810c7f4daec5cea86bda17&symbols="+source
    response = requests.get(url)
    parsed = response.json()
    response2=requests.get(url2)
    parsed2=response2.json()
    try:
        dic={}
        dic['datetime']=datetime.datetime.fromtimestamp(int(parsed["timestamp"])).strftime('%Y-%m-%d %H:%M:%S')
        dic[str(source)+' to '+str(target)]=parsed["rates"][target]/parsed2["rates"][source]
    except:
        if parsed['success'] == False:
            return JsonResponse(parsed)
        else:
            return JsonResponse(parsed2)
    
    return JsonResponse(dic)

def index_page(request):
    context={}
    form = SearchForm()
    context['form']=form
    if request.method=='POST':
        my_form = SearchForm(request.POST)
        if my_form.is_valid():
            return currency_rate_view(request,**my_form.cleaned_data)
        else:
            JsonResponse(my_form.errors)
    
    return render(request,'currency_index.html',context)

def all_currency_rates(request):
    url="http://data.fixer.io/api/latest?access_key=46c682a11a810c7f4daec5cea86bda17"
    response=requests.get(url)
    parsed=response.json()

    return JsonResponse(parsed)

def all_symbols():
    url = "http://data.fixer.io/api/symbols?access_key=46c682a11a810c7f4daec5cea86bda17"
    response = requests.get(url)

    parsed = response.json()
    return JsonResponse(parsed)