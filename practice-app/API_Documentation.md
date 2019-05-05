
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

{TODO: Brief description of the API}

**URL** : `/currencyrate`

**Method** : `GET`

**Auth required** : NO

**Permissions required** : None

**Parameters** : None

## Success Response

{ TODO : ADD MORE RESPONSES IF NEEDED } 

**Code** : `200 OK`

**Content examples**

{TODO: BRIEF RESPONSE DESCRIPTION}

```json
{
    "API_SAMPLE" : "GOES_HERE"
}
```

## Notes

* { TODO: INSERT NECESSARY NOTES ABOUT YOUR API HERE }
  

</details>
<!--------               CURRENCY API ENDS               --------->


<!--------               NEWS API STARTS                   --------->
<details>
<summary>News API</summary>
<br>

# News

{TODO: Brief description of the API}

**URL** : `/news`

**Method** : `GET`

**Auth required** : NO

**Permissions required** : None

**Parameters** : None

## Success Response

{ TODO : ADD MORE RESPONSES IF NEEDED } 

**Code** : `200 OK`

**Content examples**

{TODO: BRIEF RESPONSE DESCRIPTION}

```json
{
    "API_SAMPLE" : "GOES_HERE"
}
```

## Notes

* { TODO: INSERT NECESSARY NOTES ABOUT YOUR API HERE }
 
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
<!--------               TWITTER API STARTS                   --------->


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
