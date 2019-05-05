from django.test import TestCase
from django.urls import reverse
from urllib.error import HTTPError
import json

import environ

env = environ.Env(DEBUG=(bool, False))

class GoogleMapTest(TestCase):

    def test_google_map_api_key_exists(self):
        api = env('API_KEY', default='')
        self.assertNotEqual(api, '')

    def test_map_place_url_exists_at_desired_location(self):
        response = self.client.get('/google/map/place')
        self.assertEqual(response.status_code, 200)

    def test_map_geocode_url_exists_at_desired_location(self):
        try:
            response = self.client.get('/google/map/geocode')
            self.assertEqual(response.status_code, 200)
        except HTTPError as e:
            #We didn't feed an input for geocode, getting 400 code is normal
            self.assertEqual(e.code, 400)

    def test_map_place_returns_valid_json_object(self):
        test_input = 'Istanbul'
        response = self.client.get('/google/map/place?input=' + test_input)
        try:
           json.loads(response.content)
           self.assertTrue(True)
        except ValueError:
            self.assertFalse(True)

    def test_map_geocode_returns_valid_json_object(self):
        #Placeholder place_id for geocode
        test_input = 'ChIJawhoAASnyhQR0LABvJj-zOE'
        response = self.client.get('/google/map/geocode?input=' + test_input)
        try:
           json.loads(response.content)
           self.assertTrue(True)
        except ValueError:
            self.assertFalse(True)

        
    