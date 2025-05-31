package ru.netology.niffer_android.publisher

object InMemPublisher : Publisher {

    val storage: Map<String, Set<String>> get() = _storage.mapKeys { it.key.name }
    private val _storage: HashMap<Class<*>, HashSet<String>> = HashMap()

    override fun publish(
        pageObjectClass: Class<*>,
        element: String,
    ) {
        _storage.putIfAbsent(pageObjectClass, HashSet())
        _storage[pageObjectClass]!!.add(element)
    }

}