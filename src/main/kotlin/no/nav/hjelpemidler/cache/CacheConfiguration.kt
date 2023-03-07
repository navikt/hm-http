package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.Expiry
import kotlin.time.Duration

class CacheConfiguration {
    var initialCapacity: Int? = null
    var maximumSize: Long? = null
    var maximumWeight: Long? = null
    var weakKeys: Boolean = false
    var weakValues: Boolean = false
    var softValues: Boolean = false
    var expireAfterWrite: Duration? = null
    var expireAfterAccess: Duration? = null
    var refreshAfterWrite: Duration? = null
    var recordStats: Boolean = false
}
