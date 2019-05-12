from django.test import Client, TestCase
from django.urls import reverse
from django.http import JsonResponse
from newsapp.views import * 
import json

class NewsTest(TestCase):

	def __init__(self, *args, **kwargs):
		TestCase.__init__(self, *args, **kwargs)

		self.client = Client()
		self.url = reverse(news_view)

	# checks whether the status code is 200
	def test_response(self):
		resp = self.client.get(self.url)
		self.assertEqual(resp.status_code, 200)

	# checks whether the response is empty
	def test_empty(self):
		resp = self.client.get(self.url)
		self.assertTrue(resp.content)

	# checks whether reponse's type is a list and each member of it is a dictionary
	def test_type(self):
		resp = self.client.get(self.url)
		data = json.loads(resp.content)
		self.assertTrue(type(data[0]) is dict) 
		self.assertTrue(type(data) is list)
