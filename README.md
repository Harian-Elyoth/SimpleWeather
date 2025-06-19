# SimpleWeather App

A simple Android weather app that displays current weather information using the OpenWeather API with persistent city management.

## Features

- Display current weather for any city
- Show weather forecasts
- Beautiful weather cards with icons
- Real-time weather data from OpenWeather API
- **Persistent city management** - Add/remove cities that persist across app restarts
- **Local caching** - Weather data cached locally for offline access
- **Swipe to delete** - Remove cities by swiping left or right
- **Pull to refresh** - Refresh weather data by pulling down
- **Default cities** - Pre-loaded with popular cities on first launch

## Setup

### 1. Get OpenWeather API Key

1. Go to [OpenWeather API](https://openweathermap.org/api)
2. Sign up for a free account
3. Navigate to your [API keys page](https://home.openweathermap.org/api_keys)
4. Copy your API key

### 2. Configure API Key

1. Open `app/src/main/java/com/example/simpleweather/data/api/WeatherUtils.kt`
2. Replace `YOUR_API_KEY_HERE` with your actual API key:

```kotlin
const val API_KEY = "your_actual_api_key_here"
```

### 3. Build and Run

1. Sync your project with Gradle files
2. Build and run the app on your device or emulator

## City Management

### Adding Cities
- Tap the **+** floating action button
- Enter the city name in the dialog
- Tap "Add City" to add it to your favorites

### Removing Cities
- **Swipe left or right** on any weather card to remove it
- Use the "UNDO" option in the snackbar to restore the city

### Default Cities
On first launch, the app automatically adds these cities:
- London
- Paris
- New York
- Tokyo
- Sydney

## API Usage

The app uses the following OpenWeather API endpoints:

- **Current Weather**: `api.openweathermap.org/data/2.5/weather`
- **5-Day Forecast**: `api.openweathermap.org/data/2.5/forecast`

### API Limits

- Free tier: 1,000 API calls per day
- Rate limit: 1 call per 10 minutes per location (recommended)

## Local Database

The app uses **Room Database** for persistent storage:

### Database Entities
- **CityEntity**: Stores user's favorite cities
- **WeatherDataEntity**: Caches weather data locally

### Features
- **30-minute cache**: Weather data cached for 30 minutes
- **Offline support**: View cached weather data when offline
- **Automatic cleanup**: Old cache data automatically removed

## Project Structure

```
app/src/main/java/com/example/simpleweather/
├── data/
│   ├── api/
│   │   ├── WeatherApi.kt          # Retrofit API interface
│   │   ├── NetworkModule.kt       # Retrofit configuration
│   │   └── WeatherUtils.kt        # API utilities and formatting
│   ├── database/
│   │   └── WeatherDatabase.kt     # Room database
│   ├── dao/
│   │   ├── CityDao.kt             # City database operations
│   │   └── WeatherDataDao.kt      # Weather data database operations
│   ├── model/
│   │   ├── WeatherResponse.kt     # API response models
│   │   ├── WeatherCard.kt         # UI data model
│   │   ├── CityEntity.kt          # City database entity
│   │   └── WeatherDataEntity.kt   # Weather data database entity
│   └── repository/
│       ├── WeatherRepository.kt   # Data layer with caching
│       └── CityManager.kt         # City management utilities
├── ui/
│   ├── weathercard/
│   │   ├── WeatherCardFragment.kt # Weather card UI
│   │   └── WeatherCardViewModel.kt # Weather card logic
│   └── weatherlist/
│       ├── WeatherListFragment.kt # Weather list UI
│       └── WeatherListViewModel.kt # Weather list logic
└── MainActivity.kt                # Main activity
```

## Dependencies

- **Retrofit**: HTTP client for API calls
- **Gson**: JSON parsing
- **Glide**: Image loading for weather icons
- **Room**: Local database for persistence
- **Coroutines**: Asynchronous programming
- **ViewModel & LiveData**: Architecture components
- **SwipeRefreshLayout**: Pull-to-refresh functionality

## Usage Examples

### Add a new city:
```kotlin
viewModel.addCity("Berlin")
```

### Remove a city:
```kotlin
viewModel.removeCity("London")
```

### Refresh all weather data:
```kotlin
viewModel.refreshWeatherData()
```

### Load weather for a city:
```kotlin
viewModel.loadWeatherForCity("Paris")
```

## Weather Icons

The app displays weather icons from OpenWeather's icon set. Icons are automatically loaded based on the weather condition codes returned by the API.

## Error Handling

The app includes comprehensive error handling for:
- Network connectivity issues
- Invalid API keys
- City not found errors
- Rate limiting
- Database operations

## Data Persistence

- **Cities**: Saved locally and persist across app restarts
- **Weather Data**: Cached for 30 minutes with automatic refresh
- **User Preferences**: First launch detection and settings

## Contributing

Feel free to submit issues and enhancement requests!

## License

This project is for educational purposes. 