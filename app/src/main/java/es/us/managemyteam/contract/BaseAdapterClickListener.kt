package es.us.managemyteam.contract

interface BaseAdapterClickListener<T> {

    fun onAdapterItemClicked(item: T, position: Int)

}