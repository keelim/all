package com.keelim.core.designsystem.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow

/**
 * Strongly-typed text components that enforce AGENTS.md convention:
 * "All Text Composables MUST explicitly specify style and color"
 *
 * Usage:
 * ```
 * KeelimText.TitleLarge(
 *     text = "Title",
 *     color = MaterialTheme.colorScheme.onSurface
 * )
 * ```
 */
object KeelimText {

    @Composable
    fun TitleLarge(
        text: String,
        color: Color,
        modifier: Modifier = Modifier,
        textAlign: TextAlign? = null,
        textDecoration: TextDecoration? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        softWrap: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        minLines: Int = 1,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = color,
            modifier = modifier,
            textAlign = textAlign,
            textDecoration = textDecoration,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
        )
    }

    @Composable
    fun TitleMedium(
        text: String,
        color: Color,
        modifier: Modifier = Modifier,
        textAlign: TextAlign? = null,
        textDecoration: TextDecoration? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        softWrap: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        minLines: Int = 1,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = color,
            modifier = modifier,
            textAlign = textAlign,
            textDecoration = textDecoration,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
        )
    }

    @Composable
    fun TitleSmall(
        text: String,
        color: Color,
        modifier: Modifier = Modifier,
        textAlign: TextAlign? = null,
        textDecoration: TextDecoration? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        softWrap: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        minLines: Int = 1,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = color,
            modifier = modifier,
            textAlign = textAlign,
            textDecoration = textDecoration,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
        )
    }

    @Composable
    fun BodyLarge(
        text: String,
        color: Color,
        modifier: Modifier = Modifier,
        textAlign: TextAlign? = null,
        textDecoration: TextDecoration? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        softWrap: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        minLines: Int = 1,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = color,
            modifier = modifier,
            textAlign = textAlign,
            textDecoration = textDecoration,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
        )
    }

    @Composable
    fun BodyMedium(
        text: String,
        color: Color,
        modifier: Modifier = Modifier,
        textAlign: TextAlign? = null,
        textDecoration: TextDecoration? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        softWrap: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        minLines: Int = 1,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = color,
            modifier = modifier,
            textAlign = textAlign,
            textDecoration = textDecoration,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
        )
    }

    @Composable
    fun BodySmall(
        text: String,
        color: Color,
        modifier: Modifier = Modifier,
        textAlign: TextAlign? = null,
        textDecoration: TextDecoration? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        softWrap: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        minLines: Int = 1,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = color,
            modifier = modifier,
            textAlign = textAlign,
            textDecoration = textDecoration,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
        )
    }

    @Composable
    fun LabelLarge(
        text: String,
        color: Color,
        modifier: Modifier = Modifier,
        textAlign: TextAlign? = null,
        textDecoration: TextDecoration? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        softWrap: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        minLines: Int = 1,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = color,
            modifier = modifier,
            textAlign = textAlign,
            textDecoration = textDecoration,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
        )
    }

    @Composable
    fun LabelMedium(
        text: String,
        color: Color,
        modifier: Modifier = Modifier,
        textAlign: TextAlign? = null,
        textDecoration: TextDecoration? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        softWrap: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        minLines: Int = 1,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = color,
            modifier = modifier,
            textAlign = textAlign,
            textDecoration = textDecoration,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
        )
    }

    @Composable
    fun LabelSmall(
        text: String,
        color: Color,
        modifier: Modifier = Modifier,
        textAlign: TextAlign? = null,
        textDecoration: TextDecoration? = null,
        overflow: TextOverflow = TextOverflow.Clip,
        softWrap: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        minLines: Int = 1,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = modifier,
            textAlign = textAlign,
            textDecoration = textDecoration,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
        )
    }
}
