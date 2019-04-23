
from django.shortcuts import render
from django.http import HttpResponse, JsonResponse

from .models import Article

import tweepy 
import json

# Create your views here.

def article_detail_view(request):
    query_set = Article.objects.all()
    context = {
        'object_set' : query_set
    }
    return render(request,'article_detail.html', context)

def twitter_sado_view(request):

    with open('twitter_credentials.json') as f:
        credentials = json.load(f)

    # keys that are taken from twitter
    consumer_key = credentials["CONSIMER_KEY"]
    consumer_secret = credentials["CONSIMER_SECRET"]
    access_token = credentials["ACCESS_TOKEN"]
    access_token_secret = credentials["ACCESS_SECRET"]
    
    # OAuth process, using the keys and tokens
    auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)
    
    # Creation of the actual interface, using authentication
    # using the api variable, you can use all functions that tweepy support
    api = tweepy.API(auth)

    # dictionary to return
    context = {}

    # variable that shows to myself
    me = api.me()

    # lists floowers of me
    followers = api.followers_ids(id = me)
    context['followers'] = followers

    # lists everyone that I am following
    following = api.friends_ids(id = me)
    context['following'] = following

    # all tweets that I posted
    #all_tweets = api.home_timeline(id = me)
    #context['all_tweets'] = all_tweets

    #for tweet in all_tweets:
    #    pprint(tweet._json["text"])

    # You can post new tweet using this function
    # api.update_status(status = 'Hello Twitter! This is my first api tweet')

    # Almost all fucntions have some mendetory argument. Also they have optinal arguments, 
    # in order to specify your api call, you can use those argumnets

    # for more info look to the link below
    # https://tweepy.readthedocs.io/en/v3.5.0/index.html

    return JsonResponse(context)