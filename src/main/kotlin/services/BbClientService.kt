package services

import com.intellij.openapi.components.NamedComponent
import configuration.connection.BbClient

interface BbClientService: NamedComponent {
    fun setBbClient(client: BbClient)
    fun getBbClient(): BbClient?
}