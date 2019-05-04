from django.test import Client, TestCase
from django.urls import reverse
from django.http import JsonResponse
from eventsapp.views import * 
import json

class EventsTest(TestCase):

	def __init__(self, *args, **kwargs):
		TestCase.__init__(self, *args, **kwargs)

		self.client = Client()
		self.url = reverse(events_view)

	# checks whether the status code is 200
	def test_response(self):
		resp = self.client.get(self.url)
		self.assertEqual(resp.status_code, 200)

	# checks whether the response is empty
	def test_empty(self):
		resp = self.client.get(self.url)
		self.assertTrue(resp.content)

	# checks whether reponse's type is a list of dictionaries
	def test_type(self):
		resp = self.client.get(self.url)	
		resp_list = json.loads(resp.content)
		self.assertTrue(type(resp_list) is list)
		self.assertTrue(type(resp_list[0]) is dict)