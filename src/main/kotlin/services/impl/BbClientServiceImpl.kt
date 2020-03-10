package services.impl

import configuration.connection.BbClient
import services.BbClientService

class BbClientServiceImpl : BbClientService {
    private var bbClient: BbClient? = null

    override fun getBbClient(): BbClient? {
        return bbClient
    }

    override fun setBbClient(client: BbClient) {
        bbClient = client
    }
}