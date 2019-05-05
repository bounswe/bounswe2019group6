
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

{TODO: Brief description of the API}

**URL** : `/twitter`

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
<!--------               TWITTER API STARTS                   --------->


<!--------               GOOGLE API STARTS                   --------->

<details>

<summary>Google API</summary>
<br>

# Google 

{TODO: Brief description of the API}

**URL** : `/google`

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

<!--------               GOOGLE API ENDS                   --------->
