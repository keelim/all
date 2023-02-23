import arrow.core.Either
import arrow.core.Nel
import arrow.core.ValidatedNel
import arrow.core.computations.either
import arrow.core.handleErrorWith
import arrow.core.invalidNel
import arrow.core.validNel
import arrow.typeclasses.Semigroup
import arrow.core.zip

sealed class ValidationError(val msg: String) {
    data class DoesNotContain(val value: String) : ValidationError("Did not contain $value")
    data class MaxLength(val value: Int) : ValidationError("Exceeded length of $value")
    data class NotAnEmail(val reasons: Nel<ValidationError>) : ValidationError("Not a valid email")
}

data class FormField(val label: String, val value: String)
data class Email(val value: String)

sealed class Strategy {
    object FailFast : Strategy()
    object ErrorAccumulation : Strategy()
}

object Rules {

    private fun FormField.contains(needle: String): ValidatedNel<ValidationError, FormField> =
        if (value.contains(needle, false)) validNel()
        else ValidationError.DoesNotContain(needle).invalidNel()

    private fun FormField.maxLength(maxLength: Int): ValidatedNel<ValidationError, FormField> =
        if (value.length <= maxLength) validNel()
        else ValidationError.MaxLength(maxLength).invalidNel()

    private fun FormField.validateErrorAccumulate(): ValidatedNel<ValidationError, Email> =
        contains("@").zip(
            Semigroup.nonEmptyList(), // accumulates errors in a non empty list, can be omited for NonEmptyList
            maxLength(250)
        ) { _, _ -> Email(value) }.handleErrorWith { ValidationError.NotAnEmail(it).invalidNel() }

    /** either blocks support binding over Validated values with no additional cost or need to convert first to Either **/
    private fun FormField.validateFailFast(): Either<Nel<ValidationError>, Email> =
        either.eager {
            contains("@").bind() // fails fast on first error found
            maxLength(250).bind()
            Email(value)
        }
}
