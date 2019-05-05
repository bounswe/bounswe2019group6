
from django.shortcuts import render
from django.http import JsonResponse,HttpResponse
import requests
import json
import datetime
from .forms import *

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




