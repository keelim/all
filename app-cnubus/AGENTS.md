# CNUBUS

Own campus bus and map presentation and app navigation. Keep Google Maps/device permission handling explicit; unavailable location or provider data is not a valid map result. Route screen state through ViewModels and repositories, preserving existing MapViewModel changes. Keep scheduling/data operations off the main thread. For this module’s changes use only `./gradlew :app-cnubus:testDebugUnitTest`.
