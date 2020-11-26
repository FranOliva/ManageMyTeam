package es.us.managemyteam.contract

interface PreferencesInterface {

    fun save(key: String, value: Any)

    fun <T> get(key: String, defaultValue: T? = null): T?

}