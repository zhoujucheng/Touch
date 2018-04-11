package com.dnnt.touch.been

import com.dnnt.touch.base.AppDatabase
import com.dnnt.touch.ui.main.message.MessageFragment
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import java.util.*

/**
 * Created by dnnt on 18-3-15.
 */
@Table(database = AppDatabase::class)
data class LatestChat(@PrimaryKey var id: Long = 0, @Column var headUrl: String = "",
                      @Column var nickname: String = "", @Column var time: Date = Date(),
                      @Column var latestMsg: String = "", @Column var type: Int = 0)