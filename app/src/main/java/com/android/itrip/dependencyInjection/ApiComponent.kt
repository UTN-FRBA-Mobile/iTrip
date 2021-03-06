package com.android.itrip.dependencyInjection

import com.android.itrip.activities.LogInActivity
import com.android.itrip.activities.MainActivity
import com.android.itrip.activities.QuizActivity
import com.android.itrip.fragments.CreateTravelFragment
import com.android.itrip.services.AuthenticationService
import com.android.itrip.services.DatabaseService
import com.android.itrip.services.StorageService
import com.android.itrip.services.TravelService
import com.android.itrip.util.Toaster
import com.android.itrip.util.VolleyClient
import com.android.itrip.viewModels.*
import dagger.Component


@Component(modules = [ContextModule::class])
interface ApiComponent {

    fun getVolleyClient(): VolleyClient
    fun getToaster(): Toaster
    fun getAuthenticationService(): AuthenticationService
    fun getQuizService(): AuthenticationService
    fun getTravelService(): TravelService
    fun getStorageService(): StorageService
    fun getDatabaseService(): DatabaseService

    fun injectLogInActivity(activity: LogInActivity)
    fun injectMainActivity(activity: MainActivity)
    fun injectQuizActivity(activity: QuizActivity)

    fun injectHomeViewModel(viewModel: HomeViewModel)
    fun injectActivitiesViewModel(viewModel: ActivitiesViewModel)
    fun injectCreateTravelViewMovel(viewModel: CreateTravelViewMovel)
    fun injectDestinationViewModel(viewModel: DestinationViewModel)
    fun injectQuizViewModel(viewModel: QuizViewModel)
    fun injectScheduleViewModel(viewModel: ScheduleViewModel)
    fun injectTripViewModel(viewModel: TripViewModel)

    fun injectCreateTravelFragment(createTravelFragment: CreateTravelFragment)

    fun injectStorageService(storageService: StorageService)
}
