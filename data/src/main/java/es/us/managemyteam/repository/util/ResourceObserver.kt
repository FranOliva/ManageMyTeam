package es.us.managemyteam.repository.util

import androidx.lifecycle.Observer
import es.us.managemyteam.data.R

abstract class ResourceObserver<T> : Observer<Resource<T>> {

    override fun onChanged(resource: Resource<T>?) {
        resource?.let {
            when {
                it.isSuccess() -> {
                    onLoading(false)
                    onSuccess(it.data)
                }
                it.isError() -> {
                    onLoading(false)
                     it.error?.let { error ->
                        onError(error)
                    }
                }
                else -> {
                    onLoading(true)
                }
            }
        }
    }

    abstract fun onSuccess(response: T?)

    open fun onError(error: Error) {
        if (error.isUnknown()) {
            error.errorMessageId = R.string.unknown_error
        }
    }

    open fun onLoading(loading: Boolean) {
        // no-op
    }

}