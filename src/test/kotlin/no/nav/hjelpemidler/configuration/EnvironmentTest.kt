package no.nav.hjelpemidler.configuration

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class EnvironmentTest {
    @Test
    fun `gjeldende miljø skal være local`() {
        Environment.current shouldBe LocalEnvironment
    }

    @Test
    fun `hvert miljø har riktig tier`() {
        LocalEnvironment shouldHaveTier Environment.Tier.LOCAL
        FssEnvironment.DEV shouldHaveTier Environment.Tier.DEV
        FssEnvironment.PROD shouldHaveTier Environment.Tier.PROD
        GcpEnvironment.LABS shouldHaveTier Environment.Tier.LABS
        GcpEnvironment.DEV shouldHaveTier Environment.Tier.DEV
        GcpEnvironment.PROD shouldHaveTier Environment.Tier.PROD
    }

    @Test
    fun `hver tier har riktig flagg`() {
        Environment.Tier.LOCAL.shouldHaveFlag(isLocal = true)
        Environment.Tier.LABS.shouldHaveFlag(isLabs = true)
        Environment.Tier.DEV.shouldHaveFlag(isDev = true)
        Environment.Tier.PROD.shouldHaveFlag(isProd = true)
    }

    private infix fun Environment.shouldHaveTier(tier: Environment.Tier) {
        this.tier shouldBe tier
    }

    private fun Environment.Tier.shouldHaveFlag(
        isLocal: Boolean = false,
        isLabs: Boolean = false,
        isDev: Boolean = false,
        isProd: Boolean = false,
    ) = assertSoftly {
        this.isLocal shouldBe isLocal
        this.isLabs shouldBe isLabs
        this.isDev shouldBe isDev
        this.isProd shouldBe isProd
    }
}
