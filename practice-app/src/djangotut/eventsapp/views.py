
from django.shortcuts import render
from django.http import JsonResponse

import json
import requests


class Event:
    def __init__(self, data):
        self.id = data['CalendarId']
        self.date = data['Date'][:10]
        self.country = data['Country']
        self.name = data['Event']
        self.source = data['Source']
        self.actual = data['Actual']
        self.previous = data['Previous']
        self.forecasat = data['TEForecast']
        self.importance = data['Importance']
        self.currency = data['Currency']
    def get_info(self):
        lst =  {"country":self.country,"date":self.date,"rep":self.importance,"actual":self.actual,"prev":self.previous,"forecast":self.forecasat}
        return lst

# Create your views here.
def events_view(request):
    
    url = "https://api.tradingeconomics.com/calendar?c=guest:guest"
     
    response = requests.get(url)
    data = response.text
    parsed = json.loads(data)

    events_data = {}
    
    for i in parsed:
        temp = Event(data = i)
        info = temp.get_info()
        events_data[temp.name] = info

    #Using JSON format
    return JsonResponse(events_data)

    #Using RENDER format
    """context = {
        'object_set' : tabulate_data
    }
    return render(request,'event_details.html',context)"""