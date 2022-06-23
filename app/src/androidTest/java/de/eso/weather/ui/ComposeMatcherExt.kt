package de.eso.weather.ui

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasText

fun withRole(role: Role) = SemanticsMatcher("Role is $role") {
    val roleProperty = it.config.getOrNull(SemanticsProperties.Role) ?: false
    roleProperty == role
}

fun SemanticsNodeInteractionsProvider.onTextInButton(text: String) =
    onNode(hasText(text) and hasParent(withRole(Role.Button)), useUnmergedTree = true)
