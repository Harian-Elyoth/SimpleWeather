// class WeatherRepository @Inject constructor(
//     private val weatherApi: WeatherApi, // For api calls
//     private val weatherDao: WeatherDao, // For local database
//     private val dispatcher: CoroutineDispatcher // For background operations
// ) {
//     suspend fun getCurrentWeather(city: String): Result<WeatherData> {
//         return withContext(dispatcher) {
//             try {
//                 // First try to get from local database
//                 val localData = weatherDao.getWeatherForCity(city)
//                 if (localData != null && !isDataStale(localData)) {
//                     return@withContext Result.success(localData)
//                 }

//                 val apiResponse = weatherApi.getCurrentWeather(city)

//                 weatherDao.insertWeather(apiResponse)
//                 Result.success(apiResponse)
//             } catch (e: Exception) {
//                 Result.failure(e)
//             }
//         }
//     }

//     suspend fun getWeeklyForecast(city: String): Result<List<DailyForecast>> {
//         return withContext(dispatcher) {
//             try {
//                 val apiResponse = weatherApi.getWeeklyForecast(city)
//                 weatherDao.insertForecast(city, apiResponse)
//                 Result.success(apiResponse)
//             } catch (e: Exception) {
//                 Result.failure(e)
//             }
//         }
//     }

//     suspend fun searchCities(query: String): Result<List<City>> {
//         return withContext(dispatcher) {
//             try {
//                 val cities = weatherApi.searchCities(query)
//                 Result.success(cities)
//             } catch (e: Exception) {
//                 Result.failure(e)
//             }
//         }
//     }

//     suspend fun isDataStale(data: WeatherData): Boolean {
//         // Check if data is older than 30 minutes
//         return System.currentTimeMillis() - data.timestamp > 1000 * 60 * 30
//     }
// }
