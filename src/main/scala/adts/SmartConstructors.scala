package adts

object SmartConstructors {
  /**
  TODO
   * Sometimes we don't want to take the time to model data precisely.
   * For example, we might want to
   * model an email address with a string, even though most strings are not valid email addresses.
   *
   * In such cases, we can save time by using a smart constructor, which lets us ensure we model
   * only valid data, but without complicated data types.
   */
  object smart_constructors {
    sealed abstract case class Email private(value: String)

    object Email {
      def fromString(email: String): Option[Email] =
        if (email.matches("""/\w+@\w+.com""")) Some(new Email(email) {}) else None
    }

  }

  /**
   * EXERCISE 1
   *
   * Create a smart constructor for `NonNegative` which ensures the integer is always non-negative.
   */
  sealed abstract case class NonNegative private (value: Int)

  /**
   * EXERCISE 2
   *
   * Create a smart constructor for `Age` that ensures the integer is between 0 and 120.
   */
  sealed abstract case class Age private (value: Int)

  /**
   * EXERCISE 3
   *
   * Create a smart constructor for password that ensures some security considerations are met.
   */
  sealed abstract case class Password private (value: String)
}