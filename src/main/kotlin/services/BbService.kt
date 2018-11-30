package services

import com.intellij.openapi.components.NamedComponent
import tools.BbTools

interface BbService: NamedComponent {
    fun setBbTools(bbTools: BbTools)

    fun getBbTools(): BbTools?
}
