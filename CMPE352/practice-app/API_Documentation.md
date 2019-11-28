
### Base URL = `http://54.89.235.179/`
---

<!--------               EVENTS API STARTS               --------->

<details>
<summary>Events API</summary>
<br>

# Events

Get the details of economic events perfoming today.

**URL** : `/events`

**Method** : `GET`

**Auth required** : NO

**Permissions required** : None

**Parameters** : None

## Success Response

**Code** : `200 OK`

The return type is a list of dictionaries of which elements contain event information.

**Content examples**

```json
    [{
        "name": "General Elections",
        "country": "Panama",
        "date": "2019-05-05",
        "rep": 2,
        "actual": "",
        "prev": "",
        "forecast": ""
    },
    {
        "name": "Emirates NBD PMI",
        "country": "Egypt",
        "date": "2019-05-05",
        "rep": 1,
        "actual": "50.8",
        "prev": "49.9",
        "forecast": "50.1"
    }]
```

## Notes

* "rep" key resresents the importance of an economic event ranging from 1 to 3.
* "actual", "prev" and "forecast" keys might have empty values for some events.
  
</details>
<!--------               EVENTS API ENDS               --------->


<!--------               CURRENCY API STARTS               --------->

<details>
<summary>Currency Rate API</summary>
<br>

# Currency Rate

After obtaining request from user, it respond the requested currency translation or all currency translations.

**URL** : `/currencyrate/<str:source>to<str:target>`

**Method** : `POST`

**Auth required** : NO

**Permissions required** : None

**Parameters** : source, target

## Success Response

**Code** : `200 OK`

**Content examples**

```json
{
    "response": success,
    "datetime": "2019-05-06 11:49:05",
    "Source to Target: "6.708424",
}
```

## Notes

No note here!
  

</details>
<!--------               CURRENCY API ENDS               --------->


<!--------               NEWS API STARTS                   --------->
<details>
<summary>News API</summary>
<br>

# News

Having information about the newly emerged financial news 

**URL** : `/news`

**Method** : `GET`

**Auth required** : NO

**Permissions required** : None

**Parameters** : None

## Success Response


**Code** : `200 OK`

**Content examples**

List of dictionaries is returned, each are itemized by their id's. Information about the news are categorized accordingly.



```json

{"77111":
    {"id": "77111",
     "title": "Rwanda Inflation Rate Lowest in 3 Months in April",
     "description": "The annual inflation rate in Rwanda eased to 0.2 percent in April 2019 from 1.1 percent in the prior month. It is 
                     the lowest inflation since January, as prices fell further for food & non-alcoholic beverages (-3.1% vs 2.4% in 
                     March) and slowed for transport (3% vs 6.7%). Also, cost was flat for housing & utilities (vs 1.1% in March). On a 
                     monthly basis, consumer prices went up 0.3 percent, following a 1.1 percent rise in the previous month.",
     "date": "2019-05-10",
     "country": "Rwanda",
     "symbol": "RwandaIR",
     "url": "/rwanda/inflation-cpi"},
 "77107": 
    {"id": "77107",
    "title": "Kosovo Inflation Rate Highest in Over 6 Years", 
    "description": "Annual inflation rate in Kosovo edged up to 3.4 percent in April of 2019 from 3.3 percent in the previous month. It was 
                    the highest inflation rate since January of 2013, as cost rose faster for food & non-alcoholic beverages (7.1 percent 
                    from 6.7 percent in March) and for furniture & household equipment (0.9 percent vs 0.3 percent). On the other hand, 
                    prices went up at a softer pace for transport (2.2 percent vs 2.7 percent); alcoholic beverages & tobacco (2.7 percent 
                    vs 2.8 percent) and miscellaneous goods & services (1.0 percent vs 1.3 percent) while cost fell for housing & utilities 
                    (-1.4 percent vs -1.3 percent); recreation & culture (-0.3 percent vs -0.4 percent) and communication (-0.3 percent vs 
                    -0.5 percent). On a monthly basis, consumer prices went up 0.1 percent, the same as in March.",
    "date": "2019-05-10", 
    "country": "Kosovo",
    "symbol": "KOSSOVOINFNRATE", 
    "url": "/kosovo/inflation-cpi"}
}

```

## Notes

* There is no note for the api
 
</details>

<!--------               NEWS API ENDS                   --------->

<!--------               TWITTER API STARTS                   --------->

<details>
<summary>Twitter API</summary>
<br>

# Tweets

* Returns some links that leads to events and news

**URL** : `/twitter`

**Method** : `GET`

**Auth required** : NO

**Permissions required** : None

**Parameters** : None

## Success Response

**Code** : `200 OK`

**Content examples**

```json
{
    "all_links": [
        "https://go.shr.lc/2L9MKtJ",
        "https://www.publish0x.com/earning-crypto/what-are-security-tokens-and-stos-xllynk?a=mWZdPqweKg",
        "https://go.shr.lc/2VeKaGv",
        "http://bit.ly/2U1f8gz",
        "https://www.publish0x.com/consensus-report/along-with-bitcos-prices-lightg-network-numbers-explod-xepzw?a=mWZdPqweKg"
    ]
}
```

## Notes

* NO NOTE HERE
  

</details>
<!--------               TWITTER API ENDS                   --------->


<!--------               GOOGLE API STARTS                   --------->

<details>

<summary>Google API</summary>
<br>

# Google Place

Suggest autocomplete places for given input keyword

**URL** : `/google/map/place`

**Method** : `GET`

**Auth required** : NO

**Permissions required** : None

**Parameters** : input

## Success Response

Place predictions about given input

**Code** : `200 OK`

**Content examples**

```json
{
    "predictions" : [ 
        { 
            "description" : "Ankara, Turkey", 
            "id" : "908fdf0efc46fb81721d9b06ff54ee23e8703a4f", 
            "matched_substrings" : [ 
                { 
                    "length" : 6, 
                    "offset" : 0 
                } 
            ],
            "place_id" : "ChIJsS1zINVH0xQRjSuEwLBX3As",
            "reference" : "ChIJsS1zINVH0xQRjSuEwLBX3As", 
            "structured_formatting" : { 
                "main_text" : "Ankara", 
                "main_text_matched_substrings" : [ 
                    { 
                        "length" : 6, 
                        "offset" : 0 
                    }
                ], 
                "secondary_text" : "Turkey" 
            }, 
            "terms" : [ 
                { 
                    "offset" : 0, 
                    "value" : "Ankara" 
                }, 
                { 
                    "offset" : 8, 
                    "value" : "Turkey" 
                } 
            ], 
            "types" : [ "locality", "political", "geocode" ] 
        }
    ],
    "status" : "OK" 
}
```

## Notes

*NO NOTE HERE*

# Google Geocode

Returns Google Geocode Object from given place_id

**URL** : `/google/map/geocode`

**Method** : `GET`

**Auth required** : NO

**Permissions required** : None

**Parameters** : input

## Success Response

Geocode Object

**Code** : `200 OK`

**Content examples**

```json
{ 
    "results" : [ 
        { 
            "address_components" : [ 
                { 
                    "long_name" : "Ankara", 
                    "short_name" : "Ankara", 
                    "types" : [ "locality", "political" ] 
                }, 
                { 
                    "long_name" : "Ankara", 
                    "short_name" : "Ankara", 
                    "types" : [ "administrative_area_level_1", "political" ] 
                }, 
                { 
                    "long_name" : "Turkey", 
                    "short_name" : "TR", 
                    "types" : [ "country", "political" ] 
                } 
            ], 
            "formatted_address" : "Ankara, Turkey", 
            "geometry" : { 
                "bounds" : { 
                    "northeast" : { 
                        "lat" : 40.076332, 
                        "lng" : 33.007056 
                    }, 
                    "southwest" : { 
                        "lat" : 39.7304211, 
                        "lng" : 32.5184735 
                    } 
                }, 
                "location" : { 
                    "lat" : 39.9333635, 
                    "lng" : 32.8597419 
                }, 
                "location_type" : "APPROXIMATE", 
                "viewport" : { 
                    "northeast" : { 
                        "lat" : 40.076332, 
                        "lng" : 33.007056 
                    }, 
                    "southwest" : { 
                        "lat" : 39.7304211, 
                        "lng" : 32.5184735 
                    } 
                }
            }, 
            "place_id" : "ChIJsS1zINVH0xQRjSuEwLBX3As", 
            "types" : [ "locality", "political" ] 
        } 
    ], 
    "status" : "OK" 
}
```

## Notes

*NO NOTE HERE*
    
</details>

<!--------               GOOGLE API ENDS                   --------->
