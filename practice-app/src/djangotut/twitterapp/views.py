from django.shortcuts import render
from django.http import HttpResponse, JsonResponse

import tweepy 
import json

import environ

env = environ.Env(DEBUG=(bool, False))
# Create your views here.

def twitter_news_view(request):

    consumer_key = env("CONSUMER_KEY")
    consumer_secret = env("CONSUMER_SECRET")
    access_token = env("ACCESS_TOKEN")
    access_token_secret = env("ACCESS_SECRET")

    auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)

    API = tweepy.API(auth)
#,'AswathDamodaran','mark_dow','Lavorgnanomics','Ralph_Acampora','jimcramer','TheStalwart','drdaronacemoglu','TruthGundlach']
    resource_users_id = ['Investingcom','newsinvesting','InvestingTR','cryptonewsday']
    all_links = []
    for source in resource_users_id:
        tweets = API.user_timeline(source)
        
    for tweet in tweets:
        try:
            a = tweet._json['entities']['urls'][0]['expanded_url']
            if "twitter" not in a:
                all_links.append(a)  
        except:
            pass

    return JsonResponse({'all_links':all_links})