from django.shortcuts import render
from django.http import JsonResponse

import json
import requests

class News:
	def __init__(self,data):
		self.id=data['id']
		self.title=data['title']
		self.date=data['date'][:10]
		self.description=data['description']
		self.country=data['country']
		self.category=data['category']
		self.symbol=data['symbol']
		self.url=data['url']
	def get_info(self):
		list={"id":self.id,
			"title":self.title,
			"description":self.description,
			"date":self.date,
			"country":self.country,
			"symbol":self.symbol,
			"url":self.url}
		return list

#Creating the views
def news_view(request):

	url="https://api.tradingeconomics.com/news?c=guest:guest"


	response = requests.get(url)
	data= response.text
	parsed = json.loads(data)

	news_data = {}

	for i in parsed:
		temp = News(data = i)
		info = temp.get_info()
		news_data[temp.id] = info

	#Using JSON format
	return JsonResponse(news_data)
	
	#Using RENDER format
	"""context = {
		'object_set' : news_data
	}
	return render(request,'news_details.html',context)"""
	

