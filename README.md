CityGuide
=========

Features included:
- Pull to Refresh
- Distance Matrix api and google places api were used to fetch and calculate the distance of nearby coffeeshops, bars, and bistros (restaurants)
- Tries to fetch places within 500 miles radius
- Results shown are sorted based on how close they are from you.
- All api calls sync to the local SQLite database, thus results are cached in case no internet coverage is available.
- The slider is draggable, and so are the fragments showing the list of places.

Steps to run the app:

- Open the app.
- Wait for data to be fetched. If taking too long, pull to refresh to check if new data is available as you move.

External libraries used:

- Retrofit: for making API calls.