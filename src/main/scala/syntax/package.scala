package policykit

package object syntax {
  implicit class CommutativeSemiringOps[A](x: A) {
    def *(y: A)(implicit cs: CommutativeSemiring[A]): A =
      cs.*(x, y)

    def +(y: A)(implicit cs: CommutativeSemiring[A]): A =
      cs.+(x, y)

    def and(y: A)(implicit cs: CommutativeSemiring[A]): A =
      cs.*(x, y)

    def or(y: A)(implicit cs: CommutativeSemiring[A]): A =
      cs.+(x, y)
  }
}
