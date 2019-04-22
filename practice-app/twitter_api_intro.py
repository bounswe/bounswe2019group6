import tweepy 
import json

from pprint import pprint

with open('twitter_credentials.json') as f:
    credentials = json.load(f)

# Consumer keys and access tokens, used for OAuth
# I have my own keys in my twitter_credentials.json file
# In order to use  the api, pelease create your own developer account, 
# and create a twitter_credentials.json file
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

# variable that shows to myself
me = api.me()

# lists floowers of me
followers = api.followers_ids(id = me)

# lists everyone that I am following
following = api.friends_ids(id = me)

# all tweets that I posted
all_tweets = api.home_timeline(id = me)

for tweet in all_tweets:
    pprint(tweet._json["text"])

# You can post new tweet using this function
api.update_status(status = 'Hello Twitter! This is my first api tweet')

# Almost all fucntions have some mendetory argument. Also they have optinal arguments, 
# in order to specify your api call, you can use those argumnets

# for more info look to the link below
# https://tweepy.readthedocs.io/en/v3.5.0/index.html