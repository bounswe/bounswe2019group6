import requests
import json
import datetime
from tabulate import tabulate


""" API FORMAT :
[
    {
        "CalendarId": "184344",
        "Date": "2019-04-29T01:30:00",
        "Country": "Thailand",
        "Category": "Unemployment Rate",
        "Event": "Unemployment Rate",
        "Reference": "Mar",
        "Source": "Bank of Thailand",
        "Actual": "0.9%",
        "Previous": "0.8%",
        "Forecast": "",
        "TEForecast": "0.8%",
        "URL": "/thailand/unemployment-rate",
        "DateSpan": "0",
        "Importance": 2,
        "LastUpdate": "2019-04-29T02:01:00",
        "Revised": "",
        "Currency": "",
        "Unit": "",
        "OCountry": "Thailand",
        "OCategory": "Unemployment Rate",
        "Ticker": "THLMUERT",
        "Symbol": "THLMUERT"
    }]
"""

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
        lst =  [self.name,self.country,self.date,self.importance,self.actual,self.previous,self.forecasat]
        return lst

url = "https://api.tradingeconomics.com/calendar?c=guest:guest"
 
response = requests.get(url)
data = response.text
parsed = json.loads(data)

tabulate_data = []
tabulate_headers = ["Name","Country","Date","Rep","Actual","Previous","Forecast"]
for i in parsed:
    temp = Event(data = i)
    info = temp.get_info()
    tabulate_data.append(info)

print(tabulate(tabulate_data,headers = tabulate_headers))


