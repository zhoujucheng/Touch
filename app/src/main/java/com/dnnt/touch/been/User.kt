package com.dnnt.touch.been

import com.dnnt.touch.base.AppDatabase
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import java.io.Serializable

/**
 * Created by dnnt on 18-1-25.
 */
@Table(database = AppDatabase::class)
data class User(@PrimaryKey var id: Long = 0, @PrimaryKey var friendId: Long = 0,
                @Column var userName: String = "", var phone: String? = null,
                @Column var headUrl: String = "", @Column var nickname: String? = null) : Serializable