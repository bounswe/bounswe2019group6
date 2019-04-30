
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

def twitter_news_view(request):

    with open('twitter_credentials.json') as f:
        credentials = json.load(f)

    consumer_key = credentials["CONSUMER_KEY"]
    consumer_secret = credentials["CONSUMER_SECRET"]
    access_token = credentials["ACCESS_TOKEN"]
    access_token_secret = credentials["ACCESS_SECRET"]

    auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)

    API = tweepy.API(auth)

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