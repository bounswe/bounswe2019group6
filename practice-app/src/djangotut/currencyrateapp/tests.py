from django.test import Client, TestCase
from django.urls import reverse
from django.http import JsonResponse
from currencyrateapp.views import * 
from .forms import *
import json

class CurrencyTests(TestCase):

    def __init__(self, *args, **kwargs):
        TestCase.__init__(self, *args, **kwargs)

        self.client = Client()
        self.url = reverse(index_page)

    # checks whether the status code is 200
    def test_response(self):
        resp = self.client.get(self.url)
        self.assertEqual(resp.status_code, 200)

    # checks whether the response is empty
    def test_empty(self):
        resp = self.client.get(self.url)
        self.assertTrue(resp.content)


    # checks whether reponse's type is a dict
    def test_type(self):
        sample_test = "eurtotry"
        resp = self.client.get(self.url+sample_test)    
        resp_list = json.loads(resp.content)
        self.assertTrue(type(resp_list) is dict)

    def test_valid_symbols(self):
        sample_test = "eurtousd"
        resp = self.client.get(self.url+sample_test)    
        data = json.loads(resp.content)
        self.assertEqual(data['success'], True)

    def test_invalid_symbols(self):
        sample_tests = ["ABCtoTRY", "USDtoABC", "ABCtoDEF"]
        for test in sample_tests:
            resp =  self.client.get(self.url+test)
            data = json.loads(resp.content)
            self.assertEqual(data['success'], False)