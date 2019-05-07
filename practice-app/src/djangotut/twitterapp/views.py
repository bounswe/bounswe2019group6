from django.shortcuts import render
from django.http import HttpResponse, JsonResponse

import tweepy 
import json

import environ

env = environ.Env(DEBUG=(bool, False))
# Create your views here.

# twitter view that uses the twitter api and return the links
def twitter_news_view(request):

    # necessary keys that will be used in twitter api
    consumer_key = env("CONSUMER_KEY")
    consumer_secret = env("CONSUMER_SECRET")
    access_token = env("ACCESS_TOKEN")
    access_token_secret = env("ACCESS_SECRET")

    # tiwtter authentication 
    auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)

    # api object that the reuqests will be made
    API = tweepy.API(auth)

    # accounts that will be crawled
    resource_users_id = ['Investingcom','newsinvesting','InvestingTR','cryptonewsday']
    
    # list that the links will be collected into
    all_links = []

    # collects all tweets of the accouts
    for source in resource_users_id:
        tweets = API.user_timeline(source)
        
    # extract the links from tweets
    for tweet in tweets:
        try:
            a = tweet._json['entities']['urls'][0]['expanded_url']

            # if link directs to another twitter post, neglect it
            if "twitter" not in a:
                all_links.append(a)  
        except:
            pass

    # return the json object
    return JsonResponse({'all_links':all_links})