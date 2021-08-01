# Weather Sample App

1. The app base on MVVM architecture, because application range is small so Single Responsibility principle is the most used. I have one Activiy which observe data from SearchWeatherViewModel and update on UI. SearchWeatherViewModel includes WeatherRepository which used to connect to ApiService to fetch data from server. I use a Retrofit to get response from API, using Retrofit catching able response data even if device offline. WeatherDeserializer used to help Gson parse special data that I want.
2. My code folder structure:
  - data: contains all classes which used to handle data parsing like data model, repository, Retrofit client, Deserializer,...
  - enums: contains all classes which used to define application level constant,...
  - manager: contains all classes which used to manage application like Shared Preference manager,...
  - service: contains all classes which used to connect to server,...
  - ui: contains all classes which present application views and UI handler like SearchWeatherActivity, viewmodel, adapter,...
  - utils: contains all application level classes which used to convert (date, number, view, unit,...) from any place in app.
  - WeatherApp: an application class used to manage app lifecycle if need.
3. How to run local app: open project by Android Studio 4.0.1 or above, gradle version: gradle-6.1.1.
4. Checklist items has done:
  - Kotlin is used.
  - MVVM architecture is used.
  - LiveData is used.
  - UI looks like in attachment.
  - Unit test is wrote for the most importance class: SearchWeatherViewModelTest.
  - Avoid decompile apk file, using proguard in release build. 
  - Exception handling.
  - Readme file inclued.
  - Configuaration change handling.
  - Retrofit data caching.
  - Views catching.
