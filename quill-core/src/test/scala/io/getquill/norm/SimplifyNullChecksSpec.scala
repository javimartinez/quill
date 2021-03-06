package io.getquill.norm

import io.getquill.Spec
import io.getquill.ast._
import io.getquill.ast.Implicits._

class SimplifyNullChecksSpec extends Spec {

  val ia = Ident("a")
  val ib = Ident("b")
  val it = Ident("t")
  val ca = Constant("a")

  "center rule must" - {
    "apply when conditionals same" in {
      SimplifyNullChecks(
        IfExist(
          IfExistElseNull(ia, it),
          IfExistElseNull(ia, it),
          Ident("o")
        )
      ) mustEqual If(
          IsNotNullCheck(Ident("a")) +&&+ IsNotNullCheck(Ident("t")), Ident("t"), Ident("o")
        )
    }

    "apply left rule" in {
      SimplifyNullChecks(
        IfExist(IfExistElseNull(ia, ib), ca, it)
      ) mustEqual If(IsNotNullCheck(ia) +&&+ IsNotNullCheck(ib), ca, it)
    }

    "apply right rule" in {
      SimplifyNullChecks(
        IfExistElseNull(ia, IfExistElseNull(ib, it))
      ) mustEqual If(IsNotNullCheck(ia) +&&+ IsNotNullCheck(ib), it, NullValue)
    }
  }
}
