package com.android.itrip.dependencyInjection

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Named


@Module
internal class ContextModule(
    @get:Provides
    @get:Named("ApplicationContext")
    var context: Context
)