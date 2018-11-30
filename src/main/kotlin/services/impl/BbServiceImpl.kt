package services.impl

import services.BbService
import tools.BbTools

class BbServiceImpl : BbService {
    private var bbTools: BbTools? = null

    override fun getBbTools(): BbTools? {
        return bbTools
    }

    override fun setBbTools(bbTools: BbTools) {
        this.bbTools = bbTools
    }

    override fun getComponentName(): String {
        return "Bobril Build Component"
    }
}
