package policykit

final case class Vote[Role](role: Role, quorum: Int)
