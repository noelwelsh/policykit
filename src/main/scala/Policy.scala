package policykit

import cats._
import cats.syntax.all._

/** A policy defines the actions that should be taken in response to a request
  */
sealed trait Policy[Request, Action] {
  def apply(r: Request): Option[Action]
}
object Policy {
  implicit def policyCommutativeSemiring[Request, Action: CommutativeSemiring]
      : CommutativeSemiring[Policy[Request, Action]] =
    new CommutativeSemiring[Policy[Request, Action]] {
      def +(x: Policy[Request, Action], y: Policy[Request, Action]): Policy[Request, Action] =
        Policy.oneOf(x, y)

      def zero: Policy[Request, Action] = Policy.zero

      def *(x: Policy[Request, Action], y: Policy[Request, Action]): Policy[Request, Action] =
        Policy.allOf(x, y)

      val one: Policy[Request, Action] = Policy.one
    }

  final case class Base[Request, Action](f: Request => Option[Action])
      extends Policy[Request, Action] {
    def apply(r: Request): Option[Action] = f(r)
  }
  final case class OneOf[Request, Action: CommutativeSemiring](
      policies: Seq[Policy[Request, Action]]
  ) extends Policy[Request, Action] {
    def apply(r: Request): Option[Action] =
      CommutativeSemiring.andAll(policies.map(_.apply(r)))
  }
  final case class AllOf[Request, Action: CommutativeSemiring](
      policies: Seq[Policy[Request, Action]]
  ) extends Policy[Request, Action] {
    def apply(r: Request): Option[Action] =
      CommutativeSemiring.orAll(policies.map(_.apply(r)))
  }

  def apply[Request, Action](f: Request => Option[Action]): Policy[Request, Action] =
    Base(f)

  /** The policy that never produces an action for any request
    */
  def zero[Request, Action]: Policy[Request, Action] =
    Policy(_ => none[Action])

  /** The policy that produces the identity action for any request
    */
  def one[Request, Action: Monoid]: Policy[Request, Action] =
    Policy(_ => Monoid[Action].empty.some)

  def oneOf[Request, Action: CommutativeSemiring](
      policies: Policy[Request, Action]*
  ): Policy[Request, Action] =
    OneOf(policies)

  def allOf[Request, Action: CommutativeSemiring](
      policies: Policy[Request, Action]*
  ): Policy[Request, Action] =
    AllOf(policies)
}
