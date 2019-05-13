from django.test import Client, TestCase
from django.urls import reverse
from django.http import JsonResponse

from twitterapp.views import * 
import json

class TwitterTest(TestCase):

	def __init__(self, *args, **kwargs):
		TestCase.__init__(self, *args, **kwargs)

		self.client = Client()
		self.url = reverse(twitter_news_view)

	# checks whether the status code is 200
	def test_response(self):
		resp = self.client.get(self.url)
		self.assertEqual(resp.status_code, 200)

	# checks whether the response is empty
	def test_empty(self):
		resp = self.client.get(self.url)
		self.assertTrue(resp.content)

	# checks whether reponse's type is a dictionaries and contains a list
	def test_type(self):
		resp = self.client.get(self.url)	
		resp_json = json.loads(resp.content)
		self.assertTrue(type(resp_json) is dict)
		self.assertTrue(type(resp_json['all_links']) is list)