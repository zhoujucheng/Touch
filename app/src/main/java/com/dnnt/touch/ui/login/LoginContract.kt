package com.dnnt.touch.ui.login

import com.dnnt.touch.BasePresenter
import com.dnnt.touch.BaseView

/**
 * Created by dnnt on 18-1-25.
 */
interface LoginContract {
    interface View : BaseView<Presenter>

    interface Presenter : BasePresenter<View>
}