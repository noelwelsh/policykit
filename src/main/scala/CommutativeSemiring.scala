package policykit

import cats.Monoid

/** Commutative semiring type class
  */
trait CommutativeSemiring[A] extends Monoid[A] {
  def +(x: A, y: A): A
  def zero: A

  def *(x: A, y: A): A
  def one: A

  def combine(x: A, y: A): A = *(x, y)
  def empty: A = one
}
object CommutativeSemiring {
  import policykit.syntax._

  implicit def optionCommutativeSemiring[A: CommutativeSemiring]: CommutativeSemiring[Option[A]] =
    new CommutativeSemiring[Option[A]] {
      def +(x: Option[A], y: Option[A]): Option[A] =
        (x, y) match {
          case (Some(x1), Some(y1)) => Some(x1.+(y1))
          case (Some(x1), None)     => Some(x1)
          case (None, Some(y1))     => Some(y1)
          case (None, None)         => None
        }
      def zero: Option[A] = None

      def *(x: Option[A], y: Option[A]): Option[A] =
        (x, y) match {
          case (Some(x1), Some(y1)) => Some(x1.*(y1))
          case _                    => None
        }
      def one: Option[A] = Some(CommutativeSemiring[A].one)
    }

  def apply[A](implicit cs: CommutativeSemiring[A]): CommutativeSemiring[A] =
    cs

  def zero[A](implicit cs: CommutativeSemiring[A]): A =
    cs.zero

  def one[A](implicit cs: CommutativeSemiring[A]): A =
    cs.one

  def andAll[A: CommutativeSemiring](as: IterableOnce[A]): A =
    as.iterator.foldLeft(CommutativeSemiring[A].one)((accum, elt) => accum.and(elt))

  def orAll[A: CommutativeSemiring](as: IterableOnce[A]): A =
    as.iterator.foldLeft(CommutativeSemiring[A].zero)((accum, elt) => accum.or(elt))
}
