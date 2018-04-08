package com.dnnt.touch.base

import com.raizlabs.android.dbflow.annotation.Database

/**
 * Created by dnnt on 18-4-6.
 */
@Database(name = AppDatabase.DB_NAME, version = AppDatabase.VERSION)
object AppDatabase {
    const val DB_NAME = "TOUCH"
    const val VERSION = 1
}
